import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import AuthLayout from './AuthLayout';
import AlertBanner from './AlertBanner';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { register } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (formData.password !== formData.confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        setLoading(true);

        const result = await register(
            formData.username,
            formData.email,
            formData.password
        );

        if (result.success) {
            navigate('/dashboard');
        } else {
            setError(result.error);
        }

        setLoading(false);
    };

    return (
        <AuthLayout
            title="Create your account"
            subtitle="Join Solar Watch to access secured sunrise/sunset data."
            footer={
                <>
                    Already have an account?{' '}
                    <Link className="link link-primary" to="/login">
                        Login
                    </Link>
                </>
            }
        >
            {error ? <AlertBanner type="error" title="Registration failed">{error}</AlertBanner> : null}

            <form onSubmit={handleSubmit} className="mt-4 space-y-3">
                <div className="form-control">
                    <label className="label" htmlFor="username">
                        <span className="label-text">Username</span>
                    </label>
                    <input
                        className="input input-bordered"
                        type="text"
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                        disabled={loading}
                        autoComplete="username"
                    />
                </div>

                <div className="form-control">
                    <label className="label" htmlFor="email">
                        <span className="label-text">Email</span>
                    </label>
                    <input
                        className="input input-bordered"
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        disabled={loading}
                        autoComplete="email"
                    />
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                    <div className="form-control">
                        <label className="label" htmlFor="password">
                            <span className="label-text">Password</span>
                        </label>
                        <input
                            className="input input-bordered"
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            disabled={loading}
                            autoComplete="new-password"
                        />
                    </div>

                    <div className="form-control">
                        <label className="label" htmlFor="confirmPassword">
                            <span className="label-text">Confirm</span>
                        </label>
                        <input
                            className="input input-bordered"
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            required
                            disabled={loading}
                            autoComplete="new-password"
                        />
                    </div>
                </div>

                <button className="btn btn-primary w-full" type="submit" disabled={loading}>
                    {loading ? (
                        <span className="inline-flex items-center gap-2">
                            <span className="loading loading-spinner" />
                            Creating account…
                        </span>
                    ) : (
                        'Register'
                    )}
                </button>
            </form>
        </AuthLayout>
    );
};

export default Register;
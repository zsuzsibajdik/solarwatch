import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from '../hooks/useAuth.js';
import AuthLayout from './AuthLayout';
import AlertBanner from './AlertBanner';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        const result = await login(username, password);

        if (result.success) {
            navigate('/dashboard');
        } else {
            setError(result.error)
        }

        setLoading(false);
    };

    return (
        <AuthLayout
            title="Welcome back"
            subtitle="Sign in to access your Solar Watch dashboard."
            footer={
                <>
                    Don&apos;t have an account?{' '}
                    <Link className="link link-primary" to="/register">
                        Register
                    </Link>
                </>
            }
        >
            {error ? <AlertBanner type="error" title="Login failed">{error}</AlertBanner> : null}

            <form onSubmit={handleSubmit} className="mt-4 space-y-3">
                <div className="form-control">
                    <label className="label" htmlFor="username">
                        <span className="label-text">Username</span>
                    </label>
                    <input
                        className="input input-bordered"
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Username"
                        autoComplete="username"
                        required
                        disabled={loading}
                    />
                </div>

                <div className="form-control">
                    <label className="label" htmlFor="password">
                        <span className="label-text">Password</span>
                    </label>
                    <input
                        className="input input-bordered"
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        autoComplete="current-password"
                        required
                        disabled={loading}
                    />
                </div>

                <button className="btn btn-primary w-full" type="submit" disabled={loading}>
                    {loading ? (
                        <span className="inline-flex items-center gap-2">
                            <span className="loading loading-spinner" />
                            Logging in…
                        </span>
                    ) : (
                        'Login'
                    )}
                </button>
            </form>
        </AuthLayout>
    );
};

export default Login;
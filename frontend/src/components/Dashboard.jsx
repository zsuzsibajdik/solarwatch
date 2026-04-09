import { useAuth } from '../hooks/useAuth';
import { axiosPrivate } from '../api/axios';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AlertBanner from './AlertBanner';

const Dashboard = () => {
    const { user, logout } = useAuth();
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axiosPrivate.get('/user/profile');
                setMessage(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
                setError('Could not load profile message from the server.');
            }
        };

        fetchData();
    }, []);

    const handleLogout = async () => {
        await logout();
        navigate('/login');
    };

    return (
        <div className="space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-3">
                <div>
                    <h1 className="text-2xl sm:text-3xl font-bold">Dashboard</h1>
                    <p className="text-base-content/70">Account overview & server status.</p>
                </div>

                <button onClick={handleLogout} className="btn btn-outline btn-error">
                    Logout
                </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="stat bg-base-100 rounded-box shadow">
                    <div className="stat-title">Signed in as</div>
                    <div className="stat-value text-xl">{user?.username || '—'}</div>
                    <div className="stat-desc">Your username</div>
                </div>
                <div className="stat bg-base-100 rounded-box shadow">
                    <div className="stat-title">Roles</div>
                    <div className="stat-value text-xl">{user?.roles?.length || 0}</div>
                    <div className="stat-desc truncate">{user?.roles?.join(', ') || '—'}</div>
                </div>
                <div className="stat bg-base-100 rounded-box shadow">
                    <div className="stat-title">Quick link</div>
                    <div className="stat-value text-xl">Solar Watch</div>
                    <div className="stat-actions">
                        <a className="btn btn-sm btn-primary" href="/solar-watch">Open</a>
                    </div>
                </div>
            </div>

            {error ? <AlertBanner type="warning" title="Heads up">{error}</AlertBanner> : null}
        </div>
    );
};

export default Dashboard;
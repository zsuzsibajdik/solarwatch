import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { useEffect, useState } from 'react';

function NavItem({ to, children, end = false }) {
  return (
    <li>
      <NavLink
        to={to}
        end={end}
        className={({ isActive }) => (isActive ? 'active font-semibold' : undefined)}
      >
        {children}
      </NavLink>
    </li>
  );
}

export default function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [isDark, setIsDark] = useState(false);

  useEffect(() => {
    let theme = 'light';
    try {
      theme = localStorage.getItem('theme') || 'light';
    } catch {
      // ignore
    }

    document.documentElement.setAttribute('data-theme', theme);
    setIsDark(theme === 'dark');
  }, []);

  async function onLogout() {
    await logout();
    navigate('/login');
  }

  return (
    <div className="navbar bg-base-100/80 backdrop-blur border-b border-base-300 sticky top-0 z-50">
      <div className="navbar-start">
        <div className="dropdown">
          <div tabIndex={0} role="button" className="btn btn-ghost lg:hidden" aria-label="Open menu">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </div>
          <ul
            tabIndex={0}
            className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow"
          >
            <NavItem to="/solar-watch" end>
              Solar Watch
            </NavItem>
            <NavItem to="/dashboard">Dashboard</NavItem>
            {user?.roles?.includes('ROLE_ADMIN') && <NavItem to="/admin">Admin</NavItem>}
          </ul>
        </div>

        <Link to="/solar-watch" className="btn btn-ghost text-lg tracking-tight">
          Solar Watch
        </Link>
      </div>

      <div className="navbar-center hidden lg:flex">
        <ul className="menu menu-horizontal px-1">
          <NavItem to="/solar-watch" end>
            Solar Watch
          </NavItem>
          <NavItem to="/dashboard">Dashboard</NavItem>
          {user?.roles?.includes('ROLE_ADMIN') && <NavItem to="/admin">Admin</NavItem>}
        </ul>
      </div>

      <div className="navbar-end gap-2">
        <label className="swap swap-rotate btn btn-ghost btn-circle" title="Toggle theme">
          <input
            type="checkbox"
            onChange={(e) => {
              const theme = e.target.checked ? 'dark' : 'light';
              document.documentElement.setAttribute('data-theme', theme);
              setIsDark(theme === 'dark');
              try {
                localStorage.setItem('theme', theme);
              } catch {
                // ignore
              }
            }}
            checked={isDark}
          />
          <svg
            className="swap-on h-5 w-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364-6.364-.707.707M6.343 17.657l-.707.707m12.728 0-.707-.707M6.343 6.343l-.707-.707M12 6a6 6 0 1 0 0 12 6 6 0 0 0 0-12Z"
            />
          </svg>
          <svg
            className="swap-off h-5 w-5"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79Z"
            />
          </svg>
        </label>

        {isAuthenticated ? (
          <div className="dropdown dropdown-end">
            <div tabIndex={0} role="button" className="btn btn-ghost">
              <div className="avatar placeholder mr-2">
                <div className="bg-primary text-primary-content rounded-full w-8">
                  <span className="text-sm">{(user?.username || 'U').slice(0, 1).toUpperCase()}</span>
                </div>
              </div>
              <span className="hidden sm:inline">{user?.username}</span>
            </div>
            <ul tabIndex={0} className="menu dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-56 p-2 shadow">
              <li className="menu-title">
                <span>Account</span>
              </li>
              <li>
                <button type="button" onClick={onLogout} className="text-error">
                  Logout
                </button>
              </li>
            </ul>
          </div>
        ) : (
          <div className="join">
            <Link className="btn btn-ghost join-item" to="/login">
              Login
            </Link>
            <Link className="btn btn-primary join-item" to="/register">
              Register
            </Link>
          </div>
        )}
      </div>
    </div>
  );
}



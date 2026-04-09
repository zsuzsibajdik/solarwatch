import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import SolarWatchPage from './components/SolarWatchPage';
import AppLayout from './components/AppLayout';

function App() {
  return (
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            <Route
                path="/dashboard"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Dashboard />
                    </AppLayout>
                  </ProtectedRoute>
                }
            />

            <Route
                path="/solar-watch"
                element={
                  <ProtectedRoute>
                    <AppLayout>
                      <SolarWatchPage />
                    </AppLayout>
                  </ProtectedRoute>
                }
            />
            <Route
                path="/admin"
                element={
                  <ProtectedRoute requiredRole="ROLE_ADMIN">
                    <AppLayout>
                      <div className="card bg-base-100 shadow">
                        <div className="card-body">
                          <h1 className="card-title">Admin Panel</h1>
                          <p className="text-base-content/70">
                            You can place admin-only management tools here.
                          </p>
                        </div>
                      </div>
                    </AppLayout>
                  </ProtectedRoute>
                }
            />

            <Route path="/" element={<Navigate to="/solar-watch" replace />} />

            <Route
                path="/unauthorized"
                element={
                  <div className="min-h-dvh bg-base-200 grid place-items-center p-4">
                    <div className="card bg-base-100 shadow-xl max-w-md w-full">
                      <div className="card-body">
                        <h1 className="card-title">Unauthorized</h1>
                        <p className="text-base-content/70">You don't have permission to view this page.</p>
                        <div className="card-actions justify-end">
                          <a className="btn btn-primary" href="/solar-watch">Go back</a>
                        </div>
                      </div>
                    </div>
                  </div>
                }
            />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
  );
}

export default App;
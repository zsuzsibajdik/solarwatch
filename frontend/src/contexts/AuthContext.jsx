import { createContext, useState, useEffect } from "react";
import authService from "../services/authService.js";
import { tokenStorage, axiosPublic } from "../api/axios.js";

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const initAuth = async () => {
            const currentUser = authService.getCurrentUser();

            if (currentUser) {
                setUser(currentUser);
                setLoading(false);
            } else {

                try {
                    const response = await axiosPublic.post('/auth/refresh');
                    if (response.data.accessToken) {
                        tokenStorage.setAccessToken(response.data.accessToken);
                        const refreshedUser = authService.getCurrentUser();
                        setUser(refreshedUser);
                    }
                } catch (error) {
                    console.log('No valid session');
                } finally {
                    setLoading(false)
                }
            }
        };

        initAuth();
    }, []);

    const login = async (username, password) => {
        try {
            const data = await authService.login(username, password);
            const currentUser = authService.getCurrentUser();
            setUser(currentUser);
            return { success: true, data };
        } catch (error) {
            return {
              success: false,
              error: error.response?.data?.message || 'Login failed'
            };
        }
    };

    const register = async (username, email, password) => {
        try {
            const data = await authService.register(username, email, password);
            const currentUser = authService.getCurrentUser();
            setUser(currentUser);
            return { success: true, data };
        } catch (error) {
            return {
                success: false,
                error: error.response?.data?.message || 'Registration failed'
            };
        }
    };

    const logout = async () => {
        await authService.logout();
        setUser(null);
    };

    const hasRole = (role) => {
        return user?.roles?.includes(role) || false;
    };

    const value = {
        user,
        login,
        register,
        logout,
        hasRole,
        isAuthenticated: !!user,
        loading
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};
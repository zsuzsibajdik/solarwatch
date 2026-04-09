import { axiosPublic, tokenStorage } from "../api/axios.js";

const authService = {
    register: async (username, email, password) => {
        const response = await axiosPublic.post('/auth/register', {
            username,
            email,
            password
        });

        if (response.data.accessToken) {
            tokenStorage.setAccessToken(response.data.accessToken);
        }

        return response.data;
    },

    login: async (username, password) => {
        const response = await axiosPublic.post('/auth/login', {
            username,
            password
        });

        if (response.data.accessToken) {
            tokenStorage.setAccessToken(response.data.accessToken);
        }

        return response.data;
    },

    logout: async () => {
        try {
            await axiosPublic.post('/auth/logout');
        } catch (error) {
            console.error('Logout error: ', error);
        } finally {
            tokenStorage.clearTokens();
        }
    },

    getCurrentUser: () => {
        const accessToken = tokenStorage.getAccessToken();
        if (!accessToken) return null;

        try {
            const payload = JSON.parse(atob(accessToken.split('.')[1]));

            if (payload.exp * 1000 < Date.now()) {
                return null;
            }

            return {
                username: payload.sub,
                roles: payload.roles ? payload.roles.split(',') : []
            };
        } catch (error) {
            return null;
        }
    },

    isAuthenticated: () => {
        return !!tokenStorage.getAccessToken();
    }
};

export default authService;
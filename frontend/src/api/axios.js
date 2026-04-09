import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

export const axiosPublic = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

export const axiosPrivate = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true
});

axiosPublic.defaults.withCredentials = true;

let accessToken = null;

export const tokenStorage = {
    getAccessToken: () => accessToken,
    setAccessToken: (token) => {
        accessToken = token;
    },
    clearTokens: () => {
        accessToken = null;
    }
};

axiosPrivate.interceptors.request.use(
    (config) => {
        const accessToken = tokenStorage.getAccessToken();
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

axiosPrivate.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status !== 401 || originalRequest._retry) {
            return Promise.reject(error);
        }

        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                failedQueue.push({ resolve, reject });
            })
                .then(token => {
                    originalRequest.headers.Authorization = `Bearer ${token}`;
                    return axiosPrivate(originalRequest);
                })
                .catch(err => Promise.reject(err));
        }

        originalRequest._retry = true;
        isRefreshing = true;

        try {
            const response = await axiosPublic.post('/auth/refresh');

            const { accessToken } = response.data;
            tokenStorage.setAccessToken(accessToken);

            originalRequest.headers.Authorization = `Bearer ${accessToken}`;

            processQueue(null, accessToken);

            return axiosPrivate(originalRequest);
        } catch (refreshError) {
            processQueue(refreshError, null);
            tokenStorage.clearTokens();
            window.location.href = '/login';
            return Promise.reject(refreshError);
        } finally {
            isRefreshing = false;
        }
    }
);
import axios from 'axios';
import { useAuth } from '../context/AuthContext'; // Import your AuthContext or where you manage tokens

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // Set your base URL here
});

// Adding a request interceptor to include the JWT in headers

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        console.log("Token being attached:", token); // Log token
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        console.error("Error in request interceptor:", error);
        return Promise.reject(error);
    }
);
export default axiosInstance;

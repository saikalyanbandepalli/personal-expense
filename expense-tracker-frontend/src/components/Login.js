import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import axiosInstance from '../hooks/axiosInstance';
import './Login.css';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const { login } = useAuth(); // Get login function from context

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(null); // Clear previous error messages

        try {
            const response = await axiosInstance.post('api/users/login', { username, password });
            if (response.status === 200) {
                const { token } = response.data; // Assuming your API response contains the JWT token
                localStorage.setItem('token', token);
                login(username); // Set authenticated state
                navigate('/add-expense');
            }
        } catch (err) {
            // Handle different error cases
            if (err.response) {
                // Server responded with a status code outside of the 2xx range
                setError(err.response.data.message || 'Invalid username or password'); // Use server message if available
            } else if (err.request) {
                // Request was made but no response was received
                setError('Network error. Please try again later.');
            } else {
                // Something else happened
                setError('An error occurred. Please try again.');
            }
        }
    };

    return (
        <div className="login-container">
            <h1 className="text-center">Login</h1>
            <form onSubmit={handleLogin}>
                <div className="mb-3">
                    <label htmlFor="username" className="form-label">Username:</label>
                    <input
                        type="text"
                        id="username"
                        className="form-control"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="password" className="form-label">Password:</label>
                    <input
                        type="password"
                        id="password"
                        className="form-control"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-success w-100">Login</button>
            </form>
            {error && <p className="text-danger text-center mt-2">{error}</p>} {/* Display error message if any */}
        </div>
    );
};

export default Login;

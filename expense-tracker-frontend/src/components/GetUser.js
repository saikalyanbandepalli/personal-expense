import React, { useState } from 'react';
import axios from 'axios';
import axiosInstance from '../hooks/axiosInstance';
import './GetUser.css'

const GetUser = () => {
    const [username, setUsername] = useState('');
    const [userData, setUserData] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');

    const fetchUserByUsername = async () => {
        try {
            const response = await axiosInstance.get(`api/users/${username}`);
            setUserData(response.data);
            setErrorMessage('');
        } catch (error) {
            setUserData(null);
            setErrorMessage('User not found.');
            console.error(error);
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        fetchUserByUsername();
    };

    return (
        <div>
            <h2>Get User by Username</h2>
            <form onSubmit={handleSearch}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Search</button>
            </form>
            {userData && (
                <div>
                    <h3>User Details</h3>
                    <p>Username: {userData.username}</p>
                    <p>Email: {userData.email}</p>
                    {/* Add other user details as needed */}
                </div>
            )}
            {errorMessage && <p>{errorMessage}</p>}
        </div>
    );
};

export default GetUser;

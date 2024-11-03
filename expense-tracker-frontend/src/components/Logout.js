import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { useAuth } from '../context/AuthContext';
import { clearExpenses } from '../components/expenseSlice'; // Import the clear action
import './Logout.css';

const Logout = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch(); // Get the dispatch function
    const { logout } = useAuth();

    useEffect(() => {
        const handleLogout = async () => {
            try {
                console.log('Logging out...');
                await logout(); // Ensure logout returns a promise if needed
                localStorage.removeItem('token'); // Remove token
                dispatch(clearExpenses()); // Dispatch clear action
                navigate('/Login'); // Redirect to login page
            } catch (error) {
                console.error('Error logging out:', error);
            }
        };

        handleLogout();
    }, [logout, navigate, dispatch]); // Add dispatch to the dependency array

    return (
        <div className="logout-container">
            <h1>Logging out...</h1>
        </div>
    );
};

export default Logout;

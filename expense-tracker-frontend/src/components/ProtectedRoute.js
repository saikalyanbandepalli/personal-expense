import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './ProtectedRoute.css'

const ProtectedRoute = ({ component: Component }) => {
    const { isAuthenticated } = useAuth();

    if (!isAuthenticated) {
        // Redirect to login if not authenticated
        return <Navigate to="/login" />;
    }

    return <Component />; // Render the component if authenticated
};

export default ProtectedRoute;

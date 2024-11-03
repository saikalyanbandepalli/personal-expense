import { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loggedUser, setloggedUser] = useState(null); // Better to use null for better state management
    const [userId, setUserId] = useState(null); // State to hold user ID

    const login = (username) => {
        setIsAuthenticated(true);
        setloggedUser(username); // Assuming user has a username property
        //setUserId(); // Assuming user has an id property
        //console.log(user.id);
        console.log("this is from auth context"+ username);
    };

    const logout = () => {
        setIsAuthenticated(false);
        setloggedUser(null); // Clear the username on logout
        //setUserId(null); // Clear the user ID on logout
        localStorage.removeItem('token'); // This ensures the token is removed on logout
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout, loggedUser}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);

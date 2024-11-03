import React from 'react';

const ServerError = () => {
    return (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h1>500 - Internal Server Error</h1>
            <p>Something went wrong on our end. Please try again later.</p>
            <a href="/">Go back to Home</a>
        </div>
    );
};

export default ServerError;

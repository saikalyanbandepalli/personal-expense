import React, { useContext } from 'react'; 
import { Link, useLocation } from 'react-router-dom';
import './Navbar.css'; // Import custom styles
import { useAuth } from '../context/AuthContext'; // Import your Auth context

const Navbar = () => {
  const { loggedUser } = useAuth(); // Get loggedUser state from AuthContext
  const location = useLocation(); // Get current route location

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light" style={{ padding: '1rem 2rem' }}>
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">
          <img 
            src="/track.png" // Replace with the path to your image
            alt="Logo"
            style={{ height: '50px', marginRight: '10px' }} // Adjust height as needed
          />
          Ｔｒａｃｋｅｒ
        </Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            {!loggedUser ? ( // Check if user is logged in
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/">
                    <button className="btn btn-primary btn-lg">Dashboard</button>
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/Register">
                    <button className="btn btn-primary btn-lg">Register</button>
                  </Link>
                </li>
              </>
            ) : (
              // Show Logout button only if not on Dashboard
              <>
                {location.pathname !== '/dashboard' && (
                  <li className="nav-item">
                    <Link className="nav-link" to="/Logout">
                      <button className="btn btn-primary btn-lg">Logout</button>
                    </Link>
                  </li>
                )}
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

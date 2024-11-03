import React from 'react';
import { Link } from 'react-router-dom';

const Dashboard = () => (
  <div
    className="container mt-5"
    style={{
      backgroundImage: "url('path-to-your-image.jpg')",
      backgroundSize: 'cover',
      backgroundPosition: 'center',
      minHeight: '100vh',
      paddingTop: '50px', 
    }}
  >
    <h1 className="text-center mb-4">Welcome to the Expense Tracker</h1>

    <div className="row justify-content-center mb-4">
      <div className="col-md-6 text-center">
        <p className="mb-3">
          To access your account, please <Link to="/login" className="btn btn-primary btn-lg">Log In</Link>.
        </p>
        <p>
          If you don’t have an account yet, you can <Link to="/register" className="btn btn-secondary btn-lg">Register Here</Link>.
        </p>
      </div>
    </div>

    <div className="row mt-4">
      <div className="col-12">
        <h2 className="text-center mb-4">About the Expense Tracker</h2>
        <div className="card">
          <div className="card-body">
            <p className="text-center">
              The Expense Tracker is a user-friendly application designed to help individuals and families manage their finances effortlessly. 
              With our intuitive interface, you can easily track your daily expenses, categorize them for better understanding, and analyze your spending habits over time.
            </p>
            <p className="text-center">
              Whether you're looking to save for a special occasion, pay off debt, or simply gain insight into your financial habits, 
              our Expense Tracker provides the tools you need to achieve your financial goals. 
              Say goodbye to the hassle of manual tracking and let us help you take control of your financial future!
            </p>
            <p className="text-center">
              Join us today, and start your journey toward smarter financial management!
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
);

export default Dashboard;

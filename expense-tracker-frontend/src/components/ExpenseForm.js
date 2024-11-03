import React, { useContext, useState } from 'react';
import axiosInstance from '../hooks/axiosInstance';
import ExpenseContext from '../context/ExpenseContext';
import { useAuth } from '../context/AuthContext'; // Import the AuthContext

const ExpenseForm = () => {
  const { setExpenses } = useContext(ExpenseContext);
  const { loggedUser } = useAuth(); // Get userId from AuthContext
  const [name, setName] = useState('');
  const [amount, setAmount] = useState(0);
  const [category, setCategory] = useState(''); // State for category
  const [errorMessages, setErrorMessages] = useState([]); // State for error messages

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessages([]); // Clear previous error messages

    try {
      const response = await axiosInstance.post('/api/expenses/add', {
        name,
        amount,
        category, // Include category in the request
        loggedUser // Include userId in the request
      });

      // Update local state with the newly added expense
      setExpenses(prevExpenses => [...prevExpenses, response.data]);
      setName('');
      setAmount(0);
      setCategory(''); // Reset category after submission
    } catch (err) {
      if (err.response && err.response.data) {
        // Capture validation errors from the backend
        const serverErrors = err.response.data; // Adjust this based on your server's response format
        setErrorMessages(Array.isArray(serverErrors) ? serverErrors : [serverErrors]);
      } else {
        setErrorMessages(['Failed to add expense. Please try again.']);
      }
      console.error(err); // Log the error for debugging
    }
  };

  return (
    <div className="container mt-4">
      <h2>Add Expense</h2>
      <form onSubmit={handleSubmit} className="needs-validation" noValidate>
        <div className="mb-3">
          <label htmlFor="expenseName" className="form-label">Expense Name</label>
          <input
            type="text"
            id="expenseName"
            className="form-control"
            placeholder="Expense Name"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
              setErrorMessages([]); // Clear errors when the user types
            }}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="amount" className="form-label">Amount</label>
          <input
            type="number"
            id="amount"
            className="form-control"
            placeholder="Amount"
            value={amount}
            onChange={(e) => {
              setAmount(Number(e.target.value));
              setErrorMessages([]); // Clear errors when the user types
            }}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="category" className="form-label">Category</label>
          <input
            type="text"
            id="category"
            className="form-control"
            placeholder="Category"
            value={category}
            onChange={(e) => {
              setCategory(e.target.value);
              setErrorMessages([]); // Clear errors when the user types
            }} // Handle category input
            required
          />
        </div>
        <button type="submit" className="btn btn-primary">Add Expense</button>
      </form>
      {errorMessages.length > 0 && (
        <div className="mt-3">
          {errorMessages.map((error, index) => (
            <p key={index} className="text-danger">{error}</p>
          ))}
        </div>
      )}
    </div>
  );
};

export default ExpenseForm;

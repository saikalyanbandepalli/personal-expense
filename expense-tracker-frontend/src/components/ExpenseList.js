import React, { useContext, useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import ExpenseItem from './ExpenseItem';
import './ExpenseForm.css';
import ExpenseContext from '../context/ExpenseContext';
import { setExpenses } from '../components/expenseSlice';
import axiosInstance from '../hooks/axiosInstance';
import ConfirmModal from './ConfirmModal'; // Import the modal

const colors = {
  blue: { light: '#66b2ff' },
  green: { light: '#80e0a8' },
  gray: { light: '#d3d3d3' },
};

const generateColor = (category) => {
  const colorKeys = Object.keys(colors);
  const index = category.length % colorKeys.length;
  const colorCategory = colors[colorKeys[index]];
  return colorCategory.light;
};

const ExpenseList = () => {
  const dispatch = useDispatch();
  const { expenses, setExpenses: setContextExpenses } = useContext(ExpenseContext);
  const [draggedExpense, setDraggedExpense] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [categoryToUpdate, setCategoryToUpdate] = useState('');
  const [error, setError] = useState(null); // State for error messages

  useEffect(() => {
    const fetchExpenses = async () => {
      try {
        const response = await axiosInstance.get('/api/expenses/allexpenses', {
          params: { loggedUser: 'yoshirin' },
        });

        setContextExpenses(response.data);
        dispatch(setExpenses(response.data));
      } catch (error) {
        setError('Error fetching expenses. Please try again.'); // Set error message
        console.error('Error fetching expenses:', error);
      }
    };

    fetchExpenses();
  }, [dispatch, setContextExpenses]);

  const groupedExpenses = expenses.reduce((acc, expense) => {
    if (!acc[expense.category]) {
      acc[expense.category] = [];
    }
    acc[expense.category].push(expense);
    return acc;
  }, {});

  const calculateTotal = (expenses) => {
    return expenses.reduce((total, expense) => total + expense.amount, 0);
  };

  const handleDragStart = (e, expense) => {
    setDraggedExpense(expense);
  };

  const handleDrop = (e, category) => {
    e.preventDefault();
    if (draggedExpense) {
      setCategoryToUpdate(category);
      setShowModal(true); // Show the modal
    }
  };

  const handleConfirm = async () => {
    setError(null); // Clear previous error messages
    // Proceed with updating the expense category in the backend
    try {
      await axiosInstance.put(`/api/expenses/update/${draggedExpense.id}`, {
        ...draggedExpense,
        category: categoryToUpdate, // New category after dragging
      });

      // Update local state
      setContextExpenses(prevExpenses => {
        return prevExpenses.map(exp =>
          exp.id === draggedExpense.id ? { ...exp, category: categoryToUpdate } : exp
        );
      });

      dispatch(setExpenses(prevExpenses =>
        prevExpenses.map(exp =>
          exp.id === draggedExpense.id ? { ...exp, category: categoryToUpdate } : exp
        )
      ));
    } catch (error) {
      setError('Error updating expense category. Please try again.'); // Set error message
      console.error('Error updating expense category:', error);
    }
    setShowModal(false); // Close the modal
    setDraggedExpense(null); // Reset dragged expense
  };

  const handleCancel = () => {
    setShowModal(false); // Close the modal
    setDraggedExpense(null); // Reset dragged expense
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  return (
    <div className="container mt-4">
      <div className="row">
        {Object.keys(groupedExpenses).map((category) => {
          const totalAmount = calculateTotal(groupedExpenses[category]);
          return (
            <div key={category} className="col-md-6 mb-4" onDragOver={handleDragOver} onDrop={(e) => handleDrop(e, category)}>
              <div className="card" style={{ backgroundColor: generateColor(category) }}>
                <div className="card-body">
                  <h2 className="card-title">{category}</h2>
                  <p className="card-text text-end" style={{ fontWeight: 'bold' }}>
                    Total: ${totalAmount.toFixed(2)}
                  </p>
                  <div className="expense-items">
                    {groupedExpenses[category].map((expense) => (
                      <ExpenseItem key={expense.id} expense={expense} onDragStart={handleDragStart} />
                    ))}
                  </div>
                </div>
              </div>
            </div>
          );
        })}
      </div>
      {showModal && (
        <ConfirmModal 
          message={`Do you want to move "${draggedExpense?.name}" to "${categoryToUpdate}"?`}
          onConfirm={handleConfirm}
          onCancel={handleCancel}
        />
      )}
      {error && <p className="text-danger mt-3">{error}</p>} {/* Display error message if any */}
    </div>
  );
};

export default ExpenseList;

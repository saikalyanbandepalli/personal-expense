import React from 'react';
import './ExpenseItem.css'; // Import the CSS file

const ExpenseItem = ({ expense, onDragStart }) => {
  return (
    <div
      className="expense-item"
      draggable
      onDragStart={(e) => onDragStart(e, expense)}
      style={{ cursor: 'grab' }} // Optional: Change cursor to indicate draggable
    >
      <h5>{expense.name}</h5>
      <p>Amount: ${expense.amount.toFixed(2)}</p>
    </div>
  );
};

export default ExpenseItem;

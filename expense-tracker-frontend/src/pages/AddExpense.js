import React from 'react'; 
import ExpenseForm from '../components/ExpenseForm';
import InnerDashboard from './InnerDashboard';
//import './AddExpense.css'; // Optional: If you have specific styles for AddExpense

const AddExpense = () => (
  <div className="container mt-4">
    <h1 className="mb-4">Expenses</h1>
    <div className="card">
      <div className="card-body">
        <ExpenseForm />
      </div>
    </div>
    <InnerDashboard />
   
  </div>
);

export default AddExpense;

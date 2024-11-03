// ConfirmModal.js
import React from 'react';
import './ConfirmModal.css'; // Add styles for your modal

const ConfirmModal = ({ message, onConfirm, onCancel }) => {
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Confirmation</h2>
        <p>{message}</p>
        <div className="modal-buttons">
          <button onClick={onConfirm}>OK</button>
          <button onClick={onCancel}>Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;

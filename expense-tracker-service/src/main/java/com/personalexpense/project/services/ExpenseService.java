package com.personalexpense.project.services;


import com.personalexpense.project.model.Expense;
import com.personalexpense.project.repositories.ExpenseRepository;
import com.personalexpense.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;

    public Expense addExpense(Expense expense) {
        userRepository.save(expense.getUser());
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUser(String username) {
        return expenseRepository.findByUserUsername(username);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> getAllExpenses() {
       return expenseRepository.findAll();
    }


    public Optional<Expense> getExpensesById(Long expenseId) {
        return expenseRepository.findById(expenseId);
    }
}

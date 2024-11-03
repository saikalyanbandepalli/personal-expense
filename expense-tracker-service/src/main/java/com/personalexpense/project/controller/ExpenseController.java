package com.personalexpense.project.controller;


import com.personalexpense.project.dto.ExpenseRequest;
import com.personalexpense.project.exception.ResourceNotFoundException;
import com.personalexpense.project.model.Expense;
import com.personalexpense.project.model.User;
import com.personalexpense.project.services.ExpenseService;
import com.personalexpense.project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest, BindingResult result) throws ResourceNotFoundException {
        // Check if there are validation errors
        if (result.hasErrors()) {
            // Create a map to store validation errors
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            // Return validation errors in response with BAD_REQUEST status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Fetch the logged-in user by username
        System.out.println("The logged-in user is " + expenseRequest.getLoggedUser());
        User user = userService.findByUsername(expenseRequest.getLoggedUser());
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + expenseRequest.getLoggedUser());
        }

        // Create the expense object with user association
        Expense expense = new Expense(
                expenseRequest.getName(),
                expenseRequest.getAmount(),
                expenseRequest.getCategory(),
                expenseRequest.getDate(),
                user
        );

        // Save the expense and return it in the response
        return ResponseEntity.ok(expenseService.addExpense(expense));
    }

    @GetMapping
    public List<Expense> getExpensesByemail(String email) throws ResourceNotFoundException {

        return expenseService.getExpensesByUser(email);
    }

    @GetMapping("/allexpenses")
    public List<Expense> getExpensesByUsername(@RequestParam("loggedUser")  String username) throws ResourceNotFoundException {
        System.out.println("the username is "+username);
        //userService.getExpensesByUsername(username);
        return expenseService.getExpensesByUser(username);
    }

    @GetMapping("/expenses")
    public List<Expense> getAllExpenses() throws ResourceNotFoundException {
        return expenseService.getAllExpenses();
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateExpense(
            @PathVariable(value = "id") Long expenseId,
            @Valid @RequestBody Expense updatedExpense, // Adding @Valid to validate the request body
            BindingResult result) throws ResourceNotFoundException {

        // Check if there are validation errors
        if (result.hasErrors()) {
            // Create a map to store validation errors
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            // Return validation errors in response with BAD_REQUEST status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Find the existing expense by ID
        Expense existingExpense = expenseService.getExpensesById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found for this id :: " + expenseId));

        // Update the existing expense fields
        existingExpense.setName(updatedExpense.getName());
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory()); // Update the category

        // Save the updated expense
        final Expense savedExpense = expenseService.addExpense(existingExpense);

        // Return the updated expense
        return ResponseEntity.ok(savedExpense);
    }
}

package com.personalexpense.project.mockito;



import com.personalexpense.project.controller.ExpenseController;
import com.personalexpense.project.dto.ExpenseRequest;
import com.personalexpense.project.exception.ResourceNotFoundException;
import com.personalexpense.project.model.Expense;
import com.personalexpense.project.model.User;
import com.personalexpense.project.services.ExpenseService;
import com.personalexpense.project.services.UserService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController expenseController;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addExpense_ShouldReturnExpense() throws ResourceNotFoundException {
        ExpenseRequest expenseRequest = new ExpenseRequest();
        expenseRequest.setLoggedUser("testuser");
        expenseRequest.setName("Test Expense");
        expenseRequest.setAmount(100.0);
        expenseRequest.setCategory("Food");

        User user = new User();
        user.setUsername("testuser");

        when(userService.findByUsername(expenseRequest.getLoggedUser())).thenReturn(user);
        when(expenseService.addExpense(any(Expense.class))).thenReturn(new Expense());

        ResponseEntity<?> response = expenseController.addExpense(expenseRequest, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getExpensesByUsername_ShouldReturnExpenses() throws ResourceNotFoundException {
        String username = "testuser";
        when(expenseService.getExpensesByUser(username)).thenReturn(List.of(new Expense()));

        ResponseEntity<List<Expense>> response = ResponseEntity.ok(expenseController.getExpensesByUsername(username));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteExpense_ShouldCallDeleteService() {
        Long expenseId = 1L;

        expenseController.deleteExpense(expenseId);

        verify(expenseService, times(1)).deleteExpense(expenseId);
    }
}


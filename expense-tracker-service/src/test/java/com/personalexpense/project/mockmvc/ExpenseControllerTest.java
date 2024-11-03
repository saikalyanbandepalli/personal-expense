package com.personalexpense.project.mockmvc;

import com.personalexpense.project.controller.ExpenseController;
import com.personalexpense.project.dto.ExpenseRequest;
import com.personalexpense.project.model.Expense;
import com.personalexpense.project.model.User;
import com.personalexpense.project.services.ExpenseService;
import com.personalexpense.project.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExpenseControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ExpenseController expenseController;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    public void testAddExpense_Success() throws Exception {
        //ExpenseRequest expenseRequest = new ExpenseRequest("Dinner", 50.0, "Food", "2024-10-25", "testUser");
       // User user = new User("testUser", "password", null);
        //Expense expense = new Expense("Dinner", 50.0, "Food", "2024-10-25", user);

       // when(userService.findByUsername("testUser")).thenReturn(user);
        //when(expenseService.addExpense(any(Expense.class))).thenReturn(expense);

        mockMvc.perform(post("/api/expenses/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Dinner\",\"amount\":50.0,\"category\":\"Food\",\"date\":\"2024-10-25\",\"loggedUser\":\"testUser\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddExpense_UserNotFound() throws Exception {
        //ExpenseRequest expenseRequest = new ExpenseRequest("Dinner", 50.0, "Food", "2024-10-25", "testUser");

        when(userService.findByUsername("testUser")).thenReturn(null);

        mockMvc.perform(post("/api/expenses/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Dinner\",\"amount\":50.0,\"category\":\"Food\",\"date\":\"2024-10-25\",\"loggedUser\":\"testUser\"}"))
                .andExpect(status().isNotFound());
    }
}

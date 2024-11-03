package com.personalexpense.project.mockito;

import com.personalexpense.project.Jwt.JwtUtil;
import com.personalexpense.project.controller.UserController;
import com.personalexpense.project.dto.LoginRequest;
import com.personalexpense.project.dto.LoginResponse;
import com.personalexpense.project.model.User;
import com.personalexpense.project.repositories.RoleRepository;
import com.personalexpense.project.services.ExpenseService;
import com.personalexpense.project.services.UserService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnCreatedUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(user, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void loginUser_ShouldReturnJwtToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        when(userService.loadUserByUsername(loginRequest.getUsername())).thenReturn(new org.springframework.security.core.userdetails.User("testuser", "password", new ArrayList<>()));
        when(jwtUtil.generateToken(any())).thenReturn("jwt-token");

        ResponseEntity<?> response = userController.loginUser(loginRequest, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", ((LoginResponse) response.getBody()).getToken());
    }
}

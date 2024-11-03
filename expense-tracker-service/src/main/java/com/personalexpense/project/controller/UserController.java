package com.personalexpense.project.controller;


import com.personalexpense.project.Jwt.JwtUtil;
import com.personalexpense.project.dto.LoginRequest;
import com.personalexpense.project.dto.LoginResponse;
import com.personalexpense.project.model.Expense;
import com.personalexpense.project.model.Role;
import com.personalexpense.project.model.User;
import com.personalexpense.project.repositories.RoleRepository;
import com.personalexpense.project.services.ExpenseService;
import com.personalexpense.project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody User user, // @Valid for validation
            BindingResult result) {        // BindingResult for error handling

        // Handle validation errors
        if (result.hasErrors()) {
            // Create a map for validation errors
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            // Return the validation errors with BAD_REQUEST status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Log received user data
        System.out.println("Received user data: " + user);
        System.out.println("Roles: " + user.getRoles());

        // Additional manual validation to ensure roles are present
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return ResponseEntity.badRequest().body("Roles are required."); // Return 400 if roles are missing
        }

        // Extract role names directly from the incoming User object
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName) // Extracting role names from the objects
                .collect(Collectors.toList());

        // Log the extracted role names for debugging
        System.out.println("Extracted role names: " + roleNames);

        // Fetch role entities from the database based on the extracted names
        List<Role> roleEntities = roleRepository.findByNameIn(roleNames);

        // If no valid roles are found in the database, return a 400 BAD REQUEST
        if (roleEntities.isEmpty()) {
            System.out.println("No valid roles found for names: " + roleNames);
            return ResponseEntity.badRequest().body("Invalid roles provided."); // Return 400 if no valid roles
        }

        // Log the fetched role entities
        System.out.println("Fetched role entities: " + roleEntities);

        // Set the roles in the user object as fetched Role entities
        user.setRoles(new HashSet<>(roleEntities));

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user via the userService
        User createdUser = userService.registerUser(user);

        // Check if user creation was successful
        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // Return 201 Created with the user
        } else {
            // Log if there was an error creating the user
            System.out.println("User creation failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return 500 if saving the user failed
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @Valid @RequestBody LoginRequest loginRequest, // Validating login request
            BindingResult result) {                       // BindingResult for validation errors

        // Handle validation errors
        if (result.hasErrors()) {
            // Create a map to collect validation error messages
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); // Return 400 Bad Request with validation errors
        }

        try {
            // Log the received username
            System.out.println(loginRequest.getUsername());

            // Check if username is null
            if (loginRequest.getUsername() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("User not found"));
            }

            // Authenticate the user using the provided username and password
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Load user details
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

            // Generate JWT token
            String jwt = jwtUtil.generateToken(userDetails);

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Return JWT token in the response
            return ResponseEntity.ok(new LoginResponse(jwt));

        } catch (InternalAuthenticationServiceException e) {
            // Log the exception and return a custom message for multiple user issues
            System.out.println("Authentication failed: " + e.getMessage());
            String errorMessage = "Authentication error: Multiple users found with the same credentials. Please contact support.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse(errorMessage));

        } catch (BadCredentialsException e) {
            // Return error response with invalid credentials message
            String errorMessage = "Invalid credentials";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(errorMessage));

        } catch (Exception e) {
            // Log other exceptions and return a general error response
            e.printStackTrace();
            String errorMessage = "An error occurred during authentication";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse(errorMessage));
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username); // Assuming this returns User or null

        if (user != null) {
            return ResponseEntity.ok(user); // Return 200 OK with the user if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 NOT FOUND if not found
        }
    }
}

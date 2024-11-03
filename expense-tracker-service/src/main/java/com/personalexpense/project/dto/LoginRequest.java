package com.personalexpense.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Username is required") // Ensures the username is not null or empty
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters") // Sets length constraints
    private String username;
    @NotBlank(message = "Password is required") // Ensures the password is not null or empty
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters") // Sets minimum length for password
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

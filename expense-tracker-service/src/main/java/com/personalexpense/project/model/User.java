package com.personalexpense.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required") // Ensures the username is not null or empty
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters") // Sets length constraints
    private String username;

    @NotBlank(message = "Password is required") // Ensures the password is not null or empty
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters") // Sets minimum length for password
    private String password;

    @NotBlank(message = "Email is required") // Ensures the email is not null or empty
    @Email(message = "Email should be valid") // Validates the email format
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Expense> expenses;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles= new HashSet<>(); // User roles

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(String username, String password, String email, Set<Expense> expenses, Set<Role> roles) {
       // this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.expenses = expenses;
        this.roles = roles;
    }

    // Getters and Setters...


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles + // This will call toString on Role if it has one
                ", expenses=" + expenses + // This will call toString on Expense if it has one
                '}';
    }
}

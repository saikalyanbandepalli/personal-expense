package com.personalexpense.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Component
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required") // Ensures the name is not null and not empty
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Amount is required") // Ensures the amount is not null
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0") // Ensures the amount is positive
    private Double amount;

    @NotBlank(message = "Category is required") // Ensures category is not null or empty
    @Size(min = 3, max = 50, message = "Category must be between 3 and 50 characters")
    private String category;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Expense() {

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // Default constructor

    // Parameterized constructor
    public Expense( String name, double amount, String category, LocalDate date, User user) {
       // this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.user = user; // Ensure the user is set here
    }


}

package com.personalexpense.project.repositories;




import com.personalexpense.project.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e FROM Expense e JOIN e.user u WHERE u.email = :email")
    List<Expense> findByUserEmail(String email);

    List<Expense> findAll();
    //List<Expense> getAllExpenses();
    @Query("SELECT e FROM Expense e JOIN e.user u WHERE u.username = :username")
    List<Expense> findByUserUsername(String username);

     //Expense findById(Long id);

}


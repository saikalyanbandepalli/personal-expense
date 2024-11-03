package com.personalexpense.project.repositories;


import com.personalexpense.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Long findIdByUsername(@Param("username") String username);

}


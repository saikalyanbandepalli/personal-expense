package com.personalexpense.project.repositories;



import com.personalexpense.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    List<Role> findByNameIn(List<String> names);
    // Custom query to find a role by name
}


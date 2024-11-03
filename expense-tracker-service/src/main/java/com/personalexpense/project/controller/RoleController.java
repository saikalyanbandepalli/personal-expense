package com.personalexpense.project.controller;



import com.personalexpense.project.dto.RoleDTO;
import com.personalexpense.project.model.Role;

import com.personalexpense.project.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping("/getroles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles(); // Ensure this matches the return type of the service
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/addrole")
    public ResponseEntity<Role> addRole(@Valid @RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }


}

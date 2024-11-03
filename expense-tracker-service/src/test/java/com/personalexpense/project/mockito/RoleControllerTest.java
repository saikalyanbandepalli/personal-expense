package com.personalexpense.project.mockito;

import com.personalexpense.project.controller.RoleController;
import com.personalexpense.project.dto.RoleDTO;
import com.personalexpense.project.model.Role;
import com.personalexpense.project.services.RoleService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRoles_ShouldReturnRoleList() {
        RoleDTO roleDTO = new RoleDTO();
        when(roleService.getAllRoles()).thenReturn(List.of(roleDTO));

        ResponseEntity<List<RoleDTO>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void addRole_ShouldReturnCreatedRole() {
        Role role = new Role();
        when(roleService.createRole(any(Role.class))).thenReturn(role);

        ResponseEntity<Role> response = roleController.addRole(role);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(role, response.getBody());
    }
}

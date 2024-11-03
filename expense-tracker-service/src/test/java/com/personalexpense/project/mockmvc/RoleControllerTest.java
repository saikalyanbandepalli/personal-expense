package com.personalexpense.project.mockmvc;

import com.personalexpense.project.controller.RoleController;
import com.personalexpense.project.dto.RoleDTO;
import com.personalexpense.project.model.Role;
import com.personalexpense.project.services.RoleService;
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
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

       // when(roleService.getAllRoles()).thenReturn((List<RoleDTO>) List.of(role));

        ResponseEntity<List<RoleDTO>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(role.getId(), response.getBody().get(0).getId());
        assertEquals(role.getName(), response.getBody().get(0).getName());
    }

    @Test
    void addRole_ShouldReturnCreatedRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        when(roleService.createRole(any(Role.class))).thenReturn(role);

        ResponseEntity<Role> response = roleController.addRole(role);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(role, response.getBody());
    }
}

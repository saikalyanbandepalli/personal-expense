package com.personalexpense.project.services;

import com.personalexpense.project.dto.RoleDTO;
import com.personalexpense.project.model.Role;
import com.personalexpense.project.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleDTO(Math.toIntExact(role.getId()), role.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Role createRole(Role role) {
        // Check if the role already exists to prevent duplication
        Optional<Role> existingRole = Optional.ofNullable(roleRepository.findByName(role.getName()));
        if (existingRole.isPresent()) {
            throw new IllegalArgumentException("Role with name '" + role.getName() + "' already exists");
        }
        return roleRepository.save(role);
    }

    public Role findByName(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            throw new IllegalArgumentException("Role with name '" + name + "' not found");
        }
        return role;
    }
}

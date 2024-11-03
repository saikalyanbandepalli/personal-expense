package com.personalexpense.project.services;



import com.personalexpense.project.model.Role;
import com.personalexpense.project.model.User;
import com.personalexpense.project.repositories.ExpenseRepository;
import com.personalexpense.project.repositories.RoleRepository;
import com.personalexpense.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }
    public User registerUser(User user) {
        // Perform validation and password encryption if necessary
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void addRoleToUser(User user, String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            user.getRoles().add(role);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Role not found");
        }
    }

    public Long findIdByUsername(String username){
        return userRepository.findIdByUsername(username);
    }
}

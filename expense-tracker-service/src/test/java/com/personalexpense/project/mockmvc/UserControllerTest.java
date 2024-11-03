package com.personalexpense.project.mockmvc;



import com.personalexpense.project.controller.UserController;
import com.personalexpense.project.dto.LoginRequest;
import com.personalexpense.project.model.User;
import com.personalexpense.project.services.UserService;
import com.personalexpense.project.Jwt.JwtUtil;
import com.personalexpense.project.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
       User user = new User();
       when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\",\"email\":\"test@gmail.com\",\"roles\":[\"admin\"]}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testLoginUser_Success() throws Exception {
       // LoginRequest loginRequest = new LoginRequest("testUser", "password");
        String jwt = "dummy.jwt.token";

       // when(userService.loadUserByUsername(loginRequest.getUsername())).thenReturn(null);
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtUtil.generateToken(any())).thenReturn(jwt);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }
}


package com.db.securityservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.db.securityservice.model.JwtRequest;
import com.db.securityservice.util.JwtHelper;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager manager;

    @Mock
    private JwtHelper helper;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(authController)
                .setControllerAdvice(new AuthController())
                .build();
    }

    @Test
    void login_success() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("admin", "password");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testAppId");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(helper.generateToken(any(UserDetails.class))).thenReturn("testToken");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"appId\":\"testAppId\", \"password\":\"testPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"jwtToken\":\"testToken\", \"username\":\"testAppId\"}"));
    }

    @Test
    void login_badCredentials() throws Exception {
        doThrow(new BadCredentialsException("Invalid Username or Password"))
                .when(manager).authenticate(any());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"appId\":\"wrongAppId\", \"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credentials Invalid !!"));
    }

    @Test
    void exceptionHandler() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"appId\":\"wrongAppId\", \"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credentials Invalid !!"));
    }
}
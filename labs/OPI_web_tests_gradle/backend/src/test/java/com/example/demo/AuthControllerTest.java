package com.example.demo;

import com.example.demo.controller.AuthController;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_ok() {
        String username = "karim";
        String password = "123456";
        when(userService.checkCredentials(username, password)).thenReturn(true);

        ResponseEntity<?> response = authController.login(username, password, session);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login success", response.getBody());
        verify(session).setAttribute("USER", username);
    }

    @Test
    void login_fail() {
        String username = "karim";
        String password = "123456";
        when(userService.checkCredentials(username, password)).thenReturn(false);

        ResponseEntity<?> response = authController.login(username, password, session);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void register_ok() {
        String username = "alice";
        String password = "pass123";

        ResponseEntity<?> response = authController.register(username, password);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered", response.getBody());
        verify(userService).register(username, password);
    }

    @Test
    void register_fail() {
        String username = "karim";
        String password = "123456";
        String errorMsg = "Username already exists";
        doThrow(new RuntimeException(errorMsg)).when(userService).register(username, password);

        ResponseEntity<?> response = authController.register(username, password);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errorMsg, response.getBody());
    }

    @Test
    void logout_ok() {
        ResponseEntity<?> response = authController.logout(session);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logout success", response.getBody());
        verify(session).invalidate();
    }
}

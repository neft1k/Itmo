package com.example.demo.controller;

import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password,
                                   HttpSession session) {
        try {
            if (userService.checkCredentials(username, password)) {
                session.setAttribute("USER", username);
                return ResponseEntity.ok("Login success");
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("login error");
        }
    }

    @PostMapping(path = "/register", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> register(@RequestParam String username,
                                      @RequestParam String password) {
        try {
            userService.register(username, password);
            return ResponseEntity.ok("User registered");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        String actor = (String) session.getAttribute("USER");
        if (actor == null || actor.isBlank()) actor = "anonymous";
        session.invalidate();
        return ResponseEntity.ok("Logout success");
    }


}



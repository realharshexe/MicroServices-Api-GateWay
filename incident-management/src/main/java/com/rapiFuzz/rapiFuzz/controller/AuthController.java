package com.rapiFuzz.rapiFuzz.controller;

import com.rapiFuzz.rapiFuzz.entity.User;
import com.rapiFuzz.rapiFuzz.model.UserDto;
import com.rapiFuzz.rapiFuzz.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User users) {
        try {
            authService.registerUser(users);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("incorrect email");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserDto userDTO) {
        try {
            String token = authService.authenticateUser(userDTO);
            return ResponseEntity.ok().body(token + "You've logged in successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials not logged in");
        }

    }
}

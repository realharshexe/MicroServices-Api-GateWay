package com.user_management.controller;

import com.user_management.model.ProfileDto;
import com.user_management.model.UserDto;
import com.user_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users and profiles")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    @Operation(summary = "Register as a new user", description = "creating an account based on email")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed for user with email: {}", userDTO.getEmail(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("incorrect email");
        } catch (Exception e) {
            logger.error("An unexpected error occurred during registration " +
                    "for user with email: {}", userDTO.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }
    @Operation(summary = "login as a existing user", description = "If user email already exists,he can login")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserDto userDTO) {
        try {
            String token = userService.authenticateUser(userDTO);
            logger.info("User logged in successfully: {}", userDTO.getEmail());
            return ResponseEntity.ok().body(token + "You've logged in successfully");
        } catch (Exception e) {
            logger.error("Login failed for email {}: {}", userDTO.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

    }
    @Operation(summary = "Get user profile", description = "Fetches user profile details by user ID")
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        try {
            ProfileDto profile = userService.getProfile(id);
            if (profile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
            }
            logger.info("Fetched profile for user ID: {}", id);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Error fetching profile for user ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching profile");
        }
    }

    @Operation(summary = "Update user profile", description = "Updates user profile details by user ID")
    @PutMapping("/profile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable Long id, @Valid @RequestBody ProfileDto profileDTO) {
        try {
            userService.updateProfile(id, profileDTO);
            logger.info("Profile updated successfully for user ID: {}", id);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (IllegalArgumentException e) {
            logger.warn("Profile update failed for user ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid profile data");
        } catch (Exception e) {
            logger.error("Unexpected error during profile update for user ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while updating profile");
        }
    }
}
package com.user_management.service;

public interface JwtService {
    String generateToken(String email);
    boolean validateToken(String token, String email);
    String extractUsername(String token);

}

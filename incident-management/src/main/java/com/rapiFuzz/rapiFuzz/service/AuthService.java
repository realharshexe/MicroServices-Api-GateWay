package com.rapiFuzz.rapiFuzz.service;

import com.rapiFuzz.rapiFuzz.dao.UserRepository;
import com.rapiFuzz.rapiFuzz.entity.User;
import com.rapiFuzz.rapiFuzz.model.UserDto;
import com.rapiFuzz.rapiFuzz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public void registerUser(User users) {
        // Check if user already exists
        if (userRepository.existsByEmail(users.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
         String encodedPassword = passwordEncoder.encode(users.getPassword());

        User user = new User();
        user.setUsername(users.getUsername());
        user.setEmail(users.getEmail());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(users.getPhoneNumber());
        user.setAddress(users.getAddress());
        user.setCity(users.getCity());
        user.setCountry(users.getCountry());
        user.setPinCode(users.getPinCode());
        user.setRole(users.getRole());
        userRepository.save(user);

    }

    public String authenticateUser(UserDto userDTO) {
        User users = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(userDTO.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid credentials, Password is not matching");
        }
        return jwtUtil.generateToken(users.getEmail());
    }
}

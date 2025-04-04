package com.user_management.service;

import com.user_management.dao.ProfileDao;
import com.user_management.dao.RoleDao;
import com.user_management.dao.UserDao;
import com.user_management.entity.Role;
import com.user_management.entity.Profiles;
import com.user_management.entity.Users;
import com.user_management.enums.UserStatus;
import com.user_management.model.ProfileDto;
import com.user_management.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao dao;
    private final ProfileDao profileDao;
    private final RoleDao roleDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public void registerUser(UserDto userDto) {
        // Check if user already exists
        if (dao.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        //  Fetch role_id from role table
        Role role = roleDao.findByRoles(userDto.getRoles())
                .orElseThrow(() -> new RuntimeException("Invalid role: " + userDto.getRoles()));
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        dao.save(user);

        // Creating an empty profile and linking it to the user
        Profiles profile = new Profiles();
        profile.setUser(user); // Associate profile with user
        profileDao.save(profile);
    }


    public String authenticateUser(UserDto userDTO) {
        Users users = dao.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(userDTO.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtService.generateToken(users.getEmail());
    }

    @Override
    public ProfileDto getProfile(Long profileId) {
        Profiles profiles = profileDao.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return ProfileDto.builder()
                .id(profiles.getId())
                .bio(profiles.getBio())
                .skills(profiles.getSkills())
                .location(profiles.getLocation())
                .experience(profiles.getExperience())
                .languages(profiles.getLanguages())
                .build();
    }

    @Override
    public void updateProfile(Long profileId, ProfileDto profileDTO) {
        Profiles profiles = profileDao.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profiles.setBio(profileDTO.getBio());
        profiles.setSkills(profileDTO.getSkills());
        profiles.setLocation(profileDTO.getLocation());
        profiles.setExperience(profileDTO.getExperience());
        profiles.setLanguages(profileDTO.getLanguages());
        profileDao.save(profiles);
    }
}

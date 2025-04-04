package com.user_management.service;

import com.user_management.model.ProfileDto;
import com.user_management.model.UserDto;

public interface UserService {
    void registerUser(UserDto userDTO);
    String authenticateUser(UserDto userDTO);
    ProfileDto getProfile(Long profileId);
    void updateProfile(Long profileId, ProfileDto profileDTO);
}

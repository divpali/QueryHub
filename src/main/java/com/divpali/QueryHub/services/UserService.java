package com.divpali.QueryHub.services;

import com.divpali.QueryHub.dto.UserResponseDto;
import com.divpali.QueryHub.dto.UserRequestDto;
import com.divpali.QueryHub.entities.User;

public interface UserService {

    public UserResponseDto createUserProfile(UserRequestDto userRequestDto);

    public User getUserById(Long userId);

    public User save(User user);
}

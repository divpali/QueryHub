package com.divpali.QueryHub.services;

import com.divpali.QueryHub.UserResponseDto;
import com.divpali.QueryHub.dto.UserRequestDto;

public interface UserService {

    public UserResponseDto createUserProfile(UserRequestDto userRequestDto);
}

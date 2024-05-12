package com.divpali.QueryHub.services.impl;

import com.divpali.QueryHub.dto.UserResponseDto;
import com.divpali.QueryHub.dto.UserRequestDto;
import com.divpali.QueryHub.entities.User;
import com.divpali.QueryHub.repository.UserRepository;
import com.divpali.QueryHub.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDto createUserProfile(UserRequestDto userRequestDto) {
        ModelMapper modelMapper = new ModelMapper();

        System.out.println("test: "+modelMapper);

        User user =  userRepository.save(modelMapper.map(userRequestDto, User.class));

        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}

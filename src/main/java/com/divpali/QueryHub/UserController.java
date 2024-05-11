package com.divpali.QueryHub;

import com.divpali.QueryHub.dto.UserRequestDto;
import com.divpali.QueryHub.exception.UserRegistrationException;
import com.divpali.QueryHub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUserProfile(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto userResponse = userService.createUserProfile(userRequestDto);
            return ResponseEntity.ok(userResponse);
        } catch (UserRegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

    }


}

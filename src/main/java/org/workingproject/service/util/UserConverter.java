package org.workingproject.service.util;


import org.springframework.stereotype.Component;
import org.workingproject.dto.UserRequestDto;
import org.workingproject.dto.UserResponseDto;
import org.workingproject.entity.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverter {

    public User fromDto(UserRequestDto dto) {
        User user = new User();

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public UserResponseDto toDto(User user) {

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getRoleName());
        return dto;
    }
    public List<UserResponseDto> toDtos(List<User> users) {
        return users.stream()
                .map(u -> toDto(u))
                .toList();
    }
}

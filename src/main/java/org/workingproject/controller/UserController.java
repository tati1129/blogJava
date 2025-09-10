package org.workingproject.controller;


import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.workingproject.dto.GeneralResponse;
import org.workingproject.dto.UserRequestDto;
import org.workingproject.dto.UserResponseDto;
import org.workingproject.dto.UserUpdateRequestDto;
import org.workingproject.entity.User;
import org.workingproject.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    // Создать пользователя
    @PostMapping
public GeneralResponse<UserResponseDto> create(@RequestBody UserRequestDto request){
        return service.createUser(request);
    }

    // Получить всех пользователей (пользовательский режим)
    @GetMapping("/admin")
    public GeneralResponse<List<User>> getAllForAdmin(){
        return service.getAllUSersAdmin();
    }

    // Получить пользователя по id
    @GetMapping("/{id}")
    public GeneralResponse<UserResponseDto> getById(@PathVariable Integer id){
        return service.getUserById(id);
    }

    // Обновить пользователя по id (на основе данных из UserUpdateRequestDto)
    @PutMapping("/{id}")
    public GeneralResponse<UserResponseDto> update(@RequestBody UserUpdateRequestDto updateRequest){
        return service.updateUser(updateRequest);
    }


    // найти пользователя по email
    // /api/users?email=...
    // /api/users?username=...

    @GetMapping()
    public GeneralResponse<List<UserResponseDto>> getAllUsersByParameter(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email
    ) {

        if (role != null && !role.isBlank()) {
            return service.getUserByRole(role);
        }
        if (username != null && !username.isBlank()){
            return service.getUserByUserName(username);
        }
        if (email != null && !email.isBlank()){
            UserResponseDto user =   service.getUserByEmail(email).getBody();
            return  new GeneralResponse<>(HttpStatus.OK,List.of(user),"Found by email");
        }

        return service.getAll();

    }

}

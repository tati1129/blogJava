package org.workingproject.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/newUser")
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto request) {
        return new ResponseEntity<>(service.createUser(request), HttpStatus.CREATED);
    }

    // Получить всех пользователей (пользовательский режим)
    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllForAdmin() {
        return ResponseEntity.ok(service.getAllUSersAdmin());
    }

    // Получить пользователя по id
    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Integer id) {
        return service.getUserById(id);
    }

    // Обновить пользователя по id (на основе данных из UserUpdateRequestDto)
    @PutMapping("/{id}")
    public UserResponseDto update(@RequestBody UserUpdateRequestDto updateRequest) {
        return service.updateUser(updateRequest);
    }

    @GetMapping()
    public List<UserResponseDto> getAllUsersByParameter(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email
    ) {

        if (role != null && !role.isBlank()) {
            return service.getUserByRole(role);
        }
        if (username != null && !username.isBlank()) {
            return service.getUserByUserName(username);
        }
        if (email != null && !email.isBlank()) {
            UserResponseDto user = service.getUserByEmail(email);
            return List.of(user);
        }

        return service.getAll();

    }

    @DeleteMapping("/{id}")
    public UserResponseDto deleteUser(@PathVariable Integer id) {
        return service.deleteUserById(id);
    }

}

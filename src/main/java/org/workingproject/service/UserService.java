package org.workingproject.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workingproject.dto.GeneralResponce;
import org.workingproject.dto.UserRequestDto;
import org.workingproject.dto.UserResponseDto;
import org.workingproject.dto.UserUpdateRequestDto;
import org.workingproject.entity.Role;
import org.workingproject.entity.User;
import org.workingproject.repository.UserRepository;
import org.workingproject.service.util.UserConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserConverter converter;

    public GeneralResponce<UserResponseDto> createUser(UserRequestDto request) {



        User user = converter.fromDto(request);
        user.setRole(Role.USER);

        LocalDate today = LocalDate.now();
        user.setCreateDate(today);
        user.setLastUpdate(today);

        User savedUser = repository.save(user);

        UserResponseDto response = converter.toDto(savedUser);
        return new GeneralResponce<>(HttpStatus.CREATED, response, "User created successfully");
    }

    public GeneralResponce<UserResponseDto> updateUser(UserUpdateRequestDto updateRequest) {
        Optional<User> userForUpdateOptional = repository.findById(updateRequest.getId());

        if (userForUpdateOptional.isEmpty()) {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "User not found");
        }

        User userForUpdate = userForUpdateOptional.get();

        if (updateRequest.getUserName() != null) {
            userForUpdate.setUserName(updateRequest.getUserName());
        }

        if (updateRequest.getPassword() != null) {
            userForUpdate.setPassword(updateRequest.getPassword());
        }

        userForUpdate.setLastUpdate(LocalDate.now());

        repository.save(userForUpdate);
        return new GeneralResponce<>(HttpStatus.OK, converter.toDto(userForUpdate), "User updated successfully");
    }

    public GeneralResponce<List<User>> getAllUSersAdmin() {
        return new GeneralResponce<>(HttpStatus.OK, repository.findAll(), "All Users Admin (Administrator mode)");
    }

    public GeneralResponce<List<UserResponseDto>> getAll() {

        List<UserResponseDto> response = repository.findAll().stream()
                .map(u -> converter.toDto(u))
                .toList();

        return new GeneralResponce<>(HttpStatus.OK, response, "All Users  (User mode)");
    }


    public GeneralResponce<UserResponseDto> getUserById(Integer id) {
        Optional<User> userByIdOptional = repository.findById(id);
        if (userByIdOptional.isPresent()) {
            User userById = userByIdOptional.get();
            UserResponseDto response = converter.toDto(userById);
            return new GeneralResponce<>(HttpStatus.OK, response, "User founded ");
        } else {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "User with id " + id + " not found ");
        }
    }

    public Optional<User> getUserByIdForPostService(Integer id) {
        Optional<User> userByIdOptional = repository.findById(id);
        if (userByIdOptional.isPresent()) {
            return userByIdOptional;
        } else {
            return Optional.empty();
        }
    }

    public GeneralResponce<UserResponseDto> getUserByEmail(String email) {
        Optional<User> userByEmailOptional = repository.findByEmail(email);

        if (userByEmailOptional.isPresent()) {
            User userByEmail = userByEmailOptional.get();
            UserResponseDto response = converter.toDto(userByEmail);
            return new GeneralResponce<>(HttpStatus.OK, response, "User founded ");
        } else {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "User with this email " + email + " not found ");
        }
    }

    public GeneralResponce<List<UserResponseDto>> getUserByRole(String role) {
        List<User> userByRole = repository.findByRole(role);

        if (!userByRole.isEmpty()) {
            List<UserResponseDto> response = userByRole.stream()
                    .map(user -> converter.toDto(user))
                    .toList();

            return new GeneralResponce<>(HttpStatus.OK, response, "Users with role " + role + " successfully found ");
        } else {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "Users with role " + role + " not found ");
        }
    }

    public GeneralResponce<List<UserResponseDto>> getUserByUserName(String userName) {
        List<User> userByName = repository.findByName(userName);

        if (!userByName.isEmpty()) {
            List<UserResponseDto> response = userByName.stream()
                    .map(u -> converter.toDto(u))
                    .toList();

            return new GeneralResponce<>(HttpStatus.OK, response, "Users founded " + userName);
        } else {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "Users not  found ");
        }
    }
}

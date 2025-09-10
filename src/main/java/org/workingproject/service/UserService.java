package org.workingproject.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workingproject.dto.GeneralResponse;
import org.workingproject.dto.UserRequestDto;
import org.workingproject.dto.UserResponseDto;
import org.workingproject.dto.UserUpdateRequestDto;
import org.workingproject.entity.Role;
import org.workingproject.entity.User;
import org.workingproject.repository.UserRepository;
import org.workingproject.service.util.UserConverter;
import org.workingproject.service.validation.UserValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserConverter converter;
    private final UserValidation userValidation;

    public GeneralResponse<UserResponseDto> createUser(UserRequestDto request) {

        List<String> validationErrors = userValidation.validate(request);
        if (!validationErrors.isEmpty()) {
            String errorMessage = "";
            for (String currentError : validationErrors) {
                errorMessage = errorMessage + "\n" + currentError ;
            }
            return new GeneralResponse<>(HttpStatus.BAD_REQUEST, null, errorMessage);
        }

        Optional<User> userByEmailOptional = repository.findByEmail(request.getEmail());
        if(userByEmailOptional.isPresent()) {
            return new GeneralResponse<>(HttpStatus.BAD_REQUEST, null, "Email already exists");
        }

        User user = converter.fromDto(request);
        user.setRole(Role.USER);

        LocalDate today = LocalDate.now();
        user.setCreateDate(today);
        user.setLastUpdate(today);

        User savedUser = repository.save(user);

        UserResponseDto response = converter.toDto(savedUser);
        return new GeneralResponse<>(HttpStatus.CREATED, response, "User created successfully");
    }

    public GeneralResponse<UserResponseDto> updateUser(UserUpdateRequestDto updateRequest) {
        Optional<User> userForUpdateOptional = repository.findById(updateRequest.getId());

        if (userForUpdateOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "User not found");
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
        return new GeneralResponse<>(HttpStatus.OK, converter.toDto(userForUpdate), "User updated successfully");
    }

    public GeneralResponse<List<User>> getAllUSersAdmin() {
        return new GeneralResponse<>(HttpStatus.OK, repository.findAll(), "All Users Admin (Administrator mode)");
    }

    public GeneralResponse<List<UserResponseDto>> getAll() {

        List<UserResponseDto> response = repository.findAll().stream()
                .map(u -> converter.toDto(u))
                .toList();

        return new GeneralResponse<>(HttpStatus.OK, response, "All Users  (User mode)");
    }


    public GeneralResponse<UserResponseDto> getUserById(Integer id) {
        Optional<User> userByIdOptional = repository.findById(id);
        if (userByIdOptional.isPresent()) {
            User userById = userByIdOptional.get();
            UserResponseDto response = converter.toDto(userById);
            return new GeneralResponse<>(HttpStatus.OK, response, "User founded ");
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "User with id " + id + " not found ");
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

    public GeneralResponse<UserResponseDto> getUserByEmail(String email) {
        Optional<User> userByEmailOptional = repository.findByEmail(email);

        if (userByEmailOptional.isPresent()) {
            User userByEmail = userByEmailOptional.get();
            UserResponseDto response = converter.toDto(userByEmail);
            return new GeneralResponse<>(HttpStatus.OK, response, "User founded ");
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "User with this email " + email + " not found ");
        }
    }

    public GeneralResponse<List<UserResponseDto>> getUserByRole(String role) {
        List<User> userByRole = repository.findByRole(role);

        if (!userByRole.isEmpty()) {
            List<UserResponseDto> response = userByRole.stream()
                    .map(user -> converter.toDto(user))
                    .toList();

            return new GeneralResponse<>(HttpStatus.OK, response, "Users with role " + role + " successfully found ");
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Users with role " + role + " not found ");
        }
    }

    public GeneralResponse<List<UserResponseDto>> getUserByUserName(String userName) {
        List<User> userByName = repository.findByName(userName);

        if (!userByName.isEmpty()) {
            List<UserResponseDto> response = userByName.stream()
                    .map(u -> converter.toDto(u))
                    .toList();

            return new GeneralResponse<>(HttpStatus.OK, response, "Users founded " + userName);
        } else {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Users not  found ");
        }
    }
}

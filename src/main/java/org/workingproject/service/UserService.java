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
import org.workingproject.repository.RoleRepository;
import org.workingproject.repository.UserRepository;
import org.workingproject.service.exceptions.AlreadyExistException;
import org.workingproject.service.exceptions.NotFoundException;
import org.workingproject.service.exceptions.ValidationException;
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
    private final RoleRepository roleRepository;

    public UserResponseDto createUser(UserRequestDto request) {
//        makeRequestValidation(request);
        makeAlreadyExistEmail(request.getEmail());

        User user = converter.fromDto(request);
        Role defaultRoleOptional = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("Role not found"));

        user.setRole(defaultRoleOptional);
        LocalDate today = LocalDate.now();
        user.setCreateDate(today);
        user.setLastUpdate(today);

        User savedUser = repository.save(user);

        UserResponseDto response = converter.toDto(savedUser);
        return response;
    }

    private void makeAlreadyExistEmail(String email) {
        Optional<User> userByEmailOptional = repository.findByEmail(email);
        if (userByEmailOptional.isPresent()) {
            throw new AlreadyExistException("Email already exists");
        }
    }

    public UserResponseDto updateUser(UserUpdateRequestDto updateRequest) {
        Optional<User> userForUpdateOptional = repository.findById(updateRequest.getId());

        if (userForUpdateOptional.isEmpty()) {
            throw new NotFoundException("User not found");
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
        return  converter.toDto(userForUpdate);
    }

    public List<User> getAllUSersAdmin() {
        return  repository.findAll();
    }

    public List<UserResponseDto> getAll() {
        return converter.toDtos(repository.findAll());
    }


    public UserResponseDto getUserById(Integer id) {
        return repository.findById(id)
                .map(user -> converter.toDto(user))
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));


    }

    public Optional<User> getUserByIdForPostService(Integer id) {
        Optional<User> userByIdOptional = repository.findById(id);
        if (userByIdOptional.isPresent()) {
            return userByIdOptional;
        } else {
            return Optional.empty();
        }
    }

    public UserResponseDto getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(u -> converter.toDto(u))
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    public List<UserResponseDto> getUserByRole(String role) {
        Role userRole = roleRepository.findByRoleName(role).orElseThrow((() -> new NotFoundException("Role " + role + " not found")));
        List<User> usersByRole = repository.findByRole(userRole);

        return converter.toDtos(usersByRole);
    }

    public List<UserResponseDto> getUserByUserName(String userName) {
        List<User> usersByName = repository.findByUserName(userName);

        return converter.toDtos(usersByName);
    }

    private void makeRequestValidation(UserRequestDto request) {
        List<String> validationErrors = userValidation.validate(request);
        if (!validationErrors.isEmpty()) {
            String errorMessage = "";
            for (String currentError : validationErrors) {
                errorMessage = errorMessage + "\n" + currentError;
            }
            throw new ValidationException(errorMessage);
        }
    }

    public UserResponseDto deleteUserById(Integer id) {
        return repository.findById(id)
                .map(u -> {
                    repository.deleteById(id);
                    return converter.toDto(u);
                })
                .orElseThrow(() -> new NotFoundException("User with id = " + id + " not found"));

    }
}

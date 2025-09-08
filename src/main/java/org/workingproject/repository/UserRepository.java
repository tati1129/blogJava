package org.workingproject.repository;

import org.workingproject.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User updateUser(User user);

    Optional<User> deleteById(Integer id);

    List<User> findAll();

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    List<User> findByRole(String role);

    List<User> findByName(String username);
}

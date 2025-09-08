package org.workingproject.repository;

import org.springframework.stereotype.Repository;
import org.workingproject.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users;
    private Integer userIdCounter;

    public UserRepositoryImpl() {
        this.users = new ArrayList<>();
        this.userIdCounter = 0;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++userIdCounter);
            users.add(user);
        } else {
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
        }
        return user;
    }


    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            return null;
        }
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getId().equals(user.getId())) {
                users.set(i, user);
                u.setLastUpdate(LocalDate.now());
                return user;
            }
        }
        return null;
    }


    @Override
    public Optional<User> deleteById(Integer id) {
        Optional<User> userForDeletOptional = findById(id);
        if (userForDeletOptional.isEmpty()) {
            return Optional.empty();
        } else {
            users.remove(userForDeletOptional.get());
            return userForDeletOptional;
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findByRole(String role) {
        return users.stream()
                .filter(u -> u.getRole().equals(role))
                .toList();
    }

    @Override
    public List<User> findByName(String username) {
        return users.stream()
                .filter(u -> u.getUserName().equals(username))
                .toList();
    }
}

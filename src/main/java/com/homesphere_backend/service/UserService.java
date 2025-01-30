package com.homesphere_backend.service;

import com.homesphere_backend.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(User user);

    void deleteUser(Long id);

    boolean existsByEmail(String email);

    boolean authenticateUser(String email, String rawPassword);

    Optional<User> findByEmail(String email);
}

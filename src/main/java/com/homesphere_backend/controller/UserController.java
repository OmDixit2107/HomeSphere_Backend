package com.homesphere_backend.controller;

import com.homesphere_backend.DTO.LoginRequest;
import com.homesphere_backend.DTO.LoginResponse;
import com.homesphere_backend.entity.User;
import com.homesphere_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Signup Endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use.");
        }
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        boolean authenticated = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (authenticated) {
            Optional<User> user = userService.findByEmail(loginRequest.getEmail());
            if (user.isPresent()) {
                HttpSession session = request.getSession(true); // Create a new session if one doesn't exist
                session.setAttribute("email", loginRequest.getEmail());
                // Return the role as part of the login response
                return ResponseEntity.ok(new LoginResponse(user.get().getRole()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid user"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid credentials"));
    }


    // Logout Endpoint
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        HttpSession session = request.getSession(false); // Get the current session, but don't create a new one
//        if (session != null) {
//            session.invalidate();
//            return ResponseEntity.ok("Logout successful.");
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active session to logout.");
//    }

    // Protected Endpoint
    @GetMapping("/protected-endpoint")
    public ResponseEntity<String> getProtectedData(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get the session without creating a new one
        if (session != null && session.getAttribute("email") != null) {
            String email = (String) session.getAttribute("email");
            return ResponseEntity.ok("Hello, " + email + "! This is protected data.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Please log in.");
    }

    // Get All Users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get User by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/users/by-email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete User by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }
}

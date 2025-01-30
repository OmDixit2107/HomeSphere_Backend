package com.homesphere_backend.DTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponse {
    public String Role;

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}

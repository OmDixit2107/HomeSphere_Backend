package com.homesphere_backend.security;

import com.homesphere_backend.entity.User;
import com.homesphere_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Doesn't exists"));
        // Assign roles/authorities to the user
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Example role

        // Return a UserDetails object with encoded password
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(), // Ensure this is encoded in the database
                authorities
        );
    }
}

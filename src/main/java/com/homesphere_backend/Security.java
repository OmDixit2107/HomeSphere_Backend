package com.homesphere_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()).authorizeHttpRequests(
                (authorize)->{
//                    authorize.requestMatchers("/").permitAll();
                    authorize.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults()).formLogin(Customizer.withDefaults());
        return http.build();
    }
}

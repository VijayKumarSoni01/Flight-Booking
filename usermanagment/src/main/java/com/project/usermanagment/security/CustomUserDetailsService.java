package com.project.usermanagment.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.project.usermanagment.entity.User;
import com.project.usermanagment.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> "ROLE_" + role.name())
                        .toList());
        }
}
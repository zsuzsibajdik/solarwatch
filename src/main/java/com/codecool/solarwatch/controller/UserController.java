package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.dto.user.UserProfileResponse;
import com.codecool.solarwatch.entity.User;
import com.codecool.solarwatch.exception.UserNotFoundException;
import com.codecool.solarwatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public UserProfileResponse getProfile(Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        String[] roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .sorted(Comparator.naturalOrder())
                .toArray(String[]::new);

        return new UserProfileResponse(user.getUsername(), user.getEmail(), roles);
    }
}



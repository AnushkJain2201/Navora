package com.navora.backend.service;

import com.navora.backend.dto.AuthResponseDto;
import com.navora.backend.dto.LoginRequestDto;
import com.navora.backend.dto.RegisterRequestDto;
import com.navora.backend.entity.User;
import com.navora.backend.repository.UserRepository;
import com.navora.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        if(userRepository.existsByEmail(registerRequestDto.email())) {
            throw new IllegalArgumentException("Email Already Registered.");
        }

        String hashPassword = passwordEncoder.encode(registerRequestDto.password());
        User user = new User(registerRequestDto.email(), hashPassword, registerRequestDto.name());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDto(token, user.getEmail(), user.getName());
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email or Password"));

        if (!passwordEncoder.matches(loginRequestDto.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponseDto(token, user.getEmail(), user.getName());
    }
}

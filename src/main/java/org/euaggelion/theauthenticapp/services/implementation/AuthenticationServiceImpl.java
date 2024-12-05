package org.euaggelion.theauthenticapp.services.implementation;
import lombok.RequiredArgsConstructor;
import org.euaggelion.theauthenticapp.authenticate.JwtTokenService;
import org.euaggelion.theauthenticapp.dtos.AuthenticationRequest;
import org.euaggelion.theauthenticapp.dtos.AuthenticationResponse;
import org.euaggelion.theauthenticapp.dtos.RegisterRequest;
import org.euaggelion.theauthenticapp.models.User;
import org.euaggelion.theauthenticapp.repositories.UserRepository;
import org.euaggelion.theauthenticapp.role.Role;
import org.euaggelion.theauthenticapp.services.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    // This map is a temporary in-memory storage for OTPs.
    // Ideally, OTPs should be stored in Redis or another in-memory store with an expiration.
    private final Map<String, String> otpStorage = new HashMap<>();

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered.");
        }

        // Create and save new user
        User newUser = new User();
        newUser.setUsername(request.getEmail());
        newUser.setPhoneNumber(request.getPhone());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.USER);  // Set default role as USER

        userRepository.save(newUser);

        // Generate JWT token
        String jwtToken = jwtTokenService.generateToken(newUser);

        return new AuthenticationResponse(jwtToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token
        String jwtToken = jwtTokenService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }
}


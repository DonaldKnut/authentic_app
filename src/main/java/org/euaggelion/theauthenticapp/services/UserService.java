package org.euaggelion.theauthenticapp.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.euaggelion.theauthenticapp.Exception.InvalidCredentialsException;
import org.euaggelion.theauthenticapp.Exception.UserAlreadyExistsException;
import org.euaggelion.theauthenticapp.authenticate.JwtTokenService;
import org.euaggelion.theauthenticapp.dtos.UserRegistrationDTO;
import org.euaggelion.theauthenticapp.dtos.UserResponseDTO;
import org.euaggelion.theauthenticapp.models.User;
import org.euaggelion.theauthenticapp.repositories.UserRepository;
import org.euaggelion.theauthenticapp.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Register a new user.
     *
     * @param registrationDTO Contains user registration details.
     * @return UserResponseDTO with token and success message.
     */
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        // Check if username or phone number already exists
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        if (userRepository.findByPhoneNumber(registrationDTO.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExistsException("Phone number already exists.");
        }

        // Create and save the user
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPhoneNumber(registrationDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Dynamically assign the role based on input
        try {
            user.setRole(Role.valueOf(registrationDTO.getRole().toUpperCase()));  // Ensure input matches enum format
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role provided. Please use a valid role.");
        }

        // Set user status (e.g., "ACTIVE" or "PENDING" based on role or business logic)
        user.setStatus("ACTIVE");

        // Save user and generate JWT token
        userRepository.save(user);
        String jwtToken = jwtTokenService.generateToken(user);

        // Return response with user id, username, token, and message
        return new UserResponseDTO(user.getId(), user.getUsername(), jwtToken, "Registration successful!");
    }

    /**
     * Authenticate a user.
     *
     * @param username User's username.
     * @param password User's password.
     * @return UserResponseDTO with token and success message.
     */
    public UserResponseDTO authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String jwtToken = jwtTokenService.generateToken(user);

        return new UserResponseDTO(user.getId(), user.getUsername(), jwtToken, "Authentication successful!");
    }

    /**
     * Send a reset password token to the user's email.
     *
     * @param email User's email address.
     */
    public void forgotPassword(String email) {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with the provided email"));

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);  // Store the reset token in the user entity
        userRepository.save(user);

        sendResetPasswordEmail(user.getUsername(), token);
    }

    /**
     * Helper method to send reset password email.
     *
     * @param email Recipient email.
     * @param token Reset token.
     */
    private void sendResetPasswordEmail(String email, String token) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setSubject("Reset Password Request");
            helper.setText("Click the following link to reset your password: " +
                    "http://localhost:8080/api/users/reset-password?token=" + token);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Reset the user's password using the provided token.
     *
     * @param token    Password reset token.
     * @param password New password.
     */
    public void resetPassword(String token, String password) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        user.setPassword(passwordEncoder.encode(password));
        user.setVerificationToken(null);  // Clear the token after successful reset
        userRepository.save(user);
    }
}

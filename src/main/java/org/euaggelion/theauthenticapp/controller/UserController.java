package org.euaggelion.theauthenticapp.controller;

import org.euaggelion.theauthenticapp.Exception.UserAlreadyExistsException;
import org.euaggelion.theauthenticapp.dtos.LoginRequestDTO;
import org.euaggelion.theauthenticapp.dtos.UserRegistrationDTO;
import org.euaggelion.theauthenticapp.dtos.UserResponseDTO;
import org.euaggelion.theauthenticapp.models.User;
import org.euaggelion.theauthenticapp.repositories.UserRepository;
import org.euaggelion.theauthenticapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Register a new user.
     *
     * @param registrationDTO Registration details.
     * @return ResponseEntity containing UserResponseDTO or error message.
     */
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserResponseDTO response = userService.registerUser(registrationDTO);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserResponseDTO(null, registrationDTO.getUsername(), null, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserResponseDTO(null, registrationDTO.getUsername(), null, e.getMessage()));
        }
    }

    /**
     * Authenticate user login.
     *
     * @param loginRequest Contains login credentials.
     * @return ResponseEntity with authentication token or error message.
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            UserResponseDTO response = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UserResponseDTO(null, loginRequest.getUsername(), null, e.getMessage()));
        }
    }

    /**
     * Verify user's email using token.
     *
     * @param token Email verification token.
     * @return ResponseEntity with success or failure message.
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmailVerified(true);
            user.setVerificationToken(null); // Clear the token
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token.");
        }
    }

    /**
     * Handle forgot password request.
     *
     * @param email Email for password reset.
     * @return ResponseEntity with status of email sending.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Reset the user's password using the provided token.
     *
     * @param token    Password reset token.
     * @param password New password.
     * @return ResponseEntity with success or failure message.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        try {
            userService.resetPassword(token, password);
            return ResponseEntity.ok("Password reset successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Get all users.
     *
     * @return ResponseEntity containing a list of users.
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}

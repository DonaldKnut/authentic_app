package org.euaggelion.theauthenticapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserRegistrationDTO {
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "Role cannot be empty")
    private String role;

    // Getters and Setters
}

package org.euaggelion.theauthenticapp.dtos;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String username;
    private String jwtToken;
    private String message;

    public UserResponseDTO(Long id, String username, String jwtToken, String message) {
        this.id = id;
        this.username = username;
        this.jwtToken = jwtToken;
        this.message = message;
    }
}

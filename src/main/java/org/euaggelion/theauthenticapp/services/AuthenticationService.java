package org.euaggelion.theauthenticapp.services;


import org.euaggelion.theauthenticapp.dtos.AuthenticationRequest;
import org.euaggelion.theauthenticapp.dtos.AuthenticationResponse;
import org.euaggelion.theauthenticapp.dtos.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse register(RegisterRequest request);
}

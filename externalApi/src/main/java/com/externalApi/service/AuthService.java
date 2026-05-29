package com.externalApi.service;

import com.externalApi.dto.AuthRequest;
import com.externalApi.dto.AuthResponse;
import com.externalApi.entity.Client;
import com.externalApi.repository.ClientRepository;
import com.externalApi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    //  REGISTER
    public AuthResponse register(AuthRequest request) {
        validateCredentials(request);

        // check if client already exists
        if (clientRepository.findByClientId(String.valueOf(request.getClientId())).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Client already exists with clientId: " + request.getClientId()
            );
        }

        Client client = new Client();
        client.setClientId(request.getClientId());
        client.setSecret(passwordEncoder.encode(request.getSecret()));

        clientRepository.save(client);

        return buildAuthResponse(String.valueOf(client.getClientId()));
    }

    // LOGIN
    public AuthResponse login(AuthRequest request) {
        validateCredentials(request);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getClientId(),
                            request.getSecret()
                    )
            );
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ClientId or secret is invalid"
            );
        }

        return buildAuthResponse(String.valueOf(request.getClientId()));
    }

    // VALIDATION
    private void validateCredentials(AuthRequest request) {
        if (request == null
                || !StringUtils.hasText(String.valueOf(request.getClientId()))
                || !StringUtils.hasText(request.getSecret())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ClientId or secret is invalid"
            );
        }
    }

    //  JWT RESPONSE
    private AuthResponse buildAuthResponse(String clientId) {
        String token = jwtUtil.generateToken(clientId);

        return new AuthResponse(
                token,
                "Bearer",
                jwtUtil.getExpiration(),
                jwtUtil.extractExpiration(token)
        );
    }
}
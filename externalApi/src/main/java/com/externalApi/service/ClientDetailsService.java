package com.externalApi.service;

import com.externalApi.entity.Client;
import com.externalApi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String secret) throws UsernameNotFoundException {
        Client client = clientRepository.findByClientId(secret)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with secret: " + secret));

        return org.springframework.security.core.userdetails.User
                .withUsername(String.valueOf(client.getClientId()))
                .password(client.getSecret())
                .authorities("ROLE_USER")
                .build();
    }
}

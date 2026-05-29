package com.externalApi.service;

import com.externalApi.dto.LoginResponse;
import com.externalApi.dto.PropertyType;
import com.externalApi.dto.PropertyTypeResponse;
import com.externalApi.entity.Room;
import com.externalApi.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class NewApiService {

    @Autowired
    private RoomRepository repository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.auth.url}")
    private String authUrl;

    @Value("${app.supplierId}")
    private String username;

    @Value("${app.supplierSecret}")
    private String password;

    // FETCH PROPERTY TYPES

    public List<PropertyType> fetchProp() {

        String url = "https://api.supplier.roomdb.io/api/v1/room-type";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth(getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<PropertyTypeResponse> response = restTemplate.exchange(
                url, HttpMethod.GET,  entity,  PropertyTypeResponse.class  );

        PropertyTypeResponse body = response.getBody();

        if (body == null || body.getResult() == null) {
            return List.of();
        }

        return body.getResult().stream().filter(p -> p.getName() != null && !p.getName().isBlank()).toList();
    }

    public List<PropertyType> cultfetch() {

        String url = "https://api.supplier.roomdb.io/api/v1/room-type";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth(getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<PropertyTypeResponse> response = restTemplate.exchange(
                url,  HttpMethod.GET, entity,  PropertyTypeResponse.class);

        PropertyTypeResponse body = response.getBody();

        if (body == null || body.getResult() == null ) {
            return List.of();
        }

        return body.getResult()
                .stream()
                .filter(p -> p.getCultSwitchId() != null )
                .toList();
    }

    public List<Room> fetchAndSave() {

        String url = "https://api.supplier.roomdb.io/api/v1/room-type";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PropertyTypeResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PropertyTypeResponse.class
        );

        PropertyTypeResponse body = response.getBody();

        if (body == null || body.getResult() == null) {
            return List.of();
        }

        List<Room> entities = body.getResult()
                .stream()
                // optional cleanup filter
                .filter(p -> p.getCultSwitchId() != null)
                .map(this::mapToEntity)
                .toList();

        return repository.saveAll(entities);
    }

        // =========================
        // GET TOKEN USING QUERY PARAMS (NO BODY)
        // =========================


    public String getToken() {

        String url = authUrl
                + "?supplierId=" + username
                + "&supplierSecret=" + password;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<LoginResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                LoginResponse.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("Token API returned null response");
        }

        return response.getBody().getAccessToken();
    }

    private Room mapToEntity(PropertyType dto) {
        Room entity = new Room();
        entity.setCultSwitchId(dto.getCultSwitchId());
        entity.setName(dto.getName());
        entity.setNotes(dto.getNotes());
        return entity;
    }
}
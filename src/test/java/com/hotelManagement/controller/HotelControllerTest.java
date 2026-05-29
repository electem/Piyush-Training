package com.hotelManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelManagement.config.SecurityConfig;
import com.hotelManagement.dto.CreateHotelRequest;
import com.hotelManagement.dto.HotelResponse;
import com.hotelManagement.entity.Hotel;
import com.hotelManagement.exception.GlobalExceptionHandler;
import com.hotelManagement.security.JwtAuthenticationFilter;
import com.hotelManagement.security.JwtService;
import com.hotelManagement.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class, JwtAuthenticationFilter.class})
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelService hotelService;

    @MockBean
    private JwtService jwtService;

    @Test
    void shouldReturnCreatedWhenAdminCreatesHotel() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Grand Palace");
        hotel.setLocation("Mumbai");
        hotel.setDescription("Luxury business hotel");
        hotel.setRating(4.6);
        hotel.setCreatedAt(LocalDateTime.parse("2026-05-23T13:00:00"));

        when(hotelService.createHotel(any())).thenReturn(hotel);

        CreateHotelRequest request = new CreateHotelRequest(
                "Grand Palace",
                "Mumbai",
                "Luxury business hotel",
                4.6
        );

        mockMvc.perform(post("/api/hotels")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Grand Palace"))
                .andExpect(jsonPath("$.rating").value(4.6));
    }

    @Test
    void shouldReturnOkWhenUserReadsHotels() throws Exception {
        HotelResponse hotelResponse = new HotelResponse(
                1L,
                "Grand Palace",
                "Mumbai",
                "Luxury business hotel",
                4.6,
                LocalDateTime.parse("2026-05-23T13:00:00"),
                List.of(),
                0,
                List.of()
        );

        when(hotelService.getAllHotels()).thenReturn(List.of(hotelResponse));

        mockMvc.perform(get("/api/hotels")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Grand Palace"));
    }

    @Test
    void shouldReturnForbiddenWhenUserTriesToCreateHotel() throws Exception {
        CreateHotelRequest request = new CreateHotelRequest(
                "Grand Palace",
                "Mumbai",
                "Luxury business hotel",
                4.6
        );

        mockMvc.perform(post("/api/hotels")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        CreateHotelRequest request = new CreateHotelRequest(
                "",
                "",
                "",
                6.0
        );

        mockMvc.perform(post("/api/hotels")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.location").exists())
                .andExpect(jsonPath("$.validationErrors.description").exists())
                .andExpect(jsonPath("$.validationErrors.rating").exists());
    }
}

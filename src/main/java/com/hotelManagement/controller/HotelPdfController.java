package com.hotelManagement.controller;

import com.hotelManagement.service.HotelPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelPdfController {

    private final HotelPdfService hotelPdfService;

    @GetMapping("/{hotelId}/pdf")
    public ResponseEntity<byte[]> generateHotelPdf(@PathVariable Long hotelId) {

        byte[] pdf = hotelPdfService.generateHotelPdf(hotelId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("hotel-" + hotelId + ".pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
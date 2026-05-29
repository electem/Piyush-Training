package com.hotelManagement.service;

import com.hotelManagement.entity.Hotel;
import com.hotelManagement.repository.HotelImageRepository;
import com.hotelManagement.repository.HotelRepository;
import com.hotelManagement.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class HotelPdfService {

    private final HotelRepository hotelRepository;
    private final HotelImageRepository hotelImageRepository;
    private final RoomRepository roomRepository;
    private final TemplateEngine templateEngine;
    private final PdfGenerator pdfGenerator;

    @Transactional
    public byte[] generateHotelPdf(Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        hotel.setImages(hotelImageRepository.findByHotelId(hotelId));
        hotel.setRooms(roomRepository.findByHotelId(hotelId));

        Context context = new Context();
        context.setVariable("hotel", hotel);

        String html = templateEngine.process("hotelPdf", context);

        return pdfGenerator.generate(html);
    }
}

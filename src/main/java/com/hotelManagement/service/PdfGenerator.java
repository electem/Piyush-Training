package com.hotelManagement.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfGenerator {

    public byte[] generate(String html) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, "http://localhost:8080/");
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}
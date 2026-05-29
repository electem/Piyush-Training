package com.example.CrudOperation.service;

import com.example.CrudOperation.entity.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

@Service
public class PdfService {

    public byte[] generateUserPdf(List<User> users) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Title
            document.add(new Paragraph("User Report"));
            document.add(new Paragraph("----------------------------"));

            // Loop DB data
            for (User user : users) {
                document.add(new Paragraph(
                        "ID: " + user.getId() +
                                " | Name: " + user.getName() +
                                " | Email: " + user.getEmail()
                ));
            }

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return out.toByteArray();
    }

    public String generateAndSavePdf(List<User> users, String folderPath) {

        try {
            // Create folder if not exists
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // File name
            String fileName = "users_" + System.currentTimeMillis() + ".pdf";
            String filePath = folderPath + File.separator + fileName;

            // Create PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Title
            document.add(new Paragraph("User Report"));
            document.add(new Paragraph("----------------------------"));

            // Data
            for (User user : users) {
                document.add(new Paragraph(
                        "ID: " + user.getId() +
                                " | Name: " + user.getName() +
                                " | Email: " + user.getEmail()
                ));
            }

            document.close();

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("Error saving PDF", e);
        }
    }


}

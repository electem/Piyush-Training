package com.example.CrudOperation.service;

import com.example.CrudOperation.entity.Room;
import com.example.CrudOperation.repository.RoomRepository;
import com.opencsv.CSVReader;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

@Service
public class CSVService {
    private static final DateTimeFormatter LAST_MODIFIED_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private RoomRepository roomRepository;

    public void importFromUrl(String fileUrl) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileUrl).openStream())) {
            inputStream.mark(4);
            byte[] signature = inputStream.readNBytes(4);
            inputStream.reset();

            String normalizedUrl = fileUrl.toLowerCase(Locale.ROOT);

            if (isZip(signature)) {
                importFromZipStream(inputStream);
                return;
            }

            if (isGzip(signature) || normalizedUrl.endsWith(".gz")) {
                try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
                    importCsvStream(gzipInputStream);
                }
                return;
            }

            importCsvStream(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("CSV import failed", e);
        }
    }

    private void importFromZipStream(InputStream inputStream) throws Exception {
        try (ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream)) {
            ZipArchiveEntry entry;
            boolean csvFound = false;

            while ((entry = zipInputStream.getNextZipEntry()) != null) {
                if (entry.isDirectory() || !entry.getName().toLowerCase(Locale.ROOT).endsWith(".csv")) {
                    continue;
                }

                csvFound = true;
                importCsvStream(zipInputStream);
            }

            if (!csvFound) {
                throw new IllegalArgumentException("ZIP archive does not contain any CSV file");
            }
        }
    }

    private void importCsvStream(InputStream inputStream) throws Exception {
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CSVReader csvReader = new CSVReader(reader);

        List<Room> rooms = new ArrayList<>();
        String[] row;
        boolean isHeader = true;

        while ((row = csvReader.readNext()) != null) {
            if (isHeader) {
                isHeader = false;
                continue;
            }

            if (row.length < 19) {
                continue;
            }

            Room room = new Room();
            room.setRoomId(parseLongValue(row[0]));
            room.setCountryCode(cleanValue(row[1]));
            room.setStateCode(cleanValue(row[2]));
            room.setStateName(cleanValue(row[3]));
            room.setCity(cleanValue(row[4]));
            room.setPostalCode(cleanValue(row[5]));
            room.setStreet(cleanValue(row[6]));
            room.setHotelName(cleanValue(row[7]));
            room.setLatitude(parseDoubleValue(row[8]));
            room.setLongitude(parseDoubleValue(row[9]));
            room.setCityId(cleanValue(row[10]));
            room.setBrandCode(cleanValue(row[11]));
            room.setBrandName(cleanValue(row[12]));
            room.setStars(parseIntegerValue(row[13]));
            room.setReviewRank(parseDoubleValue(row[14]));
            room.setReviewCount(parseIntegerValue(row[15]));
            room.setLastModified(parseLastModified(row[16]));
            room.setPhoneNumber(cleanValue(row[17]));
            room.setEmail(cleanValue(row[18]));

            rooms.add(room);
        }

        if (!rooms.isEmpty()) {
            roomRepository.saveAll(rooms);
        }
    }

    private boolean isZip(byte[] signature) {
        return signature.length >= 4
                && signature[0] == 'P'
                && signature[1] == 'K'
                && signature[2] == 3
                && signature[3] == 4;
    }

    private boolean isGzip(byte[] signature) {
        return signature.length >= 2
                && (signature[0] & 0xFF) == 0x1F
                && (signature[1] & 0xFF) == 0x8B;
    }

    private String cleanValue(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Long parseLongValue(String value) {
        String cleaned = cleanValue(value);
        return cleaned == null ? null : Long.parseLong(cleaned);
    }

    private Integer parseIntegerValue(String value) {
        String cleaned = cleanValue(value);
        return cleaned == null ? null : Integer.parseInt(cleaned);
    }

    private Double parseDoubleValue(String value) {
        String cleaned = cleanValue(value);
        return cleaned == null ? null : Double.parseDouble(cleaned);
    }

    private LocalDateTime parseLastModified(String value) {
        String cleaned = cleanValue(value);
        return cleaned == null ? null : LocalDateTime.of(
                java.time.LocalDate.parse(cleaned, LAST_MODIFIED_FORMAT),
                java.time.LocalTime.MIN
        );
    }
}

package com.example.CrudOperation.service;

import com.example.CrudOperation.entity.Room;
import com.example.CrudOperation.repository.RoomRepository;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Service
public class CSVService {

    private final RoomRepository roomRepository;

    public CSVService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void importFromUrl(String fileUrl) {

        try (InputStream raw = new BufferedInputStream(new URL(fileUrl).openStream())) {

            raw.mark(2);
            byte[] signature = raw.readNBytes(2);
            raw.reset();

            InputStream finalStream;

            if (isGzip(signature) || fileUrl.endsWith(".gz")) {
                finalStream = new GZIPInputStream(raw);
            } else {
                finalStream = raw;
            }

            importCsv(finalStream);

        } catch (Exception e) {
            throw new RuntimeException("CSV import failed", e);
        }
    }

    private void importCsv(InputStream inputStream) throws Exception {

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String[] row;
            boolean header = true;

            List<Room> batch = new ArrayList<>();
            int batchSize = 500;

            int count = 0;
            int maxRecords = 20;
            while ((row = reader.readNext()) != null) {

                if (header) {
                    header = false;
                    continue;
                }

                if (count >= maxRecords) {
                    break;
                }

                if (row.length < 19) continue;

                Room room = new Room();

                room.setRoomId(parseLong(row[0]));
                room.setCountryCode(clean(row[1]));
                room.setStateCode(clean(row[2]));
                room.setStateName(clean(row[3]));
                room.setCity(clean(row[4]));
                room.setPostalCode(clean(row[5]));
                room.setStreet(clean(row[6]));
                room.setHotelName(clean(row[7]));
                room.setLatitude(parseDouble(row[8]));
                room.setLongitude(parseDouble(row[9]));
                room.setCityId(clean(row[10]));
                room.setBrandCode(clean(row[11]));
                room.setBrandName(clean(row[12]));
                room.setStars(parseInt(row[13]));
                room.setReviewRank(parseDouble(row[14]));
                room.setReviewCount(parseInt(row[15]));
                room.setLastModified(parseDate(row[16]));
                room.setPhoneNumber(clean(row[17]));
                room.setEmail(clean(row[18]));

                batch.add(room);
                count++;

                if (batch.size() == batchSize) {
                    roomRepository.saveAll(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                roomRepository.saveAll(batch);
            }
        }
    }

    private boolean isGzip(byte[] sig) {
        return sig.length >= 2 && (sig[0] & 0xff) == 0x1f && (sig[1] & 0xff) == 0x8b;
    }

    private String clean(String v) {
        return (v == null || v.trim().isEmpty()) ? null : v.trim();
    }

    private Long parseLong(String v) {
        try {
            return Long.parseLong(clean(v));
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInt(String v) {
        try { return Integer.parseInt(clean(v)); } catch (Exception e) { return null; }
    }

    private Double parseDouble(String v) {
        try { return Double.parseDouble(clean(v)); } catch (Exception e) { return null; }
    }

    private LocalDateTime parseDate(String v) {
        try {
            return LocalDateTime.of(
                    LocalDate.parse(clean(v), DATE_FORMAT),
                    LocalTime.MIDNIGHT
            );
        } catch (Exception e) {
            return null;
        }
    }
}

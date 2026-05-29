package com.hotelManagement.repository;

import com.hotelManagement.dto.HotelDetailsProjection;
import com.hotelManagement.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query(value = """
    SELECT
        h.id,
        h.name,
        h.location,
        h.description,
        h.rating,

        COALESCE(
            json_agg(
                DISTINCT jsonb_build_object(
                    'id', r.id,
                    'roomNumber', r.room_number,
                    'roomType', r.room_type,
                    'price', r.price,
                    'available', r.available,
                    'images', COALESCE((
                        SELECT json_agg(
                            jsonb_build_object(
                                'id', ri.id,
                                'imageUrl', ri.image_url,
                                'imageName', ri.image_name
                            )
                            ORDER BY ri.id
                        )
                        FROM room_images ri
                        WHERE ri.room_id = r.id
                    ), '[]')
                )
            ) FILTER (WHERE r.id IS NOT NULL),
            '[]'
        ) AS rooms,

        COALESCE(
            json_agg(
                DISTINCT jsonb_build_object(
                    'id', i.id,
                    'imageUrl', i.image_url,
                    'imageName', i.image_name
                )
            ) FILTER (WHERE i.id IS NOT NULL),
            '[]'
        ) AS images

    FROM hotels h

    LEFT JOIN rooms r
           ON r.hotel_id = h.id

    LEFT JOIN hotel_images i
           ON i.hotel_id = h.id

    GROUP BY h.id

    ORDER BY h.id DESC
    """, nativeQuery = true)
    List<HotelDetailsProjection> getAllHotelsData();



    @Query(value = """
        SELECT 
            h.id,
            h.name,
            h.location,
            h.description,
            h.rating,
            h.created_at,

        COALESCE(
            json_agg(
                DISTINCT jsonb_build_object(
                    'id', r.id,
                    'roomNumber', r.room_number,
                    'roomType', r.room_type,
                    'price', r.price,
                    'available', r.available,
                    'images', COALESCE((
                        SELECT json_agg(
                            jsonb_build_object(
                                'id', ri.id,
                                'imageUrl', ri.image_url,
                                'imageName', ri.image_name
                            )
                            ORDER BY ri.id
                        )
                        FROM room_images ri
                        WHERE ri.room_id = r.id
                    ), '[]')
                )
            ) FILTER (WHERE r.id IS NOT NULL),
            '[]'
        ) AS rooms,

            COALESCE(
                json_agg(
                    DISTINCT jsonb_build_object(
                        'id', i.id,
                        'imageUrl', i.image_url,
                        'imageName', i.image_name
                    )
                ) FILTER (WHERE i.id IS NOT NULL),
                '[]'
            ) AS images

        FROM hotels h

        LEFT JOIN rooms r
               ON r.hotel_id = h.id

        LEFT JOIN hotel_images i
               ON i.hotel_id = h.id

        WHERE h.id = :hotelId

        GROUP BY h.id
        """, nativeQuery = true)
    HotelDetailsProjection getHotelDetails(@Param("hotelId") Long hotelId);
}

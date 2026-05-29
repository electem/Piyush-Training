package com.hotelManagement.repository;

import com.hotelManagement.entity.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {
    Set<HotelImage> findByHotelId(Long hotelId);
    Optional<HotelImage> findByIdAndHotelId(Long id, Long hotelId);
    List<HotelImage> findByHotelIdAndIdIn(Long hotelId, List<Long> imageIds);
}

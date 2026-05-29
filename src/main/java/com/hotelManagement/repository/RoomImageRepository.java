package com.hotelManagement.repository;

import com.hotelManagement.entity.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
    List<RoomImage> findByRoomId(Long roomId);
    Optional<RoomImage> findByIdAndRoomId(Long id, Long roomId);
    List<RoomImage> findByRoomIdAndIdIn(Long roomId, List<Long> imageIds);
}

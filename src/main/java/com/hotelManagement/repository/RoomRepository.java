package com.hotelManagement.repository;

import com.hotelManagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RoomRepository extends JpaRepository<Room,Long> {
    List<Room> findByHotelId(Long hotelId);
}

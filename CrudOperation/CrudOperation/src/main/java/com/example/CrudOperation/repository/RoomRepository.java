package com.example.CrudOperation.repository;

import com.example.CrudOperation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}

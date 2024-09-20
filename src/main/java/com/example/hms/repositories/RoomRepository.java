package com.example.hms.repositories;

import com.example.hms.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    Optional<Room> findByIdAndIsDeletedFalse(Long id);
    @Query("SELECT COUNT(r) FROM Room r WHERE r.isDeleted = false")
    int getTotalRooms();

    @Query("SELECT COUNT(r) FROM Room r WHERE r.status = 'AVAILABLE' AND r.isDeleted = false")
    int getAvailableRooms();
}

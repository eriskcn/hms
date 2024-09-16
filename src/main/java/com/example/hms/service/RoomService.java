package com.example.hms.service;

import com.example.hms.dto.room.RoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    Page<RoomDTO> getAllRooms(String search, String filterCriteria, Pageable pageable);

    RoomDTO getRoomById(Long id);

    RoomDTO createRoom(RoomDTO roomDTO);

    RoomDTO updateRoom(Long id, RoomDTO roomDTO);

    void deleteRoom(Long id);
}

package com.example.hms.service;

import com.example.hms.dto.room.RoomCreateDTO;
import com.example.hms.dto.room.RoomDTO;
import com.example.hms.dto.room.RoomUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    Page<RoomDTO> getAllRooms(String search, String filterCriteria, Pageable pageable);

    RoomDTO getRoomById(Long id);

    RoomDTO createRoom(RoomCreateDTO roomCreateDTO);

    RoomDTO updateRoom(Long id, RoomUpdateDTO roomUpdateDTO);

    void deleteRoom(Long id);
}

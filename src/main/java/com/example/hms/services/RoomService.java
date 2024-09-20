package com.example.hms.services;

import com.example.hms.dtos.room.RoomAvailableDTO;
import com.example.hms.dtos.room.RoomCreateDTO;
import com.example.hms.dtos.room.RoomDTO;
import com.example.hms.dtos.room.RoomUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    Page<RoomDTO> getAllRooms(
            String search,
            String filterCriteria,
            Pageable pageable
    );

    RoomDTO getRoomById(Long id);

    RoomDTO createRoom(RoomCreateDTO roomCreateDTO);

    RoomDTO updateRoom(Long id, RoomUpdateDTO roomUpdateDTO);

    void deleteRoom(Long id);

    List<RoomAvailableDTO> getAvailableRoomsByType(String type);
}

package com.example.hms.service;

import com.example.hms.dto.RoomDTO;

import java.util.List;

public interface RoomService {
    RoomDTO createRoom(RoomDTO roomDTO);
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Long id);
    RoomDTO updateRoom(Long id, RoomDTO roomDTO);
    void deleteRoom(Long id);
}

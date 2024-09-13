package com.example.hms.service.implementation;

import com.example.hms.dto.RoomDTO;
import com.example.hms.entity.Room;
import com.example.hms.repository.RoomRepository;
import com.example.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImplementation implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room room = mapToEntity(roomDTO);
        room = roomRepository.save(room);
        return mapToDTO(room);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAllByIsDeletedFalse();
        return rooms.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + id)
        );
        return mapToDTO(room);
    }

    @Override
    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + id)
        );
        room.setNumber(roomDTO.getNumber());
        room.setType(roomDTO.getType());
        room.setPrice(roomDTO.getPrice());
        room.setStatus(roomDTO.getStatus());
        return mapToDTO(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + id)
        );
        room.setIsDeleted(true);
        roomRepository.save(room);
    }

    private RoomDTO mapToDTO(Room room) {
        return new RoomDTO(room.getId(), room.getNumber(), room.getType(), room.getPrice(), room.getStatus());
    }

    private Room mapToEntity(RoomDTO roomDTO) {
        Room room = new Room();
        room.setNumber(roomDTO.getNumber());
        room.setType(roomDTO.getType());
        room.setPrice(roomDTO.getPrice());
        room.setStatus(roomDTO.getStatus());
        return room;
    }
}

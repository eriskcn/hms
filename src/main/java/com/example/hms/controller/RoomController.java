package com.example.hms.controller;

import com.example.hms.dto.RoomDTO;
import com.example.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hms/rooms")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<Page<RoomDTO>> getAllRooms(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterCriteria,
            Pageable pageable
    ) {
        return ResponseEntity.ok(roomService.getAllRooms(search, filterCriteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable("id") Long id) {
        RoomDTO roomDTO = roomService.getRoomById(id);
        return ResponseEntity.ok(roomDTO);
    }

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO roomDTO) {
        RoomDTO newRoom = roomService.createRoom(roomDTO);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable("id") Long id, @RequestBody RoomDTO roomDTO) {
        RoomDTO updatedRoom = roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}

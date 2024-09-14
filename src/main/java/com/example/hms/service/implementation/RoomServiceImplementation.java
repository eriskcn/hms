package com.example.hms.service.implementation;

import com.example.hms.dto.RoomDTO;
import com.example.hms.entity.Room;
import com.example.hms.repository.RoomRepository;
import com.example.hms.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hms.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;


@Service
public class RoomServiceImplementation implements RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImplementation(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room room = mapToEntity(roomDTO);
        room = roomRepository.save(room);
        return mapToDTO(room);
    }

    @Override
    public Page<RoomDTO> getAllRooms(String search, String filterCriteria, Pageable pageable) {
        Specification<Room> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search functionality
            if (search != null && !search.isEmpty()) {
                String searchLower = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(root.get("number"), searchLower));
            }

            // Filter functionality
            if (filterCriteria != null && !filterCriteria.isEmpty()) {
                switch (filterCriteria.toLowerCase()) {
                    case "standard":
                        predicates.add(criteriaBuilder.equal(root.get("standard"), filterCriteria));
                        break;
                    case "suite":
                        predicates.add(criteriaBuilder.equal(root.get("suite"), filterCriteria));
                        break;
                    case "vip":
                        predicates.add(criteriaBuilder.equal(root.get("vip"), filterCriteria));
                        break;
                    default:
                        break;
                }
            }

            // Only return non-deleted rooms
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Room> roomsPage = roomRepository.findAll(spec, pageable);
        return roomsPage.map(this::mapToDTO);
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

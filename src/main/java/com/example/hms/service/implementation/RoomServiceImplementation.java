package com.example.hms.service.implementation;

import com.example.hms.dto.room.RoomAvailableDTO;
import com.example.hms.dto.room.RoomCreateDTO;
import com.example.hms.dto.room.RoomDTO;
import com.example.hms.dto.room.RoomUpdateDTO;
import com.example.hms.entity.Room;
import com.example.hms.entity.enumdef.Status;
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
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RoomServiceImplementation implements RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImplementation(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Page<RoomDTO> getAllRooms(
            String search,
            String filterCriteria,
            Pageable pageable
    ) {
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
                        predicates.add(criteriaBuilder.equal(root.get("type"), "standard"));
                        break;
                    case "suite":
                        predicates.add(criteriaBuilder.equal(root.get("type"), "suite"));
                        break;
                    case "vip":
                        predicates.add(criteriaBuilder.equal(root.get("type"), "vip"));
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
        Room room = roomRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + id)
        );
        return mapToDTO(room);
    }

    @Override
    public List<RoomAvailableDTO> getAvailableRoomsByType(String type) {
        Specification<Room> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"), Status.AVAILABLE));
            if (type != null && !type.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type.toLowerCase()));
            }

            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Room> rooms = roomRepository.findAll(spec);
        return rooms.stream().map(this::mapToAvailableDTO).collect(Collectors.toList());
    }

    private RoomAvailableDTO mapToAvailableDTO(Room room) {
        return new RoomAvailableDTO(room.getId(), room.getNumber());
    }

    @Override
    @Transactional
    public RoomDTO createRoom(RoomCreateDTO roomCreateDTO) {
        Room room = mapToEntity(roomCreateDTO);
        room = roomRepository.save(room);
        return mapToDTO(room);
    }

    @Override
    @Transactional
    public RoomDTO updateRoom(Long id, RoomUpdateDTO roomUpdateDTO) {
        Room room = roomRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + id)
        );
        if (roomUpdateDTO.getNumber() != null) {
            room.setNumber(roomUpdateDTO.getNumber());
        }
        if (roomUpdateDTO.getType() != null) {
            room.setType(roomUpdateDTO.getType());
        }
        if (roomUpdateDTO.getPrice() != null) {
            room.setPrice(roomUpdateDTO.getPrice());
        }
        if (roomUpdateDTO.getStatus() != null) {
            room.setStatus(roomUpdateDTO.getStatus());
        }
        return mapToDTO(roomRepository.save(room));
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + id)
        );
        room.setIsDeleted(true);
        roomRepository.save(room);
    }

    private RoomDTO mapToDTO(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getNumber(),
                room.getType(),
                room.getPrice(),
                room.getStatus()
        );
    }

    private Room mapToEntity(RoomCreateDTO roomCreateDTO) {
        Room room = new Room();
        room.setNumber(roomCreateDTO.getNumber());
        room.setType(roomCreateDTO.getType());
        room.setPrice(roomCreateDTO.getPrice());
        return room;
    }
}

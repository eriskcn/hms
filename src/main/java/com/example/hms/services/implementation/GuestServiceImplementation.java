package com.example.hms.services.implementation;

import com.example.hms.dtos.booking.BookingInnerDTO;
import com.example.hms.dtos.guest.GuestCreateDTO;
import com.example.hms.dtos.guest.GuestDTO;
import com.example.hms.dtos.guest.GuestDetailsDTO;
import com.example.hms.dtos.guest.GuestUpdateDTO;
import com.example.hms.dtos.room.RoomInnerDTO;
import com.example.hms.entities.Booking;
import com.example.hms.entities.Guest;
import com.example.hms.entities.Room;
import com.example.hms.exceptions.ResourceNotFoundException;
import com.example.hms.repositories.BookingRepository;
import com.example.hms.repositories.GuestRepository;
import com.example.hms.services.GuestService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImplementation implements GuestService {
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public GuestServiceImplementation(GuestRepository guestRepository, BookingRepository bookingRepository) {
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Page<GuestDTO> getAllGuests(
            String search,
            String filterCriteria,
            Pageable pageable
    ) {
        Specification<Guest> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search functionality
            if (search != null && !search.isEmpty()) {
                String searchLower = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("idCard")), searchLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchLower)
                ));
            }

            // Filter functionality
            if (filterCriteria != null && !filterCriteria.isEmpty()) {
                switch (filterCriteria.toLowerCase()) {
                    case "vip":
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), new BigDecimal(1000)));
                        break;
                    case "normal":
                        predicates.add(criteriaBuilder.lessThan(root.get("totalAmount"), new BigDecimal(1000)));
                        break;
                    default:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), BigDecimal.ZERO));
                        break;
                }
            }

            // Only return non-deleted guests
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Guest> guestsPage = guestRepository.findAll(spec, pageable);
        return guestsPage.map(this::mapToDTO);
    }

    @Override
    public GuestDetailsDTO getGuestById(Long id) {
        Guest guest = guestRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + id)
        );
        return mapToDetailsDTO(guest);
    }

    @Override
    @Transactional
    public GuestDTO createGuest(GuestCreateDTO guestCreateDTO) {
        Guest guest = mapToEntity(guestCreateDTO);
        guest = guestRepository.save(guest);
        return mapToDTO(guest);
    }

    @Override
    public GuestDTO updateGuest(Long id, GuestUpdateDTO guestUpdateDTO) {
        Guest guest = guestRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + id)
        );
        if (guestUpdateDTO.getName() != null) {
            guest.setName(guestUpdateDTO.getName());
        }
        if (guestUpdateDTO.getIdCard() != null) {
            guest.setIdCard(guestUpdateDTO.getIdCard());
        }
        if (guestUpdateDTO.getGender() != null) {
            guest.setGender(guestUpdateDTO.getGender());
        }
        if (guestUpdateDTO.getPhone() != null) {
            guest.setPhone(guestUpdateDTO.getPhone());
        }
        return mapToDTO(guestRepository.save(guest));
    }

    @Override
    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + id)
        );
        guest.setIsDeleted(true);
        guestRepository.save(guest);
    }

    private GuestDTO mapToDTO(Guest guest) {
        return new GuestDTO(
                guest.getId(),
                guest.getName(),
                guest.getIdCard(),
                guest.getGender(),
                guest.getPhone(),
                guest.getTotalAmount()
        );
    }

    private Guest mapToEntity(GuestCreateDTO guestCreateDTO) {
        Guest guest = new Guest();
        guest.setName(guestCreateDTO.getName());
        guest.setIdCard(guestCreateDTO.getIdCard());
        guest.setGender(guestCreateDTO.getGender());
        guest.setPhone(guestCreateDTO.getPhone());
        return guest;
    }

    private GuestDetailsDTO mapToDetailsDTO(Guest guest) {
        GuestDetailsDTO guestDetailsDTO = new GuestDetailsDTO();
        guestDetailsDTO.setId(guest.getId());
        guestDetailsDTO.setName(guest.getName());
        guestDetailsDTO.setIdCard(guest.getIdCard());
        guestDetailsDTO.setGender(guest.getGender());
        guestDetailsDTO.setPhone(guest.getPhone());
        guestDetailsDTO.setTotalAmount(guest.getTotalAmount());

        List<Booking> bookings = bookingRepository.findByGuestIdAndIsDeletedFalse(guest.getId());
        List<BookingInnerDTO> bookingInnerDTOS = bookings.stream()
                .map(this::mapToInnerDTO).collect(Collectors.toList());
        guestDetailsDTO.setBookings(bookingInnerDTOS);
        return guestDetailsDTO;
    }

    private BookingInnerDTO mapToInnerDTO(Booking booking) {
        BookingInnerDTO bookingInnerDTO = new BookingInnerDTO();
        bookingInnerDTO.setId(booking.getId());
        bookingInnerDTO.setRoom(mapToInnerDTO(booking.getRoom()));
        bookingInnerDTO.setCheckIn(booking.getCheckIn());
        bookingInnerDTO.setCheckOut(booking.getCheckOut());
        bookingInnerDTO.setIsPreBooking(booking.getIsPreBooking());
        bookingInnerDTO.setAmount(booking.getAmount());
        return bookingInnerDTO;
    }

    private RoomInnerDTO mapToInnerDTO(Room room) {
        RoomInnerDTO roomInnerDTO = new RoomInnerDTO();
        roomInnerDTO.setId(room.getId());
        roomInnerDTO.setNumber(room.getNumber());
        roomInnerDTO.setType(room.getType());
        roomInnerDTO.setPrice(room.getPrice());
        return roomInnerDTO;
    }
}

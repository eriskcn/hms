package com.example.hms.service.implementation;

import com.example.hms.dto.BookingDTO;
import com.example.hms.dto.BookingPresentationDTO;
import com.example.hms.entity.Booking;
import com.example.hms.entity.Guest;
import com.example.hms.entity.Room;
import com.example.hms.repository.BookingRepository;
import com.example.hms.repository.GuestRepository;
import com.example.hms.repository.RoomRepository;
import com.example.hms.service.BookingService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hms.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImplementation implements BookingService {
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingServiceImplementation(BookingRepository bookingRepository, GuestRepository guestRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Page<BookingPresentationDTO> getAllBookings(String search, String filterCriteria, Pageable pageable) {
        Specification<Booking> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search functionality
            if (search != null && !search.isEmpty()) {
                Predicate guestNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("guest").get("name")),
                        "%" + search.toLowerCase() + "%"
                );
                Predicate roomNumberPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("room").get("number")),
                        "%" + search.toLowerCase() + "%"
                );
                predicates.add(criteriaBuilder.or(guestNamePredicate, roomNumberPredicate));
            }

            // Filter functionality
            if (filterCriteria != null && !filterCriteria.isEmpty()) {
                switch (filterCriteria.toLowerCase()) {
                    case "is_pre_booking":
                        predicates.add(criteriaBuilder.equal(root.get("isPreBooking"), true));
                        break;
                    case "checked-out":
                        predicates.add(criteriaBuilder.equal(root.get("isPreBooking"), false));
                        break;
                    default:
                        break;
                }
            }

            // Only return non-deleted bookings
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Booking> bookingPage = bookingRepository.findAll(spec, pageable);
        return bookingPage.map(this::mapToPresentationDTO);
    }

    @Override
    public BookingPresentationDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );
        return mapToPresentationDTO(booking);
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = mapToEntity(bookingDTO);
        return mapToDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );

        Guest guest = guestRepository.findById(bookingDTO.getGuestId()).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + bookingDTO.getGuestId())
        );
        Room room = roomRepository.findById(bookingDTO.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + bookingDTO.getRoomId())
        );
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckIn(bookingDTO.getCheckIn());
        booking.setCheckOut(bookingDTO.getCheckOut());
        booking.setIsPreBooking(bookingDTO.getIsPreBooking());
        booking.setAmount(bookingDTO.getAmount());
        return mapToDTO(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );
        booking.setIsDeleted(true);
        bookingRepository.save(booking);
    }

    private BookingDTO mapToDTO(Booking booking) {
        return new BookingDTO(booking.getId(), booking.getGuest().getId(), booking.getRoom().getId(), booking.getCheckIn(), booking.getCheckOut(), booking.getIsPreBooking(), booking.getAmount());
    }

    private Booking mapToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        Guest guest = guestRepository.findById(bookingDTO.getGuestId()).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + bookingDTO.getGuestId())
        );
        Room room = roomRepository.findById(bookingDTO.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + bookingDTO.getRoomId())
        );
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckIn(bookingDTO.getCheckIn());
        booking.setCheckOut(bookingDTO.getCheckOut());
        booking.setIsPreBooking(bookingDTO.getIsPreBooking());
        booking.setAmount(bookingDTO.getAmount());
        return booking;
    }

    private BookingPresentationDTO mapToPresentationDTO(Booking booking) {
        return new BookingPresentationDTO(booking.getId(), booking.getGuest(), booking.getRoom(), booking.getCheckIn(), booking.getCheckOut(), booking.getIsPreBooking(), booking.getAmount());
    }

    private Booking mapToEntity(BookingPresentationDTO bookingPresentationDTO) {
        Booking booking = new Booking();
        booking.setGuest(bookingPresentationDTO.getGuest());
        booking.setRoom(bookingPresentationDTO.getRoom());
        booking.setCheckIn(bookingPresentationDTO.getCheckIn());
        booking.setCheckOut(bookingPresentationDTO.getCheckOut());
        booking.setIsPreBooking(bookingPresentationDTO.getIsPreBooking());
        booking.setAmount(bookingPresentationDTO.getAmount());
        return booking;
    }
}

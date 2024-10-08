package com.example.hms.services.implementation;

import com.example.hms.dtos.booking.*;
import com.example.hms.dtos.bookingservice.BookingServiceInnerDTO;
import com.example.hms.dtos.guest.GuestInnerDTO;
import com.example.hms.dtos.room.RoomInnerDTO;
import com.example.hms.dtos.service.ServiceInnerDTO;
import com.example.hms.entities.Booking;
import com.example.hms.entities.Guest;
import com.example.hms.entities.Room;
import com.example.hms.entities.enumdef.Status;
import com.example.hms.repositories.BookingRepository;
import com.example.hms.repositories.BookingServiceRepository;
import com.example.hms.repositories.GuestRepository;
import com.example.hms.repositories.RoomRepository;
import com.example.hms.entities.BookingService;
import com.example.hms.entities.Service;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hms.exceptions.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BookingServiceImplementation implements com.example.hms.services.BookingService {
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final BookingServiceRepository bookingServiceRepository;

    @Autowired
    public BookingServiceImplementation(
            BookingRepository bookingRepository,
            GuestRepository guestRepository,
            RoomRepository roomRepository,
            BookingServiceRepository bookingServiceRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.bookingServiceRepository = bookingServiceRepository;
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
                    case "prebooking":
                        predicates.add(criteriaBuilder.equal(root.get("isPreBooking"), true));
                        break;
                    case "checked-out":
                        predicates.add(criteriaBuilder.isNotNull(root.get("checkOut")));                       break;
                    case "active":
                        Predicate checkOutNull = criteriaBuilder.isNull(root.get("checkOut"));
                        Predicate checkOutFuture = criteriaBuilder.greaterThan(root.get("checkOut"), criteriaBuilder.currentTimestamp());
                        predicates.add(criteriaBuilder.or(checkOutNull, checkOutFuture));
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
    public BookingDetailsDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );
        return mapToDetailsDTO(booking);
    }

    // for add pre-booking
    @Override
    @Transactional
    public BookingDTO createBooking(BookingCreateDTO bookingCreateDTO) {
        Booking booking = mapToEntity(bookingCreateDTO);
        Room room = booking.getRoom();
        room.setStatus(Status.UNAVAILABLE);
        roomRepository.save(room);
        return mapToDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(Long id, BookingUpdateDTO bookingUpdateDTO) {
        Booking booking = bookingRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id: " + id)
        );

        BigDecimal oldBookingAmount = booking.getAmount();
        updateBookingFields(booking, bookingUpdateDTO);
        BigDecimal newBookingAmount = booking.getAmount();
        updateGuestTotalAmount(booking.getGuest(), oldBookingAmount, newBookingAmount);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToDTO(updatedBooking);
    }

    private void updateBookingFields(Booking booking, BookingUpdateDTO bookingUpdateDTO) {
        if (bookingUpdateDTO.getGuestId() != null) {
            Guest guest = guestRepository.findByIdAndIsDeletedFalse(bookingUpdateDTO.getGuestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + bookingUpdateDTO.getGuestId()));
            booking.setGuest(guest);
        }

        if (bookingUpdateDTO.getRoomId() != null) {
            Room room = roomRepository.findByIdAndIsDeletedFalse(bookingUpdateDTO.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + bookingUpdateDTO.getRoomId()));
            booking.setRoom(room);
        }

        if (bookingUpdateDTO.getCheckIn() != null) {
            booking.setCheckIn(bookingUpdateDTO.getCheckIn());
        }

        if (bookingUpdateDTO.getCheckOut() != null) {
            booking.setCheckOut(bookingUpdateDTO.getCheckOut());
        }

        if (bookingUpdateDTO.getAmount() != null) {
            booking.setAmount(bookingUpdateDTO.getAmount());
        }
    }

    private void updateGuestTotalAmount(Guest guest, BigDecimal oldAmount, BigDecimal newAmount) {
        BigDecimal difference = newAmount.subtract(oldAmount);
        guest.setTotalAmount(guest.getTotalAmount().add(difference));
        guestRepository.save(guest);
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );
        booking.setIsDeleted(true);
        bookingRepository.save(booking);
    }

    private BookingDTO mapToDTO(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getGuest().getId(),
                booking.getRoom().getId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getIsPreBooking(),
                booking.getAmount()
        );
    }

    private BookingPresentationDTO mapToPresentationDTO(Booking booking) {
        return new BookingPresentationDTO(
                booking.getId(),
                mapToInnerDTO(booking.getGuest()),
                mapToInnerDTO(booking.getRoom()),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getIsPreBooking(),
                booking.getAmount()
        );
    }

    private BookingDetailsDTO mapToDetailsDTO(Booking booking) {
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        bookingDetailsDTO.setId(booking.getId());
        bookingDetailsDTO.setGuest(mapToInnerDTO(booking.getGuest()));
        bookingDetailsDTO.setRoom(mapToInnerDTO(booking.getRoom()));
        bookingDetailsDTO.setCheckIn(booking.getCheckIn());
        bookingDetailsDTO.setCheckOut(booking.getCheckOut());
        bookingDetailsDTO.setIsPreBooking(booking.getIsPreBooking());
        bookingDetailsDTO.setAmount(booking.getAmount());

        List<BookingService> services = bookingServiceRepository.findByBookingIdAndIsDeletedFalse(booking.getId());
        List<BookingServiceInnerDTO> serviceInnerDTOS = services.stream().map(this::mapToInnerDTO).collect(Collectors.toList());
        bookingDetailsDTO.setServices(serviceInnerDTOS);
        return bookingDetailsDTO;
    }

    private BookingServiceInnerDTO mapToInnerDTO(BookingService bookingService) {
        BookingServiceInnerDTO bookingServiceInnerDTO = new BookingServiceInnerDTO();
        bookingServiceInnerDTO.setId(bookingService.getId());
        bookingServiceInnerDTO.setService(mapToInnerDTO(bookingService.getService()));
        bookingServiceInnerDTO.setQuantity(bookingService.getQuantity());
        return bookingServiceInnerDTO;
    }

    private GuestInnerDTO mapToInnerDTO(Guest guest) {
        GuestInnerDTO guestInnerDTO = new GuestInnerDTO();
        guestInnerDTO.setId(guest.getId());
        guestInnerDTO.setName(guest.getName());
        guestInnerDTO.setIdCard(guest.getIdCard());
        guestInnerDTO.setGender(guest.getGender());
        guestInnerDTO.setPhone(guest.getPhone());
        return guestInnerDTO;
    }

    private RoomInnerDTO mapToInnerDTO(Room room) {
        RoomInnerDTO roomInnerDTO = new RoomInnerDTO();
        roomInnerDTO.setId(room.getId());
        roomInnerDTO.setNumber(room.getNumber());
        roomInnerDTO.setType(room.getType());
        roomInnerDTO.setPrice(room.getPrice());
        return roomInnerDTO;
    }

    private ServiceInnerDTO mapToInnerDTO(Service service) {
        ServiceInnerDTO serviceInnerDTO = new ServiceInnerDTO();
        serviceInnerDTO.setId(service.getId());
        serviceInnerDTO.setName(service.getName());
        serviceInnerDTO.setCategory(service.getCategory());
        serviceInnerDTO.setPrice(service.getPrice());
        return serviceInnerDTO;
    }

    private Booking mapToEntity(BookingCreateDTO bookingCreateDTO) {
        Booking booking = new Booking();
        Guest guest = guestRepository.findByIdAndIsDeletedFalse(bookingCreateDTO.getGuestId()).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + bookingCreateDTO.getGuestId())
        );
        Room room = roomRepository.findByIdAndIsDeletedFalse(bookingCreateDTO.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + bookingCreateDTO.getRoomId())
        );
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckIn(bookingCreateDTO.getCheckIn());
        booking.setCheckOut(bookingCreateDTO.getCheckOut());
        booking.setIsPreBooking(true);
        return booking;
    }
}

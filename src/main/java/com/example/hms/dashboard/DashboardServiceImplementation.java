package com.example.hms.dashboard;

import com.example.hms.dto.booking.BookingPresentationDTO;
import com.example.hms.dto.bookingservice.BookingServiceInnerDTO;
import com.example.hms.dto.guest.GuestInnerDTO;
import com.example.hms.dto.room.RoomInnerDTO;
import com.example.hms.dto.service.ServiceInnerDTO;
import com.example.hms.entity.Booking;
import com.example.hms.entity.BookingService;
import com.example.hms.entity.Guest;
import com.example.hms.entity.Room;
import com.example.hms.entity.Service;
import com.example.hms.repository.BookingRepository;
import com.example.hms.repository.BookingServiceRepository;
import com.example.hms.repository.GuestRepository;
import com.example.hms.repository.RoomRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.example.hms.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class DashboardServiceImplementation implements DashboardService {
    private final BookingRepository bookingRepository;
    private final BookingServiceRepository bookingServiceRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public DashboardServiceImplementation(BookingRepository bookingRepository, BookingServiceRepository bookingServiceRepository, GuestRepository guestRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingServiceRepository = bookingServiceRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    public Page<OccupiedRoomDTO> getOccupiedRooms(String search, Pageable pageable) {
        Specification<Booking> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isNull(root.get("checkOut")),
                    criteriaBuilder.greaterThan(root.get("checkOut"), LocalDateTime.now())
            ));

            if (search != null && !search.isEmpty()) {
                Predicate guestNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("guest").get("name")), "%" + search.toLowerCase() + "%"
                );
                Predicate roomNumberPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("room").get("number")), "%" + search.toLowerCase() + "%"
                );

                // Combine the predicates using OR
                predicates.add(criteriaBuilder.or(guestNamePredicate, roomNumberPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Fetch bookings with pagination and specification
        Page<Booking> bookingPage = bookingRepository.findAll(spec, pageable);

        // Map entities to DTOs
        List<OccupiedRoomDTO> occupiedRoomDTOList = new ArrayList<>();
        for (Booking booking : bookingPage) {
            occupiedRoomDTOList.add(mapToDTO(booking));
        }

        return new PageImpl<>(occupiedRoomDTOList, pageable, bookingPage.getTotalElements());
    }

    @Override
    public BookingPresentationDTO checkInGuest(CheckInDTO checkInDTO) {
        Booking booking = new Booking();
        booking.setGuest(guestRepository.findById(checkInDTO.getGuestId()).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + checkInDTO.getGuestId())
        ));
        booking.setRoom(roomRepository.findById(checkInDTO.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Room not found on::" + checkInDTO.getRoomId())
        ));
        booking.setIsPreBooking(false);
        bookingRepository.save(booking);
        return mapToPresentationDTO(booking);
    }

    @Override
    public void addRoomService(RoomServiceDTO roomServiceDTO) {

    }

    private OccupiedRoomDTO mapToDTO(Booking activeBooking) {
        OccupiedRoomDTO occupiedRoomDTO = new OccupiedRoomDTO();
        occupiedRoomDTO.setBookingId(activeBooking.getId());
        occupiedRoomDTO.setGuest(mapToInnerDTO(activeBooking.getGuest())); // GuestInnerDTO
        occupiedRoomDTO.setRoom(mapToInnerDTO(activeBooking.getRoom())); // RoomInnerDTO
        occupiedRoomDTO.setCheckIn(activeBooking.getCheckIn());
        occupiedRoomDTO.setCheckOut(activeBooking.getCheckOut());
        List<BookingService> bookingServices = bookingServiceRepository.findByBookingIdAndIsDeletedFalse(activeBooking.getId());
        List<BookingServiceInnerDTO> services = bookingServices.stream().map(this::mapToInnerDTO).toList();
        occupiedRoomDTO.setServices(services);
        return occupiedRoomDTO;
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

    private BookingServiceInnerDTO mapToInnerDTO(BookingService bookingService) {
        BookingServiceInnerDTO bookingServiceInnerDTO = new BookingServiceInnerDTO();
        bookingServiceInnerDTO.setId(bookingService.getId());
        bookingServiceInnerDTO.setService(mapToInnerDTO(bookingService.getService()));
        bookingServiceInnerDTO.setQuantity(bookingService.getQuantity());
        return bookingServiceInnerDTO;
    }

    private ServiceInnerDTO mapToInnerDTO(Service service) {
        ServiceInnerDTO serviceInnerDTO = new ServiceInnerDTO();
        serviceInnerDTO.setId(service.getId());
        serviceInnerDTO.setName(service.getName());
        serviceInnerDTO.setCategory(service.getCategory());
        serviceInnerDTO.setPrice(service.getPrice());
        return serviceInnerDTO;
    }

    private BookingPresentationDTO mapToPresentationDTO(Booking booking) {
        return new BookingPresentationDTO(booking.getId(), mapToInnerDTO(booking.getGuest()), mapToInnerDTO(booking.getRoom()), booking.getCheckIn(), booking.getCheckOut(), booking.getIsPreBooking(), booking.getAmount());
    }
}

package com.example.hms.dashboard;

import com.example.hms.dto.booking.BookingDetailsDTO;
import com.example.hms.dto.booking.BookingPresentationDTO;
import com.example.hms.dto.bookingservice.BookingServiceInnerDTO;
import com.example.hms.dto.guest.GuestInnerDTO;
import com.example.hms.dto.room.RoomInnerDTO;
import com.example.hms.dto.service.ServiceInnerDTO;
import com.example.hms.entity.*;
import com.example.hms.entity.enumdef.Status;
import com.example.hms.repository.*;
import com.example.hms.exception.ResourceNotFoundException;
import com.example.hms.util.RoomPricing;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class DashboardServiceImplementation implements DashboardService {
    private final BookingRepository bookingRepository;
    private final BookingServiceRepository bookingServiceRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ServiceRepository serviceRepository;
    private final static double PRICE_RATING = 10.0;

    @Autowired
    public DashboardServiceImplementation(
            BookingRepository bookingRepository,
            BookingServiceRepository bookingServiceRepository,
            GuestRepository guestRepository,
            RoomRepository roomRepository,
            ServiceRepository serviceRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingServiceRepository = bookingServiceRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Page<OccupiedRoomDTO> getOccupiedRooms(String search, Pageable pageable) {
        Specification<Booking> spec = createOccupiedRoomsSpecification(search);
        Page<Booking> bookingPage = bookingRepository.findAll(spec, pageable);
        List<OccupiedRoomDTO> occupiedRoomDTOList = bookingPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(occupiedRoomDTOList, pageable, bookingPage.getTotalElements());
    }

    @Override
    @Transactional
    public BookingPresentationDTO checkInGuest(CheckInDTO checkInDTO) {
        Guest guest = guestRepository.findByIdAndIsDeletedFalse(checkInDTO.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id: " + checkInDTO.getGuestId()));
        Room room = roomRepository.findByIdAndIsDeletedFalse(checkInDTO.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + checkInDTO.getRoomId()));

        room.setStatus(Status.UNAVAILABLE);
        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setIsPreBooking(false);
        booking.setCheckIn(LocalDateTime.now());
        booking = bookingRepository.save(booking);
        return mapToPresentationDTO(booking);
    }

    @Override
    @Transactional
    public void addRoomService(RoomServiceDTO roomServiceDTO) {
        Booking booking = bookingRepository.findByIdAndIsDeletedFalse(roomServiceDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + roomServiceDTO.getBookingId()));
        Service service = serviceRepository.findByIdAndIsDeletedFalse(roomServiceDTO.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + roomServiceDTO.getServiceId()));

        BookingService bookingService = new BookingService();
        bookingService.setBooking(booking);
        bookingService.setService(service);
        bookingService.setQuantity(roomServiceDTO.getQuantity());
        bookingServiceRepository.save(bookingService);

        BigDecimal additionalAmount = service.getPrice().multiply(BigDecimal.valueOf(roomServiceDTO.getQuantity()));
        booking.setAmount(booking.getAmount().add(additionalAmount));
        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        int totalRooms = roomRepository.getTotalRooms();
        int availableRooms = roomRepository.getAvailableRooms();
        BigDecimal monthRevenue = bookingRepository.getCurrentMonthRevenue();

        return new DashboardStatsDTO(
                totalRooms,
                availableRooms,
                monthRevenue.doubleValue()
        );
    }

    @Override
    @Transactional
    public void updateRoomService(Long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        BookingService bookingService = bookingServiceRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking Service not found on::" + id)
        );

        int oldQuantity = bookingService.getQuantity();
        int differenceQuantity = quantity - oldQuantity;

        BigDecimal servicePrice = bookingService.getService().getPrice();
        BigDecimal differenceAmount = servicePrice.multiply(BigDecimal.valueOf(differenceQuantity));

        Booking booking = bookingService.getBooking();
        BigDecimal oldBookingAmount = booking.getAmount();
        BigDecimal newBookingAmount = oldBookingAmount.add(differenceAmount);

        // Update booking service quantity
        bookingService.setQuantity(quantity);
        bookingServiceRepository.save(bookingService);

        // Update booking amount
        booking.setAmount(newBookingAmount);
        bookingRepository.save(booking);

        // Update guest total amount
        Guest guest = booking.getGuest();
        guest.setTotalAmount(guest.getTotalAmount().add(differenceAmount));
        guestRepository.save(guest);

        // If quantity is 0, consider removing the service from the booking
        if (quantity == 0) {
            bookingService.setIsDeleted(true);
            bookingServiceRepository.save(bookingService);
        }
    }

    @Override
    @Transactional
    public BookingDetailsDTO checkOutGuest(Long id) {
        Booking booking = bookingRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found on::" + id));

        if (booking.getCheckOut() != null) {
            throw new IllegalStateException("Guest has already checked out.");
        }

        Room room = booking.getRoom();
        room.setStatus(Status.AVAILABLE);

        // Set check-out time
        booking.setCheckOut(LocalDateTime.now());

        BigDecimal amount = booking.getAmount();
        BigDecimal roomPricing = RoomPricing.calc(booking.getRoom().getPrice(), BigDecimal.valueOf(PRICE_RATING), booking.getCheckIn(), booking.getCheckOut());
        // Calculate final amount (you might want to add any additional charges here)
        BigDecimal finalAmount =  amount.add(roomPricing);
        booking.setAmount(finalAmount);

        // Update guest's total amount
        Guest guest = booking.getGuest();
        guest.setTotalAmount(guest.getTotalAmount().add(finalAmount));
        guestRepository.save(guest);

        // Save updated booking
        booking = bookingRepository.save(booking);

        // Map to DTO and return
        return mapToDetailsDTO(booking);
    }

    private BookingDetailsDTO mapToDetailsDTO(Booking booking) {
        GuestInnerDTO guestDTO = mapToInnerDTO(booking.getGuest());
        RoomInnerDTO roomDTO = mapToInnerDTO(booking.getRoom());

        List<BookingService> bookingServices = bookingServiceRepository.findByBookingIdAndIsDeletedFalse(booking.getId());
        List<BookingServiceInnerDTO> services = bookingServices.stream()
                .map(this::mapToInnerDTO)
                .collect(Collectors.toList());

        return new BookingDetailsDTO(
                booking.getId(),
                guestDTO,
                roomDTO,
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getIsPreBooking(),
                services,
                booking.getAmount()
        );
    }

    private Specification<Booking> createOccupiedRoomsSpecification(String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isNull(root.get("checkOut")),
                    criteriaBuilder.greaterThan(root.get("checkOut"), LocalDateTime.now())
            ));

            if (search != null && !search.isEmpty()) {
                String lowercaseSearch = "%" + search.toLowerCase() + "%";
                Predicate guestNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("guest").get("name")), lowercaseSearch);
                Predicate roomNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("room").get("number")), lowercaseSearch);
                predicates.add(criteriaBuilder.or(guestNamePredicate, roomNumberPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private OccupiedRoomDTO mapToDTO(Booking activeBooking) {
        OccupiedRoomDTO occupiedRoomDTO = new OccupiedRoomDTO();
        occupiedRoomDTO.setBookingId(activeBooking.getId());
        occupiedRoomDTO.setGuest(mapToInnerDTO(activeBooking.getGuest()));
        occupiedRoomDTO.setRoom(mapToInnerDTO(activeBooking.getRoom()));
        occupiedRoomDTO.setCheckIn(activeBooking.getCheckIn());
        occupiedRoomDTO.setCheckOut(activeBooking.getCheckOut());

        List<BookingService> bookingServices = bookingServiceRepository.findByBookingIdAndIsDeletedFalse(activeBooking.getId());
        List<BookingServiceInnerDTO> services = bookingServices.stream()
                .map(this::mapToInnerDTO)
                .collect(Collectors.toList());
        occupiedRoomDTO.setServices(services);

        return occupiedRoomDTO;
    }

    private GuestInnerDTO mapToInnerDTO(Guest guest) {
        return new GuestInnerDTO(guest.getId(), guest.getName(), guest.getIdCard(), guest.getGender(), guest.getPhone());
    }

    private RoomInnerDTO mapToInnerDTO(Room room) {
        return new RoomInnerDTO(room.getId(), room.getNumber(), room.getType(), room.getPrice());
    }

    private BookingServiceInnerDTO mapToInnerDTO(BookingService bookingService) {
        return new BookingServiceInnerDTO(bookingService.getId(), mapToInnerDTO(bookingService.getService()), bookingService.getQuantity());
    }

    private ServiceInnerDTO mapToInnerDTO(Service service) {
        return new ServiceInnerDTO(service.getId(), service.getName(), service.getCategory(), service.getPrice());
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
}
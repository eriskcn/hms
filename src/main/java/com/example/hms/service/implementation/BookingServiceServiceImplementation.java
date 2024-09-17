package com.example.hms.service.implementation;

import com.example.hms.dto.booking.BookingInnerDTO;
import com.example.hms.dto.bookingservice.BookingServiceCreateDTO;
import com.example.hms.dto.bookingservice.BookingServiceDTO;
import com.example.hms.dto.bookingservice.BookingServicePresentationDTO;
import com.example.hms.dto.room.RoomInnerDTO;
import com.example.hms.dto.service.ServiceInnerDTO;
import com.example.hms.entity.Booking;
import com.example.hms.entity.Room;
import com.example.hms.entity.Service;
import com.example.hms.repository.BookingRepository;
import com.example.hms.repository.BookingServiceRepository;
import com.example.hms.entity.BookingService;
import com.example.hms.repository.ServiceRepository;
import com.example.hms.service.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hms.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BookingServiceServiceImplementation implements BookingServiceService {
    private final BookingServiceRepository bookingServiceRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public BookingServiceServiceImplementation(
            BookingServiceRepository bookingServiceRepository,
            BookingRepository bookingRepository,
            ServiceRepository serviceRepository
    ) {
        this.bookingServiceRepository = bookingServiceRepository;
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<BookingServicePresentationDTO> getAllBookingServices() {
        List<BookingService> bookingServices = bookingServiceRepository.findAllByIsDeletedFalse();
        return bookingServices.stream().map(this::mapToPresentationDTO).collect(Collectors.toList());
    }

    @Override
    public BookingServicePresentationDTO getBookingServiceById(Long id) {
        BookingService bookingService = bookingServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingService not found on::" + id)
        );
        return mapToPresentationDTO(bookingService);
    }

    @Override
    @Transactional
    public BookingServiceDTO createBookingService(BookingServiceCreateDTO bookingServiceCreateDTO) {
        BookingService bookingService = mapToEntity(bookingServiceCreateDTO);
        return mapToDTO(bookingServiceRepository.save(bookingService));
    }

    @Override
    @Transactional
    public BookingServiceDTO updateBookingService(Long id, BookingServiceDTO bookingServiceDTO) {
        BookingService bookingService = bookingServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingService not found on::" + id)
        );
        Booking booking = bookingRepository.findById(bookingServiceDTO.getBookingId()).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + bookingServiceDTO.getBookingId())
        );
        Service service = serviceRepository.findById(bookingServiceDTO.getServiceId()).orElseThrow(
                () -> new ResourceNotFoundException("Service not found on::" + bookingServiceDTO.getServiceId())
        );
        bookingService.setBooking(booking);
        bookingService.setService(service);
        return mapToDTO(bookingServiceRepository.save(bookingService));
    }

    @Override
    @Transactional
    public void deleteBookingService(Long id) {
        BookingService bookingService = bookingServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingService not found on::" + id)
        );
        bookingService.setIsDeleted(true);
        bookingServiceRepository.save(bookingService);
    }

    private BookingServiceDTO mapToDTO(BookingService bookingService) {
        return new BookingServiceDTO(bookingService.getId(), bookingService.getBooking().getId(), bookingService.getService().getId(), bookingService.getQuantity());
    }

    private BookingService mapToEntity(BookingServiceCreateDTO bookingServiceCreateDTO) {
        BookingService bookingService = new BookingService();
        Booking booking = bookingRepository.findById(bookingServiceCreateDTO.getBookingId()).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + bookingServiceCreateDTO.getBookingId())
        );
        Service service = serviceRepository.findById(bookingServiceCreateDTO.getServiceId()).orElseThrow(
                () -> new ResourceNotFoundException("Service not found on::" + bookingServiceCreateDTO.getServiceId())
        );
        bookingService.setBooking(booking);
        bookingService.setService(service);
        bookingService.setQuantity(bookingServiceCreateDTO.getQuantity());
        return bookingService;
    }

    private BookingServicePresentationDTO mapToPresentationDTO(BookingService bookingService) {
        BookingServicePresentationDTO bookingServicePresentationDTO = new BookingServicePresentationDTO();
        bookingServicePresentationDTO.setId(bookingService.getId());
        bookingServicePresentationDTO.setBooking(mapToInnerDTO(bookingService.getBooking()));
        bookingServicePresentationDTO.setService(mapToInnerDTO(bookingService.getService()));
        bookingServicePresentationDTO.setQuantity(bookingService.getQuantity());
        return bookingServicePresentationDTO;
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

    private ServiceInnerDTO mapToInnerDTO(Service service) {
        ServiceInnerDTO serviceInnerDTO = new ServiceInnerDTO();
        serviceInnerDTO.setId(service.getId());
        serviceInnerDTO.setName(service.getName());
        serviceInnerDTO.setCategory(service.getCategory());
        serviceInnerDTO.setPrice(service.getPrice());
        return serviceInnerDTO;
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

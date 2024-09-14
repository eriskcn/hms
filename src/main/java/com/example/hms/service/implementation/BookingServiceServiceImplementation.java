package com.example.hms.service.implementation;

import com.example.hms.dto.BookingServiceDTO;
import com.example.hms.entity.Booking;
import com.example.hms.entity.Service;
import com.example.hms.repository.BookingRepository;
import com.example.hms.repository.BookingServiceRepository;
import com.example.hms.entity.BookingService;
import com.example.hms.repository.ServiceRepository;
import com.example.hms.service.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hms.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BookingServiceServiceImplementation implements BookingServiceService {
    private final BookingServiceRepository bookingServiceRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public BookingServiceServiceImplementation(BookingServiceRepository bookingServiceRepository, BookingRepository bookingRepository, ServiceRepository serviceRepository) {
        this.bookingServiceRepository = bookingServiceRepository;
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<BookingServiceDTO> getAllBookingServices() {
        List<BookingService> bookingServices = bookingServiceRepository.findAllByIsDeletedFalse();
        return bookingServices.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public BookingServiceDTO getBookingServiceById(Long id) {
        BookingService bookingService = bookingServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingService not found on::" + id)
        );
        return mapToDTO(bookingService);
    }

    @Override
    public BookingServiceDTO createBookingService(BookingServiceDTO bookingServiceDTO) {
        BookingService bookingService = mapToEntity(bookingServiceDTO);
        return mapToDTO(bookingServiceRepository.save(bookingService));
    }

    @Override
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
    public void deleteBookingService(Long id) {
        BookingService bookingService = bookingServiceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BookingService not found on::" + id)
        );
        bookingService.setIsDeleted(true);
        bookingServiceRepository.save(bookingService);
    }

    private BookingServiceDTO mapToDTO(BookingService bookingService) {
        return new BookingServiceDTO(bookingService.getId(), bookingService.getBooking().getId(), bookingService.getService().getId());
    }

    private BookingService mapToEntity(BookingServiceDTO bookingServiceDTO) {
        BookingService bookingService = new BookingService();
        Booking booking = bookingRepository.findById(bookingServiceDTO.getBookingId()).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + bookingServiceDTO.getBookingId())
        );
        Service service = serviceRepository.findById(bookingServiceDTO.getServiceId()).orElseThrow(
                () -> new ResourceNotFoundException("Service not found on::" + bookingServiceDTO.getServiceId())
        );
        bookingService.setBooking(booking);
        bookingService.setService(service);
        return bookingService;
    }
}

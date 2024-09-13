package com.example.hms.service.implementation;

import com.example.hms.dto.BookingServiceDTO;
import com.example.hms.repository.BookingServiceRepository;
import com.example.hms.entity.BookingService;
import com.example.hms.service.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceServiceImplementation implements BookingServiceService {
    @Autowired
    private BookingServiceRepository bookingServiceRepository;

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
        BookingService bookingService = new BookingService();
        bookingService.setBooking(bookingServiceDTO.getBooking());
        bookingService.setService(bookingServiceDTO.getService());
        return mapToDTO(bookingServiceRepository.save(bookingService));
    }

    @Override
    public BookingServiceDTO updateBookingService(Long id, BookingServiceDTO bookingServiceDTO) {
         BookingService bookingService = bookingServiceRepository.findById(id).orElseThrow(
                 () -> new ResourceNotFoundException("BookingService not found on::" + id)
         );
         bookingService.setBooking(bookingServiceDTO.getBooking());
         bookingService.setService(bookingServiceDTO.getService());
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

    private BookingServiceDTO mapToDTO(BookingService bookingService){
        return new BookingServiceDTO(bookingService.getId(), bookingService.getBooking(), bookingService.getService());
    }

    private BookingService mapToEntity(BookingServiceDTO bookingServiceDTO){
        BookingService bookingService = new BookingService();
        bookingService.setBooking(bookingServiceDTO.getBooking());
        bookingService.setService(bookingServiceDTO.getService());
        return bookingService;
    }
}

package com.example.hms.controller;

import com.example.hms.dto.bookingservice.BookingServiceDTO;
import com.example.hms.dto.bookingservice.BookingServicePresentationDTO;
import com.example.hms.service.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hms/booking-services")
public class BookingServiceController {
    private final BookingServiceService bookingServiceService;

    @Autowired
    public BookingServiceController(BookingServiceService bookingServiceService) {
        this.bookingServiceService = bookingServiceService;
    }

    @GetMapping
    public List<BookingServicePresentationDTO> getAllBookingService() {
        return bookingServiceService.getAllBookingServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingServicePresentationDTO> getBookingServiceById(@PathVariable("id") Long id) {
        BookingServicePresentationDTO bookingServicePresentationDTO = bookingServiceService.getBookingServiceById(id);
        return ResponseEntity.ok(bookingServicePresentationDTO);
    }

    @PostMapping
    public ResponseEntity<BookingServiceDTO> createBookingService(@RequestBody BookingServiceDTO bookingServiceDTO) {
        BookingServiceDTO newBookingServices = bookingServiceService.createBookingService(bookingServiceDTO);
        return new ResponseEntity<>(newBookingServices, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingServiceDTO> updateBookingService(@PathVariable("id") Long id, @RequestBody BookingServiceDTO bookingServiceDTO) {
        BookingServiceDTO updatedBookingService = bookingServiceService.updateBookingService(id, bookingServiceDTO);
        return ResponseEntity.ok(updatedBookingService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingService(@PathVariable("id") Long id) {
        bookingServiceService.deleteBookingService(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.hms.controller;

import com.example.hms.dto.booking.*;
import com.example.hms.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hms/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<Page<BookingPresentationDTO>> getAllBookings(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterCriteria,
            Pageable pageable
    ) {
        return ResponseEntity.ok(bookingService.getAllBookings(search, filterCriteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailsDTO> getBookingById(@PathVariable("id") Long id) {
        BookingDetailsDTO bookingDetailsDTO = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingDetailsDTO);
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingCreateDTO bookingCreateDTO) {
        BookingDTO newBooking = bookingService.createBooking(bookingCreateDTO);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable("id") Long id, @RequestBody BookingUpdateDTO bookingUpdateDTO) {
        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingUpdateDTO);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}

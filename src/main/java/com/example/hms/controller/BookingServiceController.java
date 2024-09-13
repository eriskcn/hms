package com.example.hms.controller;

import com.example.hms.dto.BookingServiceDTO;
import com.example.hms.service.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hms/bookingservices")
public class BookingServiceController {
    @Autowired
    private BookingServiceService bookingServiceService;

    @GetMapping
    public List<BookingServiceDTO> getAllBookingService(){
        return bookingServiceService.getAllBookingServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingServiceDTO> getBookingServiceById(@PathVariable("id") Long id){
        BookingServiceDTO bookingServiceDTO = bookingServiceService.getBookingServiceById(id);
        return ResponseEntity.ok(bookingServiceDTO);
    }

    @PostMapping
    public ResponseEntity<BookingServiceDTO> createBookingService(@RequestBody BookingServiceDTO bookingServiceDTO){
        BookingServiceDTO newBookingServices = bookingServiceService.createBookingService(bookingServiceDTO);
        return new ResponseEntity<>(newBookingServices, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingServiceDTO> updateBookingService(@PathVariable("id") Long id, @RequestBody BookingServiceDTO bookingServiceDTO){
        BookingServiceDTO updatedBookingService = bookingServiceService.updateBookingService(id, bookingServiceDTO);
        return ResponseEntity.ok(updatedBookingService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingService(@PathVariable("id") Long id){
        bookingServiceService.deleteBookingService(id);
        return ResponseEntity.noContent().build();
    }
}

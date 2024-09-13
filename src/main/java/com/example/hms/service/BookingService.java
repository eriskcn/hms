package com.example.hms.service;

import com.example.hms.dto.BookingDTO;
import com.example.hms.dto.BookingServiceDTO;

import java.util.List;

public interface BookingService {
    List<BookingDTO> getAllBookings();
    BookingDTO getBookingById(Long id);
    BookingDTO createBooking(BookingDTO bookingDTO);
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);
    void deleteBooking(Long id);
}

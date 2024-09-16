package com.example.hms.service;

import com.example.hms.dto.booking.BookingDTO;
import com.example.hms.dto.booking.BookingDetailsDTO;
import com.example.hms.dto.booking.BookingPresentationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookingService {
    Page<BookingPresentationDTO> getAllBookings(String search, String filterCriteria, Pageable pageable);

    BookingDetailsDTO getBookingById(Long id);

    BookingDTO createBooking(BookingDTO bookingDTO);

    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);

    void deleteBooking(Long id);
}

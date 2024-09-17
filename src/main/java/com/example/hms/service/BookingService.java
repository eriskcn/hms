package com.example.hms.service;

import com.example.hms.dto.booking.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookingService {
    Page<BookingPresentationDTO> getAllBookings(String search, String filterCriteria, Pageable pageable);

    BookingDetailsDTO getBookingById(Long id);

    BookingDTO createBooking(BookingCreateDTO bookingCreateDTO);

    BookingDTO updateBooking(Long id, BookingUpdateDTO bookingUpdateDTO);

    void deleteBooking(Long id);
}

package com.example.hms.service;

import com.example.hms.dto.BookingServiceDTO;

import java.util.List;

public interface BookingServiceService {
    List<BookingServiceDTO> getAllBookingServices();
    BookingServiceDTO getBookingServiceById(Long id);
    BookingServiceDTO createBookingService(BookingServiceDTO bookingServiceDTO);
    BookingServiceDTO updateBookingService(Long id, BookingServiceDTO bookingServiceDTO);
    void deleteBookingService(Long id);
}

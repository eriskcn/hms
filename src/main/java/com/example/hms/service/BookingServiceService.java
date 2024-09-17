package com.example.hms.service;

import com.example.hms.dto.bookingservice.BookingServiceCreateDTO;
import com.example.hms.dto.bookingservice.BookingServiceDTO;
import com.example.hms.dto.bookingservice.BookingServicePresentationDTO;

import java.util.List;

public interface BookingServiceService {
    List<BookingServicePresentationDTO> getAllBookingServices();

    BookingServicePresentationDTO getBookingServiceById(Long id);

    BookingServiceDTO createBookingService(BookingServiceCreateDTO bookingServiceCreateDTO);

    BookingServiceDTO updateBookingService(Long id, BookingServiceDTO bookingServiceDTO);

    void deleteBookingService(Long id);
}

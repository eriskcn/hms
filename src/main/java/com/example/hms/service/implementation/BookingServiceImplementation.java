package com.example.hms.service.implementation;

import com.example.hms.dto.BookingDTO;
import com.example.hms.entity.Booking;
import com.example.hms.repository.BookingRepository;
import com.example.hms.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImplementation implements BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAllByIsDeletedFalse();
        return bookings.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );
        return mapToDTO(booking);
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = mapToEntity(bookingDTO);
        return mapToDTO(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );
        booking.setGuest(bookingDTO.getGuest());
        booking.setRoom(bookingDTO.getRoom());
        booking.setCheckIn(bookingDTO.getCheckIn());
        booking.setCheckOut(bookingDTO.getCheckOut());
        booking.setIsPreBooking(bookingDTO.getIsPreBooking());
        booking.setAmount(bookingDTO.getAmount());
        return mapToDTO(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found on::" + id)
        );
        booking.setIsDeleted(true);
        bookingRepository.save(booking);
    }

    private BookingDTO mapToDTO(Booking booking) {
        return new BookingDTO(booking.getId(), booking.getGuest(), booking.getRoom(), booking.getCheckIn(), booking.getCheckOut(), booking.getIsPreBooking(), booking.getAmount());
    }

    private Booking mapToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setGuest(bookingDTO.getGuest());
        booking.setRoom(bookingDTO.getRoom());
        booking.setCheckIn(bookingDTO.getCheckIn());
        booking.setCheckOut(bookingDTO.getCheckOut());
        booking.setIsPreBooking(bookingDTO.getIsPreBooking());
        booking.setAmount(bookingDTO.getAmount());
        return booking;
    }
}

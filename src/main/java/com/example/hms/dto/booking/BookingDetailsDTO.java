package com.example.hms.dto.booking;

import com.example.hms.dto.bookingservice.BookingServiceInnerDTO;
import com.example.hms.entity.Guest;
import com.example.hms.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailsDTO {
    private Long id;
    private Guest guest;
    private Room room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Boolean isPreBooking;
    private List<BookingServiceInnerDTO> services;
    private BigDecimal amount;
}

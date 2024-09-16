package com.example.hms.dto.booking;

import com.example.hms.dto.guest.GuestInnerDTO;
import com.example.hms.dto.room.RoomInnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingPresentationDTO {
    private Long id;
    private GuestInnerDTO guest;
    private RoomInnerDTO room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Boolean isPreBooking;
    private BigDecimal amount;
}

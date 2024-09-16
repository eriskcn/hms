package com.example.hms.dto.booking;

import com.example.hms.dto.room.RoomInnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingInnerDTO {
    private Long id;
    private RoomInnerDTO room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Boolean isPreBooking;
    private BigDecimal amount;
}

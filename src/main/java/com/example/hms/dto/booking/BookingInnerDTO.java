package com.example.hms.dto.booking;

import com.example.hms.entity.Room;
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
    private Room room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Boolean isPreBooking;
    private BigDecimal amount;
}

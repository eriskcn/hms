package com.example.hms.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateDTO {
    private Long guestId;
    private Long roomId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private BigDecimal amount;
}

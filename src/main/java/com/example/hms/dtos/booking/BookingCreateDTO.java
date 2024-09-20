package com.example.hms.dtos.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDTO {
    private Long guestId;
    private Long roomId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
}

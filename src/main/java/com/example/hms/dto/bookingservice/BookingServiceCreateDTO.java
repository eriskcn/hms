package com.example.hms.dto.bookingservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServiceCreateDTO {
    private Long bookingId;
    private Long serviceId;
    private Integer quantity;
}

package com.example.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServiceDTO {
    private Long id;
    private Long bookingId;
    private Long serviceId;
}

package com.example.hms.dto.bookingservice;

import com.example.hms.entity.Booking;
import com.example.hms.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServicePresentationDTO {
    private Long id;
    private Booking booking;
    private Service service;
    private Integer quantity;
}

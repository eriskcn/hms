package com.example.hms.dto;

import com.example.hms.entity.Booking;
import com.example.hms.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServiceDTO {
    private Long id;
    private Booking booking;
    private Service service;
}

package com.example.hms.dto.bookingservice;

import com.example.hms.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServiceInnerDTO {
    private Long id;
    private Service service;
    private Integer quantity;
}

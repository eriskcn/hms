package com.example.hms.dtos.bookingservice;

import com.example.hms.dtos.service.ServiceInnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServiceInnerDTO {
    private Long id;
    private ServiceInnerDTO service;
    private Integer quantity;
}

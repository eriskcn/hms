package com.example.hms.dto.bookingservice;

import com.example.hms.dto.service.ServiceInnerDTO;
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

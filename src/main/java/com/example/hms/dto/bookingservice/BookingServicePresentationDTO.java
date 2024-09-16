package com.example.hms.dto.bookingservice;

import com.example.hms.dto.booking.BookingInnerDTO;
import com.example.hms.dto.service.ServiceInnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingServicePresentationDTO {
    private Long id;
    private BookingInnerDTO booking;
    private ServiceInnerDTO service;
    private Integer quantity;
}

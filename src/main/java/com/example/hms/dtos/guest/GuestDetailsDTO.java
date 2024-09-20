package com.example.hms.dtos.guest;

import com.example.hms.dtos.booking.BookingInnerDTO;
import com.example.hms.entities.enumdef.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestDetailsDTO {
    private Long id;
    private String name;
    private String idCard;
    private Gender gender;
    private String phone;
    private List<BookingInnerDTO> bookings;
    private BigDecimal totalAmount;
}

package com.example.hms.dto.guest;

import com.example.hms.dto.booking.BookingInnerDTO;
import com.example.hms.entity.enumdef.Gender;
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

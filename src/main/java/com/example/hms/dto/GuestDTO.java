package com.example.hms.dto;

import com.example.hms.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {
    private Long id;
    private String name;
    private String idCard;
    private Gender gender;
    private String phone;
    private BigDecimal totalAmount;
}

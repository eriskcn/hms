package com.example.hms.dtos.guest;

import com.example.hms.entities.enumdef.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestInnerDTO {
    private Long id;
    private String name;
    private String idCard;
    private Gender gender;
    private String phone;
}

package com.example.hms.dtos.room;

import com.example.hms.entities.enumdef.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateDTO {
    private String number;
    private Type type;
    private BigDecimal price;
}

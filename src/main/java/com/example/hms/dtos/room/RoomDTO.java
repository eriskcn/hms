package com.example.hms.dtos.room;


import com.example.hms.entities.enumdef.Status;
import com.example.hms.entities.enumdef.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long id;
    private String number;
    private Type type;
    private BigDecimal price;
    private Status status;
}

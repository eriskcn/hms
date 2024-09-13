package com.example.hms.dto;

import com.example.hms.entity.enumdef.Category;
import com.example.hms.entity.enumdef.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Long id;
    private String name;
    private Category category;
    private BigDecimal price;
    private Status status;
}

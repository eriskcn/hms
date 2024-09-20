package com.example.hms.dtos.service;

import com.example.hms.entities.enumdef.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceInnerDTO {
    private Long id;
    private String name;
    private Category category;
    private BigDecimal price;
}

package com.example.hms.dto.service;

import com.example.hms.entity.enumdef.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCreateDTO {
    private String name;
    private Category category;
    private BigDecimal price;
}

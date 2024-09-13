package com.example.hms.entity;

import com.example.hms.entity.enumdef.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="guests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "id_card", length = 12, nullable = false)
    private String idCard;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(length = 15)
    private String phone;

    @Column(name = "total_amount", precision = 10, scale = 2, columnDefinition = "decimal(10, 2) default 0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

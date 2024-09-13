package com.example.hms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="guests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "id_card", length = 12, nullable = false)
    private String idCard;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 15)
    private String phone;

    @Column(name = "total_amount", precision = 10, scale = 2, columnDefinition = "decimal(10, 2) default 0")
    private BigDecimal totalAmount;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

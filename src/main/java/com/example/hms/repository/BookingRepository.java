package com.example.hms.repository;

import com.example.hms.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    List<Booking> findByGuestIdAndIsDeletedFalse(Long id);
    Optional<Booking> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Booking b " +
            "WHERE FUNCTION('YEAR', b.checkIn) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND FUNCTION('MONTH', b.checkIn) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND b.isDeleted = false")
    BigDecimal getCurrentMonthRevenue();
}

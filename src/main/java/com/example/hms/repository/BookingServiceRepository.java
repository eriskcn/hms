package com.example.hms.repository;

import com.example.hms.entity.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingServiceRepository extends JpaRepository<BookingService, Long> {
    List<BookingService> findAllByIsDeletedFalse();
    Optional<BookingService> findByIdAndIsDeletedFalse(Long id);
    List<BookingService> findByBookingIdAndIsDeletedFalse(Long id);
}

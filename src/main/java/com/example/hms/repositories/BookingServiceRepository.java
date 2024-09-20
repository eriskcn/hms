package com.example.hms.repositories;

import com.example.hms.entities.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingServiceRepository extends JpaRepository<BookingService, Long> {
    Optional<BookingService> findByIdAndIsDeletedFalse(Long id);
    List<BookingService> findByBookingIdAndIsDeletedFalse(Long id);
}

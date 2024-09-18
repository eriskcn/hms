package com.example.hms.repository;

import com.example.hms.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>, JpaSpecificationExecutor<Guest> {
    Optional<Guest> findByIdAndIsDeletedFalse(Long id);
}

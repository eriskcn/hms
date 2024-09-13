package com.example.hms.repository;

import com.example.hms.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findALlByIsDeletedFalse();
}

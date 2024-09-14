package com.example.hms.service;

import com.example.hms.dto.ServiceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ServiceService {
    Page<ServiceDTO> getAllServices(String search, String filter, Pageable pageable);

    ServiceDTO getServiceById(Long id);

    ServiceDTO createService(ServiceDTO serviceDTO);

    ServiceDTO updateService(Long id, ServiceDTO serviceDTO);

    void deleteService(Long id);
}

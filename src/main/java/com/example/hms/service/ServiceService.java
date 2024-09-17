package com.example.hms.service;

import com.example.hms.dto.service.ServiceCreateDTO;
import com.example.hms.dto.service.ServiceDTO;
import com.example.hms.dto.service.ServiceUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ServiceService {
    Page<ServiceDTO> getAllServices(
            String search,
            String filter,
            Pageable pageable
    );

    ServiceDTO getServiceById(Long id);

    ServiceDTO createService(ServiceCreateDTO serviceCreateDTO);

    ServiceDTO updateService(Long id, ServiceUpdateDTO serviceUpdateDTO);

    void deleteService(Long id);
}

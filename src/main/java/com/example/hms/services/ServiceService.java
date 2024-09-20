package com.example.hms.services;

import com.example.hms.dtos.service.ServiceCreateDTO;
import com.example.hms.dtos.service.ServiceDTO;
import com.example.hms.dtos.service.ServiceInnerDTO;
import com.example.hms.dtos.service.ServiceUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ServiceService {
    Page<ServiceDTO> getAllServices(
            String search,
            String filter,
            Pageable pageable
    );

    List<ServiceInnerDTO> getAllAvailableServices(String search);

    ServiceDTO getServiceById(Long id);

    ServiceDTO createService(ServiceCreateDTO serviceCreateDTO);

    ServiceDTO updateService(Long id, ServiceUpdateDTO serviceUpdateDTO);

    void deleteService(Long id);
}

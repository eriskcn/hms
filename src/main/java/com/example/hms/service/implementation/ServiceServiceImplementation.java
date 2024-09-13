package com.example.hms.service.implementation;

import com.example.hms.dto.ServiceDTO;
import com.example.hms.entity.Service;
import com.example.hms.repository.ServiceRepository;
import com.example.hms.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImplementation implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Service service = mapToEntity(serviceDTO);
        return mapToDTO(serviceRepository.save(service));
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        List<Service> services = serviceRepository.findALlByIsDeletedFalse();
        return services.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceDTO getServiceById(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Service not found on::" + id)
        );
        return mapToDTO(service);
    }

    @Override
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        Service service = serviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Service not found on::" + id)
        );
        service.setName(serviceDTO.getName());
        service.setCategory(serviceDTO.getCategory());
        service.setPrice(serviceDTO.getPrice());
        service.setStatus(serviceDTO.getStatus());
        return mapToDTO(serviceRepository.save(service));
    }

    @Override
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Service not found on::" + id)
        );
        service.setIsDeleted(true);
        serviceRepository.delete(service);
    }

    private ServiceDTO mapToDTO(Service service) {
        return new ServiceDTO(service.getId(), service.getName(), service.getCategory(), service.getPrice(), service.getStatus());
    }

    private Service mapToEntity(ServiceDTO serviceDTO) {
        Service service = new Service();
        service.setName(serviceDTO.getName());
        service.setCategory(serviceDTO.getCategory());
        service.setPrice(serviceDTO.getPrice());
        service.setStatus(serviceDTO.getStatus());
        return service;
    }
}

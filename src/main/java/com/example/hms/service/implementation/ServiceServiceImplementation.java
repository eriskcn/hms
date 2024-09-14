package com.example.hms.service.implementation;

import com.example.hms.dto.ServiceDTO;
import com.example.hms.entity.Service;
import com.example.hms.repository.ServiceRepository;
import com.example.hms.service.ServiceService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hms.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImplementation implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImplementation(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Service service = mapToEntity(serviceDTO);
        return mapToDTO(serviceRepository.save(service));
    }

    @Override
    public Page<ServiceDTO> getAllServices(String search, String filterCriteria, Pageable pageable) {
        Specification<Service> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search functionality
            if (search != null && !search.isEmpty()) {
                String searchLower = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(root.get("name"), searchLower));
            }

            // Filter functionality
            if (filterCriteria != null && !filterCriteria.isEmpty()) {
                switch (filterCriteria.toLowerCase()) {
                    case "food_and_beverage":
                        predicates.add(criteriaBuilder.equal(root.get("food_and_beverage"), filterCriteria));
                        break;
                    case "spa_and_wellness":
                        predicates.add(criteriaBuilder.equal(root.get("spa_and_wellness"), filterCriteria));
                        break;
                    case "housekeeping":
                        predicates.add(criteriaBuilder.equal(root.get("housekeeping"), filterCriteria));
                        break;
                    case "other":
                        predicates.add(criteriaBuilder.equal(root.get("other"), filterCriteria));
                        break;
                    default:
                        break;
                }
            }

            // Only return non-deleted services
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Service> servicePage = serviceRepository.findAll(spec, pageable);
        return servicePage.map(this::mapToDTO);
    }

    @Override
    public ServiceDTO getServiceById(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service not found on::" + id));
        return mapToDTO(service);
    }

    @Override
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service not found on::" + id));
        service.setName(serviceDTO.getName());
        service.setCategory(serviceDTO.getCategory());
        service.setPrice(serviceDTO.getPrice());
        service.setStatus(serviceDTO.getStatus());
        return mapToDTO(serviceRepository.save(service));
    }

    @Override
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service not found on::" + id));
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

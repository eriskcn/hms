package com.example.hms.service.implementation;

import com.example.hms.dto.service.ServiceCreateDTO;
import com.example.hms.dto.service.ServiceDTO;
import com.example.hms.dto.service.ServiceInnerDTO;
import com.example.hms.dto.service.ServiceUpdateDTO;
import com.example.hms.entity.Service;
import com.example.hms.entity.enumdef.Status;
import com.example.hms.repository.ServiceRepository;
import com.example.hms.service.ServiceService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hms.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImplementation implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImplementation(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
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
                    case "fnb":
                        predicates.add(criteriaBuilder.equal(root.get("category"), "FOOD_AND_BEVERAGE"));
                        break;
                    case "spa":
                        predicates.add(criteriaBuilder.equal(root.get("category"), "SPA_AND_WELLNESS"));
                        break;
                    case "housekeeping":
                        predicates.add(criteriaBuilder.equal(root.get("category"), "HOUSEKEEPING"));
                        break;
                    case "other":
                        predicates.add(criteriaBuilder.equal(root.get("category"), "OTHER"));
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
    public List<ServiceInnerDTO> getAllAvailableServices(String search) {
        Specification<Service> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"), Status.AVAILABLE));
            if (search != null && !search.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + search.toLowerCase() + "%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Service> services = serviceRepository.findAll(spec);
        return services.stream().map(this::mapToInnerDTO).collect(Collectors.toList());
    }

    private ServiceInnerDTO mapToInnerDTO(Service service) {
        return new ServiceInnerDTO(
                service.getId(),
                service.getName(),
                service.getCategory(),
                service.getPrice()
        );
    }

    @Override
    public ServiceDTO getServiceById(Long id) {
        Service service = serviceRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Service not found on::" + id));
        return mapToDTO(service);
    }

    @Override
    @Transactional
    public ServiceDTO createService(ServiceCreateDTO serviceCreateDTO) {
        Service service = mapToEntity(serviceCreateDTO);
        return mapToDTO(serviceRepository.save(service));
    }

    @Override
    @Transactional
    public ServiceDTO updateService(Long id, ServiceUpdateDTO serviceUpdateDTO) {
        Service service = serviceRepository.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Service not found on::" + id)
        );
        if (serviceUpdateDTO.getName() != null) {
            service.setName(serviceUpdateDTO.getName());
        }
        if (serviceUpdateDTO.getCategory() != null) {
            service.setCategory(serviceUpdateDTO.getCategory());
        }
        if (serviceUpdateDTO.getPrice() != null) {
            service.setPrice(serviceUpdateDTO.getPrice());
        }
        if (serviceUpdateDTO.getStatus() != null) {
            service.setStatus(serviceUpdateDTO.getStatus());
        }
        return mapToDTO(serviceRepository.save(service));
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        Service service = serviceRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Service not found on::" + id));
        service.setIsDeleted(true);
        serviceRepository.delete(service);
    }

    private ServiceDTO mapToDTO(Service service) {
        return new ServiceDTO(
                service.getId(),
                service.getName(),
                service.getCategory(),
                service.getPrice(),
                service.getStatus()
        );
    }

    private Service mapToEntity(ServiceCreateDTO serviceCreateDTO) {
        Service service = new Service();
        service.setName(serviceCreateDTO.getName());
        service.setCategory(serviceCreateDTO.getCategory());
        service.setPrice(serviceCreateDTO.getPrice());
        return service;
    }
}

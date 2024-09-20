package com.example.hms.controllers;

import com.example.hms.dtos.service.ServiceCreateDTO;
import com.example.hms.dtos.service.ServiceDTO;
import com.example.hms.dtos.service.ServiceInnerDTO;
import com.example.hms.dtos.service.ServiceUpdateDTO;
import com.example.hms.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hms/services")
@CrossOrigin(origins = "http://localhost:3000")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<Page<ServiceDTO>> getAllServices(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterCriteria,
            Pageable pageable
    ) {
        return ResponseEntity.ok(serviceService.getAllServices(search, filterCriteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable("id") Long id) {
        ServiceDTO serviceDTO = serviceService.getServiceById(id);
        return ResponseEntity.ok(serviceDTO);
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceCreateDTO serviceCreateDTO) {
        ServiceDTO newService = serviceService.createService(serviceCreateDTO);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }

    @PutMapping("'/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable("id") Long id, @RequestBody ServiceUpdateDTO serviceUpdateDTO) {
        ServiceDTO updatedService = serviceService.updateService(id, serviceUpdateDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable("id") Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<ServiceInnerDTO>> getAllAvailableServices(
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(serviceService.getAllAvailableServices(search));
    }
}

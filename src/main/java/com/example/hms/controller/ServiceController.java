package com.example.hms.controller;

import com.example.hms.dto.ServiceDTO;
import com.example.hms.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hms/services")
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
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        ServiceDTO newService = serviceService.createService(serviceDTO);
        return new ResponseEntity<>(newService, HttpStatus.CREATED);
    }

    @PutMapping("'/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable("id") Long id, @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO updatedService = serviceService.updateService(id, serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable("id") Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}

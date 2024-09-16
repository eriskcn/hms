package com.example.hms.controller;

import com.example.hms.dto.guest.GuestDTO;
import com.example.hms.dto.guest.GuestDetailsDTO;
import com.example.hms.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hms/guests")
@CrossOrigin(origins = "http://localhost:3000")
public class GuestController {
    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public ResponseEntity<Page<GuestDTO>> getAllGuests(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterCriteria,
            Pageable pageable
    ) {
        return ResponseEntity.ok(guestService.getAllGuests(search, filterCriteria, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDetailsDTO> getGuestById(@PathVariable Long id) {
        GuestDetailsDTO guestDetailsDTO = guestService.getGuestById(id);
        return ResponseEntity.ok(guestDetailsDTO);
    }

    @PostMapping
    public ResponseEntity<GuestDTO> createGuest(@RequestBody GuestDTO guestDTO) {
        GuestDTO newGuest = guestService.createGuest(guestDTO);
        return new ResponseEntity<>(newGuest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestDTO> updateGuest(@PathVariable Long id, @RequestBody GuestDTO guestDTO) {
        GuestDTO updatedGuest = guestService.updateGuest(id, guestDTO);
        return ResponseEntity.ok(updatedGuest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.noContent().build();
    }
}

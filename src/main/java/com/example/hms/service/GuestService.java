package com.example.hms.service;

import com.example.hms.dto.guest.GuestCreateDTO;
import com.example.hms.dto.guest.GuestDTO;
import com.example.hms.dto.guest.GuestDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuestService {
    Page<GuestDTO> getAllGuests(String search, String filterCriteria, Pageable pageable);

    GuestDetailsDTO getGuestById(Long id);

    GuestCreateDTO createGuest(GuestCreateDTO guestCreateDTO);

    GuestDTO updateGuest(Long id, GuestDTO guestDTO);

    void deleteGuest(Long id);
}

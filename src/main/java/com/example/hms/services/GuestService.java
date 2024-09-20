package com.example.hms.services;

import com.example.hms.dtos.guest.GuestCreateDTO;
import com.example.hms.dtos.guest.GuestDTO;
import com.example.hms.dtos.guest.GuestDetailsDTO;
import com.example.hms.dtos.guest.GuestUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuestService {
    Page<GuestDTO> getAllGuests(
            String search,
            String filterCriteria,
            Pageable pageable
    );

    GuestDetailsDTO getGuestById(Long id);

    GuestDTO createGuest(GuestCreateDTO guestCreateDTO);

    GuestDTO updateGuest(Long id, GuestUpdateDTO guestUpdateDTO);

    void deleteGuest(Long id);
}

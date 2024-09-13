package com.example.hms.service;

import com.example.hms.dto.GuestDTO;

import java.util.List;

public interface GuestService {
    GuestDTO createGuest(GuestDTO guestDTO);
    List<GuestDTO> getAllGuests();
    GuestDTO getGuestById(Long id);
    GuestDTO updateGuest(Long id, GuestDTO guestDTO);
    void deleteGuest(Long id);
}

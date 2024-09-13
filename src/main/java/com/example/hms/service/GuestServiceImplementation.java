package com.example.hms.service;

import com.example.hms.dto.GuestDTO;
import com.example.hms.entity.Guest;
import com.example.hms.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImplementation implements GuestService {
    @Autowired
    private GuestRepository guestRepository;

    @Override
    public GuestDTO createGuest(GuestDTO guestDTO) {
        Guest guest = mapToEntity(guestDTO);
        guest = guestRepository.save(guest);
        return mapToDTO(guest);
    }

    @Override
    public List<GuestDTO> getAllGuests() {
        List<Guest> guests = guestRepository.findAllByIsDeletedFalse();
        return guests.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public GuestDTO getGuestById(Long id) {
        Guest guest = guestRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + id)
        );
        return mapToDTO(guest);
    }

    @Override
    public GuestDTO updateGuest(Long id, GuestDTO guestDTO) {
        Guest guest = guestRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + id)
        );
        guest.setName(guestDTO.getName());
        guest.setIdCard(guestDTO.getIdCard());
        guest.setGender(guestDTO.getGender());
        guest.setPhone(guestDTO.getPhone());
        guest.setTotalAmount(guestDTO.getTotalAmount());
        return mapToDTO(guestRepository.save(guest));
    }

    @Override
    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Guest not found on::" + id)
        );
        guest.setIsDeleted(true);
        guestRepository.save(guest);
    }

    private GuestDTO mapToDTO(Guest guest) {
        return new GuestDTO(guest.getId(), guest.getName(), guest.getIdCard(), guest.getGender(), guest.getPhone(), guest.getTotalAmount());
    }

    private Guest mapToEntity(GuestDTO guestDTO) {
        Guest guest = new Guest();
        guest.setName(guestDTO.getName());
        guest.setIdCard(guestDTO.getIdCard());
        guest.setGender(guestDTO.getGender());
        guest.setPhone(guestDTO.getPhone());
        guest.setTotalAmount(guestDTO.getTotalAmount());
        return guest;
    }
}

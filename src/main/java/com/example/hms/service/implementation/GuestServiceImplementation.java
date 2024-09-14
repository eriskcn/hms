package com.example.hms.service.implementation;

import com.example.hms.dto.GuestDTO;
import com.example.hms.entity.Guest;
import com.example.hms.exception.ResourceNotFoundException;
import com.example.hms.repository.GuestRepository;
import com.example.hms.service.GuestService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GuestServiceImplementation implements GuestService {
    private final GuestRepository guestRepository;

    @Autowired
    public GuestServiceImplementation(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public GuestDTO createGuest(GuestDTO guestDTO) {
        Guest guest = mapToEntity(guestDTO);
        guest = guestRepository.save(guest);
        return mapToDTO(guest);
    }

    @Override
    public Page<GuestDTO> getAllGuests(String search, String filterCriteria, Pageable pageable) {
        Specification<Guest> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search functionality
            if (search != null && !search.isEmpty()) {
                String searchLower = "%" + search.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("idCard")), searchLower),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), searchLower)
                ));
            }

            // Filter functionality
            if (filterCriteria != null && !filterCriteria.isEmpty()) {
                switch (filterCriteria.toLowerCase()) {
                    case "vip":
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), new BigDecimal(1000)));
                        break;
                    case "normal":
                        predicates.add(criteriaBuilder.lessThan(root.get("totalAmount"), new BigDecimal(1000)));
                        break;
                    default:
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), BigDecimal.ZERO));
                        break;
                }
            }

            // Only return non-deleted guests
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Guest> guestsPage = guestRepository.findAll(spec, pageable);
        return guestsPage.map(this::mapToDTO);
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

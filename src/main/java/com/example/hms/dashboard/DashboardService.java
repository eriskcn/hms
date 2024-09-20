package com.example.hms.dashboard;

import com.example.hms.dtos.booking.BookingDetailsDTO;
import com.example.hms.dtos.booking.BookingPresentationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DashboardService {
    Page<OccupiedRoomDTO> getOccupiedRooms(String search, Pageable pageable);
    BookingPresentationDTO checkInGuest(CheckInDTO checkInDTO);
    void addRoomService(RoomServiceDTO roomServiceDTO);
    void updateRoomService(Long id, RoomServiceUpdateDTO roomServiceUpdateDTO);
    BookingDetailsDTO checkOutGuest(Long bookingId);
    DashboardStatsDTO getDashboardStats();
}

package com.example.hms.dashboard;

import com.example.hms.dto.booking.BookingDetailsDTO;
import com.example.hms.dto.booking.BookingPresentationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hms/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/occupied-rooms")
    public ResponseEntity<Page<OccupiedRoomDTO>> getOccupiedRooms(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(dashboardService.getOccupiedRooms(search, pageable));
    }

    @PostMapping("/check-in")
    public ResponseEntity<BookingPresentationDTO> checkInGuest(@RequestBody CheckInDTO checkInDTO) {
        return ResponseEntity.ok(dashboardService.checkInGuest(checkInDTO));
    }

    @PostMapping("/room-service")
    public ResponseEntity<Void> addRoomService(@RequestBody RoomServiceDTO roomServiceDTO) {
        dashboardService.addRoomService(roomServiceDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/room-service/{id}")
    public ResponseEntity<Void> updateRoomService(@PathVariable Long id, @RequestBody RoomServiceUpdateDTO roomServiceUpdateDTO) {
        dashboardService.updateRoomService(id, roomServiceUpdateDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @PutMapping("/check-out/{id}")
    public ResponseEntity<BookingDetailsDTO> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.checkOutGuest(id));
    }
}

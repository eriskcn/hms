package com.example.hms.dashboard;

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
}

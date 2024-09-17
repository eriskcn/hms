package com.example.hms.dashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DashboardService {
    Page<OccupiedRoomDTO> getOccupiedRooms(String search, Pageable pageable);
}

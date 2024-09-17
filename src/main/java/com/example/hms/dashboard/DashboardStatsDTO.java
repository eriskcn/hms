package com.example.hms.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private int totalRoom;
    private int availableRoom;
    private double monthRevenue;
}

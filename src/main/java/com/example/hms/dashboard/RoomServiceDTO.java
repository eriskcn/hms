package com.example.hms.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomServiceDTO {
    private Long bookingId;
    private Long serviceId;
    private int quantity;
}

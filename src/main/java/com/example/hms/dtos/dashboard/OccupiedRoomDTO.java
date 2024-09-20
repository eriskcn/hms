package com.example.hms.dtos.dashboard;

import com.example.hms.dtos.bookingservice.BookingServiceInnerDTO;
import com.example.hms.dtos.guest.GuestInnerDTO;
import com.example.hms.dtos.room.RoomInnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupiedRoomDTO {
    private Long bookingId;
    private GuestInnerDTO guest;
    private RoomInnerDTO room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private List<BookingServiceInnerDTO> services;
}

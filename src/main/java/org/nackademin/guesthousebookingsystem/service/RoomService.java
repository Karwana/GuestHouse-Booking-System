package org.nackademin.guesthousebookingsystem.service;

import org.nackademin.guesthousebookingsystem.dto.RoomDto;

import java.util.List;

public interface RoomService {
    List<RoomDto> getAllRooms();
    RoomDto getRoomById(Long id);
    RoomDto saveRoom(RoomDto roomDto);
    RoomDto updateRoom(Long id, RoomDto roomDto);
    void deleteRoom(Long id);
}

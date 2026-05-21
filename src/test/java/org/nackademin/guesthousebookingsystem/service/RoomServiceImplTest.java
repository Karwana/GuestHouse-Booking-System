package org.nackademin.guesthousebookingsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nackademin.guesthousebookingsystem.dto.RoomDto;
import org.nackademin.guesthousebookingsystem.entity.Room;
import org.nackademin.guesthousebookingsystem.entity.RoomType;
import org.nackademin.guesthousebookingsystem.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "local"})
class RoomServiceImplTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;
    private Room savedRoom;

    @BeforeEach
    void setUp() {
        roomRepository.deleteAll();

        Room room = new Room(null, 101, RoomType.DOUBLE, 1);
        savedRoom = roomRepository.save(room);
    }

    @Test
    void getAllRooms_shouldReturnList() {
        List<RoomDto> result = roomService.getAllRooms();

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomNumber());
    }

    @Test
    void getRoomById_shouldReturnRoom() {
        RoomDto result = roomService.getRoomById(savedRoom.getId());

        assertEquals(101, result.getRoomNumber());
        assertEquals(RoomType.DOUBLE, result.getRoomType());
    }

    @Test
    void saveRoom_shouldReturnSavedRoom() {
        RoomDto newRoom = new RoomDto(null, 102, RoomType.SINGLE, 0);

        RoomDto result = roomService.saveRoom(newRoom);

        assertEquals(102, result.getRoomNumber());
        assertEquals(2, roomRepository.findAll().size());
    }

    @Test
    void updateRoom_shouldUpdateExistingRoom() {
        RoomDto updateInfo = new RoomDto(null, 101, RoomType.DOUBLE, 2);

        RoomDto result = roomService.updateRoom(savedRoom.getId(), updateInfo);

        assertEquals(2, result.getExtraBeds());
    }

    @Test
    void deleteRoom_shouldDeleteWhenNoBookings() {
        roomService.deleteRoom(savedRoom.getId());

        assertEquals(0, roomRepository.findAll().size());
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
    }
}
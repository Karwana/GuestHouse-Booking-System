package org.nackademin.guesthousebookingsystem.repository;

import org.nackademin.guesthousebookingsystem.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository
        extends JpaRepository<Room, Long> {

    boolean existsByRoomNumber(int roomNumber);
}
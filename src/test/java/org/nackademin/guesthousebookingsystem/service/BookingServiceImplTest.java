package org.nackademin.guesthousebookingsystem.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nackademin.guesthousebookingsystem.dto.BookingDto;
import org.nackademin.guesthousebookingsystem.dto.RoomDto;
import org.nackademin.guesthousebookingsystem.entity.Booking;
import org.nackademin.guesthousebookingsystem.entity.Room;
import org.nackademin.guesthousebookingsystem.entity.RoomType;
import org.nackademin.guesthousebookingsystem.repository.BookingRepository;
import org.nackademin.guesthousebookingsystem.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "local"})
@Transactional
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    private final Long customerId = 1L;
    private Room savedRoom;
    private Booking savedBooking;

    @BeforeEach
    void setUp() {
        Room room = new Room(null, 101, RoomType.DOUBLE, 1);
        savedRoom = roomRepository.save(room);

        Booking booking = new Booking(null, customerId, savedRoom,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 5));
        savedBooking = bookingRepository.save(booking);
    }

    @Test
    void getAllBookings_shouldReturnList() {
        List<BookingDto> result = bookingService.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(customerId, result.get(0).getCustomerId());
    }

    @Test
    void saveBooking_shouldSaveWhenDatesAreFree() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto newBooking = new BookingDto(null, customerId, null, roomDto,
                LocalDate.of(2026, 6, 10),
                LocalDate.of(2026, 6, 15));

        BookingDto result = bookingService.saveBooking(newBooking);

        assertNotNull(result.getId());
        assertEquals(2, bookingRepository.findAll().size());
    }

    @Test
    void saveBooking_shouldThrowExceptionWhenDatesOverlap() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto overlappingBooking = new BookingDto(null, customerId, null, roomDto,
                LocalDate.of(2026, 6, 3),
                LocalDate.of(2026, 6, 8));

        assertThrows(IllegalStateException.class, () -> bookingService.saveBooking(overlappingBooking));
    }

    @Test
    void saveBooking_shouldFailWhenCheckoutBeforeCheckin() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto invalidBooking = new BookingDto(null, customerId, null, roomDto,
                LocalDate.of(2026, 6, 10),
                LocalDate.of(2026, 6, 5));

        assertThrows(IllegalArgumentException.class, () -> bookingService.saveBooking(invalidBooking));
    }

    @Test
    void getBookingById_shouldReturnBooking() {
        BookingDto result = bookingService.getBookingById(savedBooking.getId());
        assertEquals(customerId, result.getCustomerId());
    }

    @Test
    void updateBooking_shouldUpdateExistingBooking() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto updateInfo = new BookingDto(null, customerId, null, roomDto,
                LocalDate.of(2026, 7, 2),
                LocalDate.of(2026, 7, 6));

        BookingDto result = bookingService.updateBooking(savedBooking.getId(), updateInfo);
        assertEquals(LocalDate.of(2026, 7, 2), result.getStartDate());
    }

    @Test
    void deleteBooking_shouldDeleteSuccessfully() {
        bookingService.deleteBooking(savedBooking.getId());

        assertEquals(0, bookingRepository.findAll().size());
    }
}
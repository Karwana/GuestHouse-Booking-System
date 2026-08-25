package org.nackademin.guesthousebookingsystem.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nackademin.guesthousebookingsystem.dto.BookingDto;
import org.nackademin.guesthousebookingsystem.dto.RoomDto;
import org.nackademin.guesthousebookingsystem.entity.Booking;
import org.nackademin.guesthousebookingsystem.entity.Customer;
import org.nackademin.guesthousebookingsystem.entity.Room;
import org.nackademin.guesthousebookingsystem.entity.RoomType;
import org.nackademin.guesthousebookingsystem.repository.BookingRepository;
import org.nackademin.guesthousebookingsystem.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    private Customer savedCustomer;
    private Room savedRoom;
    private Booking savedBooking;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer(null, "Maruf", "maruf@test.com", "070123456");
        savedCustomer = customerRepository.save(customer);

        Room room = new Room(null, 101, RoomType.DOUBLE, 1);
        savedRoom = roomRepository.save(room);

        Booking booking = new Booking(null, savedCustomer.getId(), savedRoom,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 5));
        savedBooking = bookingRepository.save(booking);
    }

    @Test
    void getAllBookings_shouldReturnList() {
        List<BookingDto> result = bookingService.getAllBookings();

        assertEquals(1, result.size());
        assertEquals("Maruf", result.get(0).getCustomerName());
    }

    @Test
    void saveBooking_shouldSaveWhenDatesAreFree() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto newBooking = new BookingDto(null, savedCustomer.getId(), savedCustomer.getName(), roomDto,
                LocalDate.of(2026, 6, 10),
                LocalDate.of(2026, 6, 15));

        BookingDto result = bookingService.saveBooking(newBooking);

        assertNotNull(result.getId());
        assertEquals(2, bookingRepository.findAll().size());
    }

    @Test
    void saveBooking_shouldThrowExceptionWhenDatesOverlap() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto overlappingBooking = new BookingDto(null, savedCustomer.getId(), savedCustomer.getName(), roomDto,
                LocalDate.of(2026, 6, 3),
                LocalDate.of(2026, 6, 8));

        assertThrows(IllegalStateException.class, () -> bookingService.saveBooking(overlappingBooking));
    }

    @Test
    void saveBooking_shouldFailWhenCheckoutBeforeCheckin() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto invalidBooking = new BookingDto(null, savedCustomer.getId(), savedCustomer.getName(), roomDto,
                LocalDate.of(2026, 6, 10),
                LocalDate.of(2026, 6, 5));

        assertThrows(IllegalArgumentException.class, () -> bookingService.saveBooking(invalidBooking));
    }

    @Test
    void getBookingById_shouldReturnBooking() {
        BookingDto result = bookingService.getBookingById(savedBooking.getId());
        assertEquals("Maruf", result.getCustomerName());
    }

    @Test
    void updateBooking_shouldUpdateExistingBooking() {
        RoomDto roomDto = new RoomDto(savedRoom.getId(), 101, RoomType.DOUBLE, 1);

        BookingDto updateInfo = new BookingDto(null, savedCustomer.getId(), savedCustomer.getName(), roomDto,
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
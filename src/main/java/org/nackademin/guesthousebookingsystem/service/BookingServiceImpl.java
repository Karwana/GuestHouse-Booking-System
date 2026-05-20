package org.nackademin.guesthousebookingsystem.service;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.dto.BookingDto;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
import org.nackademin.guesthousebookingsystem.dto.RoomDto;
import org.nackademin.guesthousebookingsystem.entity.Booking;
import org.nackademin.guesthousebookingsystem.entity.Customer;
import org.nackademin.guesthousebookingsystem.entity.Room;
import org.nackademin.guesthousebookingsystem.repository.BookingRepository;
import org.nackademin.guesthousebookingsystem.repository.CustomerRepository;
import org.nackademin.guesthousebookingsystem.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;

    private BookingDto toDto(Booking booking) {
        CustomerDto customerDto = new CustomerDto(
                booking.getCustomer().getId(),
                booking.getCustomer().getName(),
                booking.getCustomer().getEmail(),
                booking.getCustomer().getPhoneNumber()
        );
        RoomDto roomDto = new RoomDto(
                booking.getRoom().getId(),
                booking.getRoom().getRoomNumber(),
                booking.getRoom().getRoomType(),
                booking.getRoom().getExtraBeds()
        );
        return new BookingDto(
                booking.getId(),
                customerDto,
                roomDto,
                booking.getStartDate(),
                booking.getEndDate()
        );
    }

    private Booking toEntity(BookingDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Kund hittades inte"));
        Room room = roomRepository.findById(dto.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Rum hittades inte"));
        return new Booking(
                dto.getId(),
                customer,
                room,
                dto.getStartDate(),
                dto.getEndDate()
        );
    }

    private void checkConflicts(BookingDto dto, Long excludeId) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Datum saknas");
        }
        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new IllegalArgumentException(
                    "Utcheckningsdatum måste vara efter incheckningsdatum");
        }
        List<Booking> conflicts = bookingRepository.findOverlapping(
                dto.getRoom().getId(),
                dto.getStartDate(),
                dto.getEndDate(),
                excludeId
        );
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException(
                    "Rummet är redan bokat för de valda datumen");
        }
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BookingDto getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Bokning hittades inte"));
    }

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        checkConflicts(bookingDto, -1L);
        Booking saved = bookingRepository.save(toEntity(bookingDto));
        return toDto(saved);
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        bookingDto.setId(id);
        checkConflicts(bookingDto, id);
        Booking saved = bookingRepository.save(toEntity(bookingDto));
        return toDto(saved);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
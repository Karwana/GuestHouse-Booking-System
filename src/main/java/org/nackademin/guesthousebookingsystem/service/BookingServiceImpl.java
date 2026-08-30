package org.nackademin.guesthousebookingsystem.service;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.client.CustomerClient;
import org.nackademin.guesthousebookingsystem.dto.BookingDto;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
import org.nackademin.guesthousebookingsystem.dto.RoomDto;
import org.nackademin.guesthousebookingsystem.entity.Booking;
import org.nackademin.guesthousebookingsystem.entity.Room;
import org.nackademin.guesthousebookingsystem.repository.BookingRepository;
import org.nackademin.guesthousebookingsystem.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerClient customerClient;

    private BookingDto toDto(Booking booking) {
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
        RoomDto roomDto = new RoomDto(
                booking.getRoom().getId(),
                booking.getRoom().getRoomNumber(),
                booking.getRoom().getRoomType(),
                booking.getRoom().getExtraBeds()
        );

<<<<<<< Updated upstream
        String customerName = "Kund-id: " + booking.getCustomerId();
=======
        String customerName = "Kund-id: "
                + booking.getCustomerId();
>>>>>>> Stashed changes
        try {
            CustomerDto customer = customerClient
                    .getCustomerById(booking.getCustomerId());
            if (customer != null) {
                customerName = customer.getName();
            }
        } catch (RuntimeException e) {
<<<<<<< Updated upstream
            // Kundtjänsten är nere — visa id istället
=======

>>>>>>> Stashed changes
        }

        return new BookingDto(
                booking.getId(),
                booking.getCustomerId(),
                customerName,
                roomDto,
                booking.getStartDate(),
                booking.getEndDate()
        );
    }

    private Booking toEntity(BookingDto dto) {
<<<<<<< Updated upstream
        Room room = roomRepository.findById(dto.getRoom().getId())
=======
        Room room = roomRepository
                .findById(dto.getRoom().getId())
>>>>>>> Stashed changes
                .orElseThrow(() ->
                        new RuntimeException("Rum hittades inte"));
        return new Booking(
                dto.getId(),
                dto.getCustomerId(),
                room,
                dto.getStartDate(),
                dto.getEndDate()
        );
    }

    private void checkConflicts(BookingDto dto, Long excludeId) {
        if (dto.getStartDate() == null
                || dto.getEndDate() == null) {
            throw new IllegalArgumentException("Datum saknas");
        }
        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new IllegalArgumentException(
                    "Utcheckningsdatum måste vara "
                            + "efter incheckningsdatum");
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
                .orElseThrow(() ->
<<<<<<< Updated upstream
                        new RuntimeException("Bokning hittades inte"));
=======
                        new RuntimeException(
                                "Bokning hittades inte"));
>>>>>>> Stashed changes
    }

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
<<<<<<< Updated upstream
        if (!customerClient.customerExists(bookingDto.getCustomerId())) {
=======
        if (!customerClient.customerExists(
                bookingDto.getCustomerId())) {
>>>>>>> Stashed changes
            throw new RuntimeException(
                    "Kund med id "
                            + bookingDto.getCustomerId()
                            + " hittades inte");
        }
        checkConflicts(bookingDto, -1L);
        Booking saved = bookingRepository.save(
                toEntity(bookingDto));
        return toDto(saved);
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
<<<<<<< Updated upstream
        if (!customerClient.customerExists(bookingDto.getCustomerId())) {
=======
        if (!customerClient.customerExists(
                bookingDto.getCustomerId())) {
>>>>>>> Stashed changes
            throw new RuntimeException(
                    "Kund med id "
                            + bookingDto.getCustomerId()
                            + " hittades inte");
        }
        bookingDto.setId(id);
        checkConflicts(bookingDto, id);
        Booking saved = bookingRepository.save(
                toEntity(bookingDto));
        return toDto(saved);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public boolean customerHasActiveBookings(Long customerId) {
        return bookingRepository.existsByCustomerId(customerId);
    }
<<<<<<< Updated upstream
=======

    @Override
    public boolean customerHasBookedRoom(
            Long customerId,
            Long roomId) {
        return bookingRepository
                .existsByCustomerIdAndRoomId(customerId, roomId);
    }
>>>>>>> Stashed changes
}
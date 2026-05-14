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
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Room room = roomRepository.findById(dto.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return new Booking(
                dto.getId(),
                customer,
                room,
                dto.getStartDate(),
                dto.getEndDate()
        );
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
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public BookingDto saveBooking(BookingDto bookingDto) {
        Booking saved = bookingRepository.save(toEntity(bookingDto));
        return toDto(saved);
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        bookingDto.setId(id);
        Booking saved = bookingRepository.save(toEntity(bookingDto));
        return toDto(saved);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}

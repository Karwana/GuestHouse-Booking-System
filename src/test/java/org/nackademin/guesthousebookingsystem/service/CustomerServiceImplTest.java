package org.nackademin.guesthousebookingsystem.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
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
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer(null, "Fahim", "fahim@test.com", "070123456");
        savedCustomer = customerRepository.save(customer);
    }

    @Test
    void getAllCustomers_shouldReturnList() {
        List<CustomerDto> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("Fahim", result.get(0).getName());
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {
        CustomerDto result = customerService.getCustomerById(savedCustomer.getId());

        assertEquals("Fahim", result.getName());
    }

    @Test
    void updateCustomer_shouldUpdateExistingCustomer() {
        CustomerDto updateInfo = new CustomerDto(null, "Fahim Uppdaterad", "fahim.ny@test.com", "070111222");
        CustomerDto result = customerService.updateCustomer(savedCustomer.getId(), updateInfo);
        assertEquals("Fahim Uppdaterad", result.getName());
    }

    @Test
    void saveCustomer_shouldReturnSavedCustomer() {
        CustomerDto newCustomer = new CustomerDto(null, "Karwan", "karwan@test.com", "070987654");

        CustomerDto result = customerService.saveCustomer(newCustomer);

        assertEquals("Karwan", result.getName());
        assertEquals(2, customerRepository.findAll().size());
    }

    @Test
    void deleteCustomer_shouldFailIfCustomerHasActiveBookings() {
        Room room = new Room(null, 101, RoomType.DOUBLE, 1);
        Room savedRoom = roomRepository.save(room);

        Booking booking = new Booking(null, savedCustomer, savedRoom,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 5));
        bookingRepository.save(booking);

        assertThrows(IllegalStateException.class, () ->
                customerService.deleteCustomer(savedCustomer.getId()));
    }

    @Test
    void deleteCustomer_shouldDeleteWhenNoBookings() {
        customerService.deleteCustomer(savedCustomer.getId());
        assertEquals(0, customerRepository.findAll().size());
    }
}
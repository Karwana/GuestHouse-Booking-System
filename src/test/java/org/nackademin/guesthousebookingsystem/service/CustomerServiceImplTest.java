package org.nackademin.guesthousebookingsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
import org.nackademin.guesthousebookingsystem.entity.Customer;
import org.nackademin.guesthousebookingsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "local"})
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

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
    void deleteCustomer_shouldDeleteWhenNoBookings() {
        customerService.deleteCustomer(savedCustomer.getId());
        assertEquals(0, customerRepository.findAll().size());
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }
}
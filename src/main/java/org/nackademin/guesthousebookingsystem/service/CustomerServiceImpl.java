package org.nackademin.guesthousebookingsystem.service;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
import org.nackademin.guesthousebookingsystem.entity.Customer;
import org.nackademin.guesthousebookingsystem.repository.BookingRepository;
import org.nackademin.guesthousebookingsystem.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;

    private CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }

    private Customer toEntity(CustomerDto dto) {
        return new Customer(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getPhoneNumber()
        );
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Kund hittades inte"));
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer saved = customerRepository.save(toEntity(customerDto));
        return toDto(saved);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        customerDto.setId(id);
        Customer saved = customerRepository.save(toEntity(customerDto));
        return toDto(saved);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kund hittades inte"));

        if (!bookingRepository.findByCustomer(customer).isEmpty()) {
            throw new IllegalStateException(
                    "Kan inte ta bort kund! Det finns aktiva bokningar kopplade till kunden");
        }

        customerRepository.deleteById(id);
    }
}
package org.nackademin.guesthousebookingsystem.service;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.entity.Customer;
import org.nackademin.guesthousebookingsystem.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl
        implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    @Override
    public Customer saveCustomer(Customer customer) {

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {

        customerRepository.deleteById(id);
    }
}
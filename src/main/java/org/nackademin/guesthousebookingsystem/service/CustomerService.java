package org.nackademin.guesthousebookingsystem.service;

import org.nackademin.guesthousebookingsystem.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    Customer saveCustomer(Customer customer);

    void deleteCustomer(Long id);
}
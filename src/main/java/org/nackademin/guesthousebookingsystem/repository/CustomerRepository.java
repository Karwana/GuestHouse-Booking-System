package org.nackademin.guesthousebookingsystem.repository;

import org.nackademin.guesthousebookingsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
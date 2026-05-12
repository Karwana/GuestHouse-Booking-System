package org.nackademin.guesthousebookingsystem.controller;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.entity.Customer;
import org.nackademin.guesthousebookingsystem.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public String getCustomers(Model model) {

        model.addAttribute(
                "customers",
                customerService.getAllCustomers()
        );

        return "customers/list";
    }

    @GetMapping("/new")
    public String createCustomerForm(Model model) {

        model.addAttribute(
                "customer",
                new Customer()
        );

        return "customers/create";
    }

    @PostMapping
    public String createCustomer(
            @ModelAttribute Customer customer) {

        customerService.saveCustomer(customer);

        return "redirect:/customers";
    }
}
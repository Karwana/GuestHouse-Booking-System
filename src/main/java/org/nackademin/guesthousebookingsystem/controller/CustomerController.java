package org.nackademin.guesthousebookingsystem.controller;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
import org.nackademin.guesthousebookingsystem.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        model.addAttribute(
                "customer",
                new CustomerDto()
        );

        model.addAttribute(
                "editMode",
                false
        );

        return "customers/list";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable Long id,
                               Model model) {

        model.addAttribute(
                "customers",
                customerService.getAllCustomers()
        );

        model.addAttribute(
                "customer",
                customerService.getCustomerById(id)
        );

        model.addAttribute(
                "editMode",
                true
        );

        return "customers/list";
    }

    @PostMapping("/save")
    public String saveCustomer(
            @ModelAttribute CustomerDto customerDto,
            RedirectAttributes ra) {

        customerService.saveCustomer(customerDto);

        ra.addFlashAttribute(
                "success",
                "Kunden sparades!"
        );

        return "redirect:/customers";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(
            @PathVariable Long id,
            @ModelAttribute CustomerDto customerDto,
            RedirectAttributes ra) {

        customerService.updateCustomer(id, customerDto);

        ra.addFlashAttribute(
                "success",
                "Kunden uppdaterades!"
        );

        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(
            @PathVariable Long id,
            RedirectAttributes ra) {

        try {

            customerService.deleteCustomer(id);

            ra.addFlashAttribute(
                    "success",
                    "Kunden togs bort."
            );

        } catch (IllegalStateException e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/customers";
    }
}
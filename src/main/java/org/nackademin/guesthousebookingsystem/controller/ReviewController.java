package org.nackademin.guesthousebookingsystem.controller;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.client.CustomerClient;
import org.nackademin.guesthousebookingsystem.client.ReviewClient;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
import org.nackademin.guesthousebookingsystem.dto.ReviewDto;
import org.nackademin.guesthousebookingsystem.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewClient reviewClient;
    private final RoomService roomService;
    private final CustomerClient customerClient;

    @GetMapping
    public String getReviews(Model model) {
        try {
            model.addAttribute("rooms",
                    roomService.getAllRooms());
            model.addAttribute("customers",
                    customerClient.getAllCustomers());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("rooms",
                    Collections.emptyList());
            model.addAttribute("customers",
                    Collections.emptyList());
        }
        model.addAttribute("review", new ReviewDto());
        model.addAttribute("reviews", Collections.emptyList());
        return "reviews/list";
    }

    @GetMapping("/room/{roomId}")
    public String getReviewsByRoom(
            @PathVariable Long roomId,
            Model model) {
        try {
            List<ReviewDto> reviews =
                    reviewClient.getReviewsByRoom(roomId);


            List<CustomerDto> allCustomers =
                    customerClient.getAllCustomers();

            reviews.forEach(review -> {
                allCustomers.stream()
                        .filter(c -> c.getId()
                                .equals(review.getCustomerId()))
                        .findFirst()
                        .ifPresent(c -> review
                                .setCustomerName(c.getName()));
            });

            model.addAttribute("reviews", reviews);
            model.addAttribute("rooms",
                    roomService.getAllRooms());
            model.addAttribute("customers", allCustomers);
            model.addAttribute("selectedRoomId", roomId);

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reviews",
                    Collections.emptyList());
            model.addAttribute("rooms",
                    roomService.getAllRooms());
            model.addAttribute("customers",
                    Collections.emptyList());
        }
        model.addAttribute("review", new ReviewDto());
        return "reviews/list";
    }

    @PostMapping("/save")
    public String saveReview(
            @ModelAttribute ReviewDto reviewDto,
            RedirectAttributes ra) {
        try {
            reviewClient.saveReview(reviewDto);
            ra.addFlashAttribute("success",
                    "Recension sparad!");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reviews/room/"
                + reviewDto.getRoomId();
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(
            @PathVariable Long id,
            @RequestParam Long roomId,
            RedirectAttributes ra) {
        try {
            reviewClient.deleteReview(id);
            ra.addFlashAttribute("success",
                    "Recension borttagen.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reviews/room/" + roomId;
    }
}
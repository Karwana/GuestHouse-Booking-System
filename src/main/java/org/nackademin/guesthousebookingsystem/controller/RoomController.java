package org.nackademin.guesthousebookingsystem.controller;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.dto.RoomDto;
import org.nackademin.guesthousebookingsystem.entity.RoomType;
import org.nackademin.guesthousebookingsystem.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public String getRooms(Model model) {

        model.addAttribute(
                "rooms",
                roomService.getAllRooms()
        );

        model.addAttribute(
                "room",
                new RoomDto()
        );

        model.addAttribute(
                "roomTypes",
                RoomType.values()
        );

        model.addAttribute(
                "editMode",
                false
        );

        return "rooms/list";
    }

    @GetMapping("/edit/{id}")
    public String editRoom(@PathVariable Long id,
                           Model model) {

        model.addAttribute(
                "rooms",
                roomService.getAllRooms()
        );

        model.addAttribute(
                "room",
                roomService.getRoomById(id)
        );

        model.addAttribute(
                "roomTypes",
                RoomType.values()
        );

        model.addAttribute(
                "editMode",
                true
        );

        return "rooms/list";
    }

    @PostMapping("/save")
    public String saveRoom(
            @ModelAttribute RoomDto roomDto,
            RedirectAttributes ra) {

        try {

            roomService.saveRoom(roomDto);

            ra.addFlashAttribute(
                    "success",
                    "Rum " + roomDto.getRoomNumber() + " sparades!"
            );

        } catch (IllegalArgumentException e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/rooms";
    }

    @PostMapping("/update/{id}")
    public String updateRoom(
            @PathVariable Long id,
            @ModelAttribute RoomDto roomDto,
            RedirectAttributes ra) {

        roomService.updateRoom(id, roomDto);

        ra.addFlashAttribute(
                "success",
                "Rummet uppdaterades!"
        );

        return "redirect:/rooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(
            @PathVariable Long id,
            RedirectAttributes ra) {

        try {

            roomService.deleteRoom(id);

            ra.addFlashAttribute(
                    "success",
                    "Rummet togs bort."
            );

        } catch (IllegalStateException e) {

            ra.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/rooms";
    }
}
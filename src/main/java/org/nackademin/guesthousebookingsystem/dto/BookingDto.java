package org.nackademin.guesthousebookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;
    private Long customerId;
    private String customerName;
    private RoomDto room;
    private LocalDate startDate;
    private LocalDate endDate;
}
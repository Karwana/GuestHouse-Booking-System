package org.nackademin.guesthousebookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long roomId;
    private Integer rating;
    private String comment;
}
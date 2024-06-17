package com.booking_api.entrypoint.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDto {

    private Long Id;
    private Long propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
}

package com.booking_api.entrypoint.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private GuestDto guestDetails;
}

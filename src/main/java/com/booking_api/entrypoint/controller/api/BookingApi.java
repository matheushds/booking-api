package com.booking_api.entrypoint.controller.api;


import com.booking_api.entrypoint.controller.dto.BookingResponseDto;
import com.booking_api.entrypoint.controller.dto.BookingRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Tag(name = "Booking API", description = "API for booking management")
public interface BookingApi {

    @PostMapping("/booking")
    @ResponseStatus(HttpStatus.CREATED)
    BookingResponseDto createBooking(@RequestBody BookingRequestDto bookingDto, @RequestParam(name = "propertyId") Long propertyId);

    @PutMapping("/booking/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateBooking(@PathVariable Long id, @RequestBody BookingRequestDto bookingDto);

    @PatchMapping("/booking/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void cancelBooking(@PathVariable Long id);

    @PatchMapping("/booking/rebook/{id}")
    void rebookBooking(@PathVariable Long id);

    @DeleteMapping("/booking/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBooking(@PathVariable Long id);

    @GetMapping("/booking/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookingResponseDto getBooking(@PathVariable Long id);

    @GetMapping("/booking/property/{propertyId}")
    @ResponseStatus(HttpStatus.OK)
    List<BookingResponseDto> getBookingByPropertyId(@PathVariable Long propertyId);
}

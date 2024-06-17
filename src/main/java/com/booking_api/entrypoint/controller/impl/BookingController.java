package com.booking_api.entrypoint.controller.impl;

import com.booking_api.entrypoint.controller.api.BookingApi;
import com.booking_api.entrypoint.controller.dto.BookingResponseDto;
import com.booking_api.core.service.BookingService;
import com.booking_api.entrypoint.controller.dto.BookingRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookingController implements BookingApi {

    private final BookingService bookingService;

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingDto, Long propertyId) {
        log.info("Creating booking for property with id: {}", propertyId);
        return bookingService.createBooking(bookingDto, propertyId);
    }

    @Override
    public void updateBooking(Long id, BookingRequestDto bookingDto) {
        log.info("Updating booking with id: {}", id);
        bookingService.updateBooking(id, bookingDto);
    }

    @Override
    public void cancelBooking(Long id) {
        log.info("Cancelling booking with id: {}", id);
        bookingService.cancelBooking(id);
    }

    @Override
    public void rebookBooking(Long id) {
        log.info("Rebooking booking with id: {}", id);
        bookingService.rebookCancelledBooking(id);
    }

    @Override
    public void deleteBooking(Long id) {
        log.info("Deleting booking with id: {}", id);
        bookingService.deleteBooking(id);
    }

    @Override
    public BookingResponseDto getBooking(Long id) {
        log.info("Getting booking with id: {}", id);
        return bookingService.getBookingById(id);
    }

    @Override
    public List<BookingResponseDto> getBookingByPropertyId(Long propertyId) {
        log.info("Getting booking by property id: {}", propertyId);
        return bookingService.findBookingsByPropertyId(propertyId);
    }
}

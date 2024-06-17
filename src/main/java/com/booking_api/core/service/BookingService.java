package com.booking_api.core.service;

import com.booking_api.core.domain.Booking;
import com.booking_api.core.domain.Guest;
import com.booking_api.core.domain.ScheduleDates;
import com.booking_api.core.exception.ActionNotAllowedException;
import com.booking_api.core.exception.BookingNotFoundException;
import com.booking_api.core.exception.DateConflictException;
import com.booking_api.entrypoint.controller.dto.BookingResponseDto;
import com.booking_api.entrypoint.controller.dto.BookingRequestDto;
import com.booking_api.entrypoint.controller.mapper.BookingMapper;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final DateIntegrityVerifierService dateIntegrityVerifierService;
    private final ScheduleDatesRepository scheduleDatesRepository;
    private final PropertyService propertyService;
    private final BookingMapper bookingMapper;


    public BookingResponseDto createBooking(BookingRequestDto bookingDto, Long propertyId) {
        dateIntegrityVerifierService.verifyIntegrity(propertyId, bookingDto.getStartDate(), bookingDto.getEndDate());

        Booking booking = new Booking();
        Guest guest = new Guest();

        guest.setGuestNumber(bookingDto.getGuestDetails().getGuestNumber());
        guest.setDetails(bookingDto.getGuestDetails().getGuestDetails());

        booking.setProperty(propertyService.findPropertyById(propertyId));
        booking.setStartDate(bookingDto.getStartDate());
        booking.setEndDate(bookingDto.getEndDate());
        booking.setGuestDetails(guest);
        booking.setCancelled(false);
        return bookingMapper.toBookingResponseDto(scheduleDatesRepository.save(booking));
    }

    public void updateBooking(Long bookingId, BookingRequestDto bookingDto) {
        Booking booking = findBookingById(bookingId);
        Guest guest = new Guest();

        dateIntegrityVerifierService.verifyUpdateIntegrity(bookingId, booking.getProperty().getId(), bookingDto.getStartDate(), bookingDto.getEndDate());

        guest.setGuestNumber(bookingDto.getGuestDetails().getGuestNumber());
        guest.setDetails(bookingDto.getGuestDetails().getGuestDetails());

        booking.setStartDate(bookingDto.getStartDate());
        booking.setEndDate(bookingDto.getEndDate());
        booking.setGuestDetails(guest);
        scheduleDatesRepository.save(booking);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = findBookingById(bookingId);
        booking.setCancelled(true);
        scheduleDatesRepository.save(booking);
    }

    public void rebookCancelledBooking(Long bookingId) {
        Booking booking = findCanceledBookingById(bookingId);

        try{
            dateIntegrityVerifierService.verifyIntegrity(booking.getProperty().getId(), booking.getStartDate(), booking.getEndDate());
        } catch (DateConflictException e) {
            throw new ActionNotAllowedException("You cant`t rebook a booking that has a conflict with another booking");
        }

        booking.setCancelled(false);
        scheduleDatesRepository.save(booking);
    }

    public void deleteBooking(Long bookingId) {
        Booking booking = findBookingById(bookingId);
        scheduleDatesRepository.delete(booking);
    }

    public BookingResponseDto getBookingById(Long id) {
        return bookingMapper.toBookingResponseDto(this.findBookingById(id));
    }

    private Booking findBookingById(Long id) {
        ScheduleDates scheduleDates = scheduleDatesRepository
                .findById(id).filter(Booking.class::isInstance)
                .filter(booking -> !((Booking) booking).isCancelled())
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        return (Booking) scheduleDates;
    }

    public Booking findCanceledBookingById(Long id) {
        ScheduleDates scheduleDates = scheduleDatesRepository
                .findById(id).filter(Booking.class::isInstance).filter(booking -> ((Booking) booking).isCancelled()).orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        return (Booking) scheduleDates;
    }

    public List<BookingResponseDto> findBookingsByPropertyId(Long propertyId) {
        return scheduleDatesRepository.findBookingsByPropertyId(propertyId)
                .stream()
                .filter(Objects::nonNull)
                .filter(Booking.class::isInstance)
                .map(Booking.class::cast)
                .map(bookingMapper::toBookingResponseDto).toList();
    }
}

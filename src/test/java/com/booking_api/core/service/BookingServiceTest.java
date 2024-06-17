package com.booking_api.core.service;

import com.booking_api.core.domain.Booking;
import com.booking_api.core.domain.Guest;
import com.booking_api.core.domain.Property;
import com.booking_api.core.exception.ActionNotAllowedException;
import com.booking_api.core.exception.BookingNotFoundException;
import com.booking_api.core.exception.DateConflictException;
import com.booking_api.entrypoint.controller.dto.BookingResponseDto;
import com.booking_api.entrypoint.controller.dto.BookingRequestDto;
import com.booking_api.entrypoint.controller.dto.GuestDto;
import com.booking_api.entrypoint.controller.mapper.BookingMapper;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
class BookingServiceTest {

    private static final Long PROPERTY_ID = 1L;
    private static final Long BOOKING_ID = 1L;
    private static final LocalDate START_DATE = LocalDate.of(2021, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2021, 1, 2);

    @Mock
    private DateIntegrityVerifierService dateIntegrityVerifierService;
    @Mock
    private ScheduleDatesRepository scheduleDatesRepository;
    @Mock
    private PropertyService propertyService;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingService bookingService;


    @Test
    void shouldCreateBooking() {
        Mockito.doNothing().when(dateIntegrityVerifierService)
                .verifyIntegrity(Mockito.anyLong(), Mockito.any(), Mockito.any());

        Mockito.when(propertyService.findPropertyById(Mockito.anyLong()))
                .thenReturn(getMockedProperty());

        bookingService.createBooking(getMockedBookingRequestDto(), PROPERTY_ID);

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldUpdateBooking() {
        Mockito.doNothing().when(dateIntegrityVerifierService)
                .verifyIntegrity(Mockito.anyLong(), Mockito.any(), Mockito.any());

        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getMockedBooking()));

        bookingService.updateBooking(BOOKING_ID, getMockedBookingRequestDto());

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    void shouldNotUpdateNotFoundBooking() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(BookingNotFoundException.class, () -> bookingService.updateBooking(BOOKING_ID, getMockedBookingRequestDto()));

        Mockito.verify(scheduleDatesRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void shouldCancelBooking() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getMockedBooking()));

        bookingService.cancelBooking(BOOKING_ID);

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldRebookCancelledBooking() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getCancelledBooking()));

        bookingService.rebookCancelledBooking(BOOKING_ID);

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldNotRebookAnOverlappingBooking() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getCancelledBooking()));

        Mockito.doThrow(new DateConflictException("Date conflict"))
                .when(dateIntegrityVerifierService)
                .verifyIntegrity(Mockito.anyLong(), Mockito.any(), Mockito.any());

        Assertions.assertThrows(ActionNotAllowedException.class, () -> bookingService.rebookCancelledBooking(BOOKING_ID));

        Mockito.verify(scheduleDatesRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void shouldNotRebookValidBooking() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getMockedBooking()));

        Assertions.assertThrows(BookingNotFoundException.class, () -> bookingService.rebookCancelledBooking(BOOKING_ID));

        Mockito.verify(scheduleDatesRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void shouldDeleteBooking() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getMockedBooking()));

        bookingService.deleteBooking(BOOKING_ID);

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    void shouldGetBookingById() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getMockedBooking()));

        Mockito.when(bookingMapper.toBookingResponseDto(Mockito.any()))
                .thenReturn(getMockedBookingResponseDto());

        BookingResponseDto bookingResponseDto =  bookingService.getBookingById(BOOKING_ID);

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingResponseDto(Mockito.any());
        Assertions.assertEquals(BOOKING_ID, bookingResponseDto.getId());
    }

    @Test
    void shouldFindCanceledBookingById() {
        Mockito.when(scheduleDatesRepository.findById(BOOKING_ID))
                .thenReturn(java.util.Optional.of(getCancelledBooking()));

        Booking booking = bookingService.findCanceledBookingById(BOOKING_ID);

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).findById(BOOKING_ID);
        Assertions.assertEquals(BOOKING_ID, booking.getId());
    }

    @Test
    void shouldFindBookingsByPropertyId() {
        Mockito.when(scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID))
                .thenReturn(java.util.List.of(getMockedBooking()));

        Mockito.when(bookingMapper.toBookingResponseDto(Mockito.any()))
                .thenReturn(getMockedBookingResponseDto());

        List<BookingResponseDto> bookingResponseDtoList = bookingService.findBookingsByPropertyId(PROPERTY_ID);

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingResponseDto(Mockito.any());
        Assertions.assertEquals(1, bookingResponseDtoList.size());
    }

    private Property getMockedProperty() {
        return Property.builder()
                .id(PROPERTY_ID)
                .propertyLocation("location")
                .propertyName("name")
                .build();
    }

    private BookingResponseDto getMockedBookingResponseDto() {
        return BookingResponseDto.builder()
                .id(BOOKING_ID)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .guestDetails(GuestDto.builder().guestDetails("details").guestNumber(1).build())
                .build();
    }

    private BookingRequestDto getMockedBookingRequestDto() {
        return BookingRequestDto.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .guestDetails(GuestDto.builder().guestDetails("details").guestNumber(1).build())
                .build();
    }

    private Booking getMockedBooking() {
        return Booking.builder()
                .id(BOOKING_ID)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .guestDetails(getMockedGuest())
                .property(getMockedProperty())
                .isCancelled(false)
                .build();
    }

    private Booking getCancelledBooking() {
        return Booking.builder()
                .id(BOOKING_ID)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .guestDetails(getMockedGuest())
                .property(getMockedProperty())
                .isCancelled(true)
                .build();
    }

    private Guest getMockedGuest() {
        return Guest.builder()
                .guestNumber(1)
                .details("details")
                .build();
    }
}
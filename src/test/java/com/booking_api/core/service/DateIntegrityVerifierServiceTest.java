package com.booking_api.core.service;

import com.booking_api.core.domain.Booking;
import com.booking_api.core.domain.Guest;
import com.booking_api.core.domain.Property;
import com.booking_api.core.domain.ScheduleDates;
import com.booking_api.core.exception.DateConflictException;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DateIntegrityVerifierServiceTest {

    private static final Long PROPERTY_ID = 1L;
    private static final Long BOOKING_ID = 1L;
    private static final LocalDate START_DATE = LocalDate.of(2025, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2025, 1, 2);

    @Mock
    private ScheduleDatesRepository scheduleDatesRepository;

    @InjectMocks
    private DateIntegrityVerifierService dateIntegrityVerifierService;


    @Test
    void shouldVerifyIntegrity() {
        Mockito.when(scheduleDatesRepository.findConflictingEntries(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        Assertions.assertDoesNotThrow(() ->
                dateIntegrityVerifierService.verifyIntegrity(PROPERTY_ID, START_DATE, END_DATE));
    }

    @Test
    void shouldThrowDateConflictException() {
        Mockito.when(scheduleDatesRepository.findConflictingEntries(Mockito.anyLong(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(getMockedBooking()));

        Assertions.assertThrows(DateConflictException.class, () ->
                dateIntegrityVerifierService.verifyIntegrity(PROPERTY_ID, START_DATE, END_DATE));
    }

    @Test
    void shouldThrowDateConflictExceptionWhenStartDateIsBeforeNow() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        Assertions.assertThrows(DateConflictException.class, () ->
                dateIntegrityVerifierService.verifyIntegrity(PROPERTY_ID, startDate, endDate));
    }

    @Test
    void shouldThrowDateConflictExceptionWhenEndDateIsBeforeStartDate() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now();

        Assertions.assertThrows(DateConflictException.class, () ->
                dateIntegrityVerifierService.verifyIntegrity(PROPERTY_ID, startDate, endDate));
    }

    private Property getMockedProperty() {
        return Property.builder()
                .id(PROPERTY_ID)
                .propertyLocation("location")
                .propertyName("name")
                .build();
    }

    private Guest getMockedGuest() {
        return Guest.builder()
                .guestNumber(1)
                .details("details")
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
}
package com.booking_api.core.service;

import com.booking_api.core.exception.DateConflictException;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DateIntegrityVerifierService {

    private final ScheduleDatesRepository scheduleDatesRepository;


    public void verifyIntegrity(Long propertyId, LocalDate start, LocalDate end) {
        isAPastDate(start);
        isEndDateIsBeforeStartDate(start, end);
        hasConflict(propertyId, start, end);
    }

    public void verifyUpdateIntegrity(Long requestId, Long propertyId, LocalDate start, LocalDate end) {
        isAPastDate(start);
        isEndDateIsBeforeStartDate(start, end);
        hasConflict(requestId, propertyId, start, end);
    }

    private void hasConflict(Long propertyId, LocalDate start, LocalDate end) {
        if (!scheduleDatesRepository.findConflictingEntries(propertyId, start, end).isEmpty()) {
            throw new DateConflictException("Date conflict");
        }
    }

    private void hasConflict(Long requestId, Long propertyId, LocalDate start, LocalDate end) {
        if (scheduleDatesRepository.findConflictingEntries(requestId, propertyId, start, end).stream().anyMatch(scheduleDates -> !scheduleDates.getId().equals(requestId))) {
            throw new DateConflictException("Date conflict");
        }
    }

    private void isAPastDate(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new DateConflictException("Invalid date, it is in the past");
        }
    }

    private void isEndDateIsBeforeStartDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new DateConflictException("Invalid date, end date is before start date");
        }
    }
}

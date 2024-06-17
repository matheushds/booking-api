package com.booking_api.infrastructure.repository;

import com.booking_api.core.domain.Block;
import com.booking_api.core.domain.Booking;
import com.booking_api.core.domain.ScheduleDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleDatesRepository extends JpaRepository<ScheduleDates, Long> {

    @Query("SELECT se FROM ScheduleDates se WHERE se.property.id = :propertyId AND (se.startDate < :endDate AND se.endDate > :startDate) AND se.isCancelled = false")
    List<ScheduleDates> findConflictingEntries(@Param("propertyId") Long propertyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT se FROM ScheduleDates se WHERE se.property.id = :propertyId AND (se.startDate < :endDate AND se.endDate > :startDate) AND se.isCancelled = false AND se.id != :requestId")
    List<ScheduleDates> findConflictingEntries(@Param("requestId") Long requestId, @Param("propertyId") Long propertyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT se FROM ScheduleDates se WHERE se.property.id = :propertyId AND se.isCancelled = false")
    List<ScheduleDates> findBookingsByPropertyId(@Param("propertyId") Long propertyId);
}

package com.booking_api.entrypoint.controller.mapper;

import com.booking_api.core.domain.Booking;
import com.booking_api.entrypoint.controller.dto.BookingResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface BookingMapper {
    BookingResponseDto toBookingResponseDto(final Booking booking);
}

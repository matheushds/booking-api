package com.booking_api.entrypoint.controller.impl;

import com.booking_api.core.domain.Booking;
import com.booking_api.entrypoint.controller.dto.BlockRequestDto;
import com.booking_api.entrypoint.controller.dto.BookingRequestDto;
import com.booking_api.entrypoint.controller.dto.GuestDto;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTest {

    private static final URI BOOKING_URL = URI.create("/booking");
    private static final Long PROPERTY_ID = 1L;
    private static final LocalDate START_DATE = LocalDate.of(2035, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2035, 1, 2);
    private static final String BLOCK_URL = "/block";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private ScheduleDatesRepository scheduleDatesRepository;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @BeforeEach
    @AfterEach
    void cleanUp() {
        scheduleDatesRepository.deleteAll();
    }

    @Test
    void shouldCreateBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateBookingWhenDateConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldNotCreateABookingWhileBlockConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(createBlockRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.put(BOOKING_URL + "/" + booking.getId())
                .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateWhenBookingIsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKING_URL + "/cancel/" + booking.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.put(BOOKING_URL + "/" + booking.getId())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotUpdateWhenDateConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                .content(objectMapper.writeValueAsString(createBookingRequestDto2()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.put(BOOKING_URL + "/" + booking.getId())
                .content(objectMapper.writeValueAsString(createBookingRequestDto2()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldCancelBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKING_URL + "/cancel/" + booking.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRebookBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKING_URL + "/cancel/" + booking.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKING_URL + "/rebook/" + booking.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenRebookingWithConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKING_URL + "/cancel/" + booking.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(createBlockRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKING_URL + "/rebook/" + booking.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.delete(BOOKING_URL + "/" + booking.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Booking booking = (Booking) scheduleDatesRepository.findBookingsByPropertyId(PROPERTY_ID).get(0);

        mockMvc.perform(MockMvcRequestBuilders.get(BOOKING_URL + "/" + booking.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookingByPropertyId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_URL).param("propertyId", PROPERTY_ID.toString())
                        .content(objectMapper.writeValueAsString(createBookingRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get(BOOKING_URL + "/property/" + PROPERTY_ID))
                .andExpect(status().isOk());
    }

    private GuestDto createGuestDto() {
        return GuestDto.builder()
                .guestNumber(1)
                .guestDetails("details")
                .build();
    }

    private BookingRequestDto createBookingRequestDto() {
        return BookingRequestDto.builder()
                .startDate(START_DATE)
                .endDate(END_DATE)
                .guestDetails(createGuestDto())
                .build();
    }

    private BookingRequestDto createBookingRequestDto2() {
        return BookingRequestDto.builder()
                .startDate(START_DATE.plusDays(1))
                .endDate(END_DATE.plusDays(1))
                .guestDetails(createGuestDto())
                .build();
    }

    private BlockRequestDto createBlockRequestDto() {
        return BlockRequestDto.builder()
                .propertyId(PROPERTY_ID)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
    }
}
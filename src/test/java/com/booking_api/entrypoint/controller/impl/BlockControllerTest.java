package com.booking_api.entrypoint.controller.impl;

import com.booking_api.entrypoint.controller.dto.BlockRequestDto;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BlockControllerTest {

    private static final Long PROPERTY_ID = 1L;
    private static final String BLOCK_URL = "/block";
    private static final Long BLOCK_ID = 0L;
    private static final LocalDate START_DATE = LocalDate.of(2035, 1, 1);
    private static final LocalDate END_DATE = START_DATE.plusDays(1);

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
    void shouldCreateBlock() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(createBlockRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotAcceptCreateBlockWithDateInThePast() throws Exception {
        BlockRequestDto blockRequestDto = createBlockRequestDto();
        blockRequestDto.setStartDate(LocalDate.of(2020, 1, 1));
        blockRequestDto.setEndDate(LocalDate.of(2019, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(blockRequestDto))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldNotAcceptCreateBlockWithEndDateBeforeStartDate() throws Exception {
        BlockRequestDto blockRequestDto = createBlockRequestDto();
        blockRequestDto.setStartDate(LocalDate.of(2026, 1, 1));
        blockRequestDto.setEndDate(LocalDate.of(2025, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(blockRequestDto))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldNotAcceptCreateBlockWithDateAlreadyExistent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(createBlockRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(createBlockRequestDto()))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldNotCreateBlockWithPropertyNotFound() throws Exception {
        BlockRequestDto blockRequestDto = createBlockRequestDto();
        blockRequestDto.setPropertyId(999L);

        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(blockRequestDto))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateBlock() throws Exception {

        BlockRequestDto blockRequestDto = createBlockRequestDto();

        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                        .content(objectMapper.writeValueAsString(blockRequestDto))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Long blockId = scheduleDatesRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.put(BLOCK_URL + "/" + blockId)
                        .content(objectMapper.writeValueAsString(blockRequestDto))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotUpdateBlockWithDateInThePast() throws Exception {
        BlockRequestDto blockRequestDto = createBlockRequestDto();

        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                .content(objectMapper.writeValueAsString(blockRequestDto))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isCreated());

        Long blockId = scheduleDatesRepository.findAll().get(0).getId();
        blockRequestDto.setStartDate(LocalDate.of(2020, 1, 1));
        blockRequestDto.setEndDate(LocalDate.of(2019, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders.put(BLOCK_URL + "/" + blockId)
                .content(objectMapper.writeValueAsString(blockRequestDto))
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldDeleteBlock() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BLOCK_URL)
                        .content(objectMapper.writeValueAsString(createBlockRequestDto()))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isCreated());

        Long blockId = scheduleDatesRepository.findAll().get(0).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(BLOCK_URL + "/" + blockId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteBlockWithInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BLOCK_URL + "/" + BLOCK_ID))
                .andExpect(status().isNotFound());
    }

    private BlockRequestDto createBlockRequestDto() {
        return BlockRequestDto.builder()
                .propertyId(PROPERTY_ID)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();
    }
}
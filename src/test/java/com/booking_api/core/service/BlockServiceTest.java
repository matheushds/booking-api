package com.booking_api.core.service;

import com.booking_api.core.domain.Block;
import com.booking_api.core.domain.Property;
import com.booking_api.core.exception.BlockNotFoundException;
import com.booking_api.entrypoint.controller.dto.BlockRequestDto;
import com.booking_api.entrypoint.controller.mapper.BlockMapper;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class BlockServiceTest {

    private static final Long BLOCK_ID = 1L;

    @Mock
    private ScheduleDatesRepository scheduleDatesRepository;
    @Mock
    private PropertyService propertyService;
    @Mock
    private DateIntegrityVerifierService dateIntegrityVerifierService;
    @Mock
    private BlockMapper blockMapper;
    @InjectMocks
    private BlockService blockService;

    @Test
    void shouldCreateBlock() {
        Mockito.when(propertyService.findPropertyById(Mockito.anyLong()))
                .thenReturn(getMockedProperty());

        Mockito.doNothing().when(dateIntegrityVerifierService)
                .verifyIntegrity(Mockito.anyLong(), Mockito.any(), Mockito.any());

        blockService.createBlock(getMockedBlockRequestDto());

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldUpdateBlock() {
        Mockito.doNothing().when(dateIntegrityVerifierService)
                .verifyIntegrity(Mockito.anyLong(), Mockito.any(), Mockito.any());

        Mockito.when(scheduleDatesRepository.findById(Mockito.anyLong()))
                .thenReturn(java.util.Optional.of(getMockedBlock()));

        blockService.updateBlock(BLOCK_ID, getMockedBlockRequestDto());

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldDeleteBlock() {
        Mockito.when(scheduleDatesRepository.findById(Mockito.anyLong()))
                .thenReturn(java.util.Optional.of(getMockedBlock()));

        blockService.deleteBlock(Mockito.anyLong());

        Mockito.verify(scheduleDatesRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    void shouldNotDeleteNotFoundBlock() {
        Mockito.when(scheduleDatesRepository.findById(Mockito.any()))
                .thenReturn(java.util.Optional.empty());

        assertThrows(BlockNotFoundException.class, () -> blockService.deleteBlock(Mockito.anyLong()));
    }

    private BlockRequestDto getMockedBlockRequestDto() {
        return BlockRequestDto.builder()
                .build();
    }

    private Property getMockedProperty() {
        return Property.builder()
                .id(1L)
                .build();
    }

    private Block getMockedBlock() {
        return Block.builder()
                .id(1L)
                .build();
    }
}
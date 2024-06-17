package com.booking_api.core.service;

import com.booking_api.core.domain.Block;
import com.booking_api.core.domain.Property;
import com.booking_api.core.exception.BlockNotFoundException;
import com.booking_api.entrypoint.controller.dto.BlockRequestDto;
import com.booking_api.entrypoint.controller.dto.BlockResponseDto;
import com.booking_api.entrypoint.controller.mapper.BlockMapper;
import com.booking_api.infrastructure.repository.ScheduleDatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BlockService {

    private final ScheduleDatesRepository scheduleDatesRepository;
    private final PropertyService propertyService;
    private final DateIntegrityVerifierService dateIntegrityVerifierService;
    private final BlockMapper blockMapper;


    public BlockResponseDto createBlock(BlockRequestDto blockRequestDto) {
        Property property = propertyService.findPropertyById(blockRequestDto.getPropertyId());

        dateIntegrityVerifierService.verifyIntegrity(blockRequestDto.getPropertyId(), blockRequestDto.getStartDate(), blockRequestDto.getEndDate());

        Block block = new Block();

        block.setStartDate(blockRequestDto.getStartDate());
        block.setEndDate(blockRequestDto.getEndDate());
        block.setProperty(property);

        return blockMapper.toBlockResponseDto(scheduleDatesRepository.save(block));
    }

    public void updateBlock(final Long blockId, final BlockRequestDto blockRequestDto) {
        dateIntegrityVerifierService.verifyUpdateIntegrity(blockId, blockRequestDto.getPropertyId(), blockRequestDto.getStartDate(), blockRequestDto.getEndDate());

        Block block = (Block) scheduleDatesRepository
                .findById(blockId).filter(Block.class::isInstance)
                .orElseThrow(() -> new BlockNotFoundException("Block not found"));

        block.setStartDate(blockRequestDto.getStartDate());
        block.setEndDate(blockRequestDto.getEndDate());

        scheduleDatesRepository.save(block);
    }

    public void deleteBlock(Long blockId) {
        scheduleDatesRepository.findById(blockId).ifPresentOrElse(scheduleDatesRepository::delete, () -> {
            throw new BlockNotFoundException("Block not found");
        });
    }
}

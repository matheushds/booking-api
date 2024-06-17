package com.booking_api.entrypoint.controller.impl;

import com.booking_api.core.service.BlockService;
import com.booking_api.entrypoint.controller.api.BlockApi;
import com.booking_api.entrypoint.controller.dto.BlockRequestDto;
import com.booking_api.entrypoint.controller.dto.BlockResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BlockController implements BlockApi {

    private final BlockService blockService;

    @Override
    public BlockResponseDto createBlock(final BlockRequestDto blockRequestDto) {
        return blockService.createBlock(blockRequestDto);
    }

    @Override
    public void updateBlock(final Long blockId, final BlockRequestDto blockRequestDto) {
        blockService.updateBlock(blockId, blockRequestDto);
    }

    @Override
    public void deleteBlock(final Long blockId) {
        blockService.deleteBlock(blockId);
    }
}

package com.booking_api.entrypoint.controller.mapper;

import com.booking_api.core.domain.Block;
import com.booking_api.entrypoint.controller.dto.BlockRequestDto;
import com.booking_api.entrypoint.controller.dto.BlockResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlockMapper {
    @Mapping(source = "property.id", target = "propertyId")
    BlockResponseDto toBlockResponseDto(Block block);
}

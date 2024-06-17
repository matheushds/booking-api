package com.booking_api.entrypoint.controller.api;


import com.booking_api.entrypoint.controller.dto.BlockRequestDto;
import com.booking_api.entrypoint.controller.dto.BlockResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Block API", description = "API for block management")
public interface BlockApi {

    @PostMapping("/block")
    @ResponseStatus(HttpStatus.CREATED)
    BlockResponseDto createBlock(@RequestBody BlockRequestDto blockRequestDto);

    @PutMapping("/block/{blockId}")
    @ResponseStatus(HttpStatus.OK)
    void updateBlock(@PathVariable Long blockId, @RequestBody BlockRequestDto blockRequestDto);

    @DeleteMapping("/block/{blockId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBlock(@PathVariable Long blockId);
}

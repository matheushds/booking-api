package com.booking_api.entrypoint.controller.api;

import com.booking_api.entrypoint.controller.dto.AuthenticationResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Authentication API", description = "API for authentication management")
public interface AuthenticationApi {

    @PostMapping("/login")
    AuthenticationResponseDto login(Authentication authentication);
}

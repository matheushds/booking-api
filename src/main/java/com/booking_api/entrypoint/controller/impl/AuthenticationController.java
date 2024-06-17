package com.booking_api.entrypoint.controller.impl;

import com.booking_api.entrypoint.controller.api.AuthenticationApi;
import com.booking_api.entrypoint.controller.dto.AuthenticationResponseDto;
import com.booking_api.infrastructure.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private AuthenticationService authenticationService;

    @Override
    public AuthenticationResponseDto login(Authentication authentication) {
        return new AuthenticationResponseDto(authenticationService.authenticate(authentication));
    }
}

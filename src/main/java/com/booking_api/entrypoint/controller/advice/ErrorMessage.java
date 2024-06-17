package com.booking_api.entrypoint.controller.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ErrorMessage {

    private String message;
    private int status;
}

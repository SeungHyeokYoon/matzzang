package com.matzzangteam.matzzang.dto.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ClientErrorResponse(
        HttpStatus status,
        Object message
) {
}

package com.pm.patientservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiSuccessResponse<T>(
        boolean success,
        String message,
        T data
) {
}

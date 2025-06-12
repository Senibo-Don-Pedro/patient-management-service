package com.pm.patientservice.dto;

import com.pm.patientservice.utils.ApiStatus;

public record ApiResponse<T>(
        ApiStatus status,
        String message,
        T data,
        Object errors
) {
}

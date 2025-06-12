package com.pm.patientservice.dto;

public record ValidationError(
    String field,
    String message,
    Object rejectedValue
) {}

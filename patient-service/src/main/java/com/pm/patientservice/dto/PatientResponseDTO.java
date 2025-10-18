package com.pm.patientservice.dto;

import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

/**
 * DTO for {@link Patient}
 */
public record PatientResponseDTO(
        String id,
        String name,
        String email,
        String address,
        LocalDate dateOfBirth
)  {
}

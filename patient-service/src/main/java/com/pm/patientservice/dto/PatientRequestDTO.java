package com.pm.patientservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.lib.MultiFormatLocalDateDeserializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO for {@link com.pm.patientservice.model.Patient}
 */
public record PatientRequestDTO(

        @Size(message = "Name cannot exceed 100 characters", max = 100)
        @NotBlank(message = "Name is required")
        String name,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Date of Birth is required")
        @JsonDeserialize(using = MultiFormatLocalDateDeserializer.class)
        LocalDate dateOfBirth,

        @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered Date is required")
        @JsonDeserialize(using = MultiFormatLocalDateDeserializer.class)
        LocalDate registeredDate
) {
}

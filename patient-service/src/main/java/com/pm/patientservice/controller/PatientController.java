package com.pm.patientservice.controller;

import com.pm.patientservice.dto.ApiResponse;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import com.pm.patientservice.utils.ApiStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing Patients ")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        ApiStatus.SUCCESS,
                        "Patients Fetched Successfully",
                        patients,
                        null
                )
        );
    }

    @PostMapping
    @Operation(summary = "Create a new patient")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        ApiStatus.SUCCESS,
                        "Patient created successfully",
                        patientResponseDTO,
                        null
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id, patientRequestDTO);

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        ApiStatus.SUCCESS,
                        "Patient updated successfully",
                        patientResponseDTO,
                        null
                )
        );

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing patient")
    public ResponseEntity<ApiResponse<Object>> deletePatient(@PathVariable UUID id) {

        patientService.deletePatient(id);

        return ResponseEntity.ok().body(
                new ApiResponse<>(
                        ApiStatus.SUCCESS,
                        String.format("Patient with id %s deleted successfully", id),
                        null,
                        null
                )
        );
    }
}

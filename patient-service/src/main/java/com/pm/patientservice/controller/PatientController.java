package com.pm.patientservice.controller;

import com.pm.patientservice.dto.ApiResponse;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;
import com.pm.patientservice.utils.ApiStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
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
    public ResponseEntity<ApiResponse<PatientResponseDTO>> createPatient(
            @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
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
    public ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(
            @PathVariable UUID id,
            @RequestBody PatientRequestDTO patientRequestDTO) {
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
}

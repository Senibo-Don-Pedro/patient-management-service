package com.pm.patientservice.service;

import com.pm.patientservice.dto.ApiSuccessResponse;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    ApiSuccessResponse<List<PatientResponseDTO>> getPatients();

    ApiSuccessResponse<PatientResponseDTO> createPatient(PatientRequestDTO patientRequestDTO);

    ApiSuccessResponse<PatientResponseDTO> updatePatient(UUID id,
                                                         PatientRequestDTO patientRequestDTO);

    void deletePatient(UUID id);
}

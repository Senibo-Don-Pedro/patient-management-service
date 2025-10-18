package com.pm.patientservice.service.impl;

import com.pm.patientservice.dto.ApiSuccessResponse;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.AlreadyExistsException;
import com.pm.patientservice.exception.NotFoundException;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    @Override
    public ApiSuccessResponse<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientRepository.findAll().stream().map(
                this::toDto
        ).toList();

        return new ApiSuccessResponse<>(
                true,
                "Patients returned Successfully",
                patients
        );
    }

    @Override
    public ApiSuccessResponse<PatientResponseDTO> createPatient(PatientRequestDTO patientRequestDTO) {

        if (patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new AlreadyExistsException(String.format(
                    "Patient with email -> %s already exists",
                    patientRequestDTO.email()));
        }

        Patient patient = patientRepository.save(toEntity(patientRequestDTO));

        return new ApiSuccessResponse<>(
                true,
                "Patient created successfully",
                toDto(patient)
        );
    }

    @Override
    public ApiSuccessResponse<PatientResponseDTO> updatePatient(UUID id,
                                                                PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Patient not found with id: %s", id))
        );

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.email(), id)) {
            throw new AlreadyExistsException(
                    "A patient with email -> " + patientRequestDTO.email() + " already exists"
            );
        }

        patient.setName(patientRequestDTO.name());
        patient.setEmail(patientRequestDTO.email());
        patient.setAddress(patientRequestDTO.address());
        patient.setDateOfBirth(patientRequestDTO.dateOfBirth());

        Patient updatedPatient = patientRepository.save(patient);

        return new ApiSuccessResponse<>(
                true,
                "Patient updated successfully",
                toDto(updatedPatient));

    }

    @Override
    public void deletePatient(UUID id) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Patient not found with id: %s", id))
        );


        patientRepository.deleteById(id);
    }


    private PatientResponseDTO toDto(Patient patient) {
        return new PatientResponseDTO(
                patient.getId().toString(),
                patient.getName(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getDateOfBirth()
        );
    }

    private Patient toEntity(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();

        patient.setName(patientRequestDTO.name());
        patient.setEmail(patientRequestDTO.email());
        patient.setAddress(patientRequestDTO.address());
        patient.setDateOfBirth(patientRequestDTO.dateOfBirth());
        patient.setRegisteredDate(patientRequestDTO.registeredDate());

        return patient;
    }

}

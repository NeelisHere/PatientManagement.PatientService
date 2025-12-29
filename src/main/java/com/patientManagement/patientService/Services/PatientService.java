package com.patientManagement.patientService.Services;

import com.patientManagement.patientService.DTOs.PatientRequestDTO;
import com.patientManagement.patientService.DTOs.PatientResponseDTO;
import com.patientManagement.patientService.Exceptions.EmailAlreadyExistsException;
import com.patientManagement.patientService.Exceptions.PatientNotFoundException;
import com.patientManagement.patientService.Models.Patient;
import com.patientManagement.patientService.Repositories.PatientRepository;
import com.patientManagement.patientService.grpc.BillingServiceGrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private static final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final SQSProducerService sqsProducerService;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, SQSProducerService sqsProducerService) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.sqsProducerService = sqsProducerService;
    }
    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientResponseDTO::toDTO).toList();
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        String email = patientRequestDTO.getEmail();
        if (patientRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format("A patient with email: %s already exists!", email));
        }
        Patient newPatient = patientRepository.save(PatientRequestDTO.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(newPatient.getId().toString())
                .setEmail(newPatient.getEmail())
                .setName(newPatient.getName())
                .setEventType("test-event")
                .build();
        sqsProducerService.send(patientEvent);
        return PatientResponseDTO.toDTO(newPatient);
    }
    public PatientResponseDTO updatePatient(UUID patientId, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new PatientNotFoundException(String.format("Patient not found with id: %s!", patientId.toString()))
        );
        String email = patientRequestDTO.getEmail();
        if (patientRepository.existsByEmailAndIdNot(email, patientId)) {
            throw new EmailAlreadyExistsException(String.format("Another patient with email: %s already exists!", email));
        }
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setEmail(patientRequestDTO.getEmail());

        return PatientResponseDTO.toDTO(patientRepository.save(patient));
    }

    public void deletePatient(UUID patientId) {
        patientRepository.deleteById(patientId);
    }
}

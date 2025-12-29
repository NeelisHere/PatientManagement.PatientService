package com.patientManagement.patientService.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.patientManagement.patientService.Models.Patient;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class PatientRequestDTO {
    @NotBlank(message = "Name should not be blank!")
    @Size(max = 100, message = "Name should not exceed 100 characters!")
    private String name;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Email should be valid!")
    private String email;

    @NotBlank(message = "Address should not be blank!")
    @Size(max = 255, message = "Address should not exceed 255 characters!")
    private String address;

    @NotNull(message = "Date of birth must be provided!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;

    @NotNull(message = "Registration date must be provided!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String registrationDate;

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegistrationDate(LocalDate.parse(patientRequestDTO.getRegistrationDate()));
        return patient;
    }

    @Override
    public String toString() {
        return "PatientRequestDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}

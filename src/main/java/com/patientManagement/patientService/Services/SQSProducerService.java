package com.patientManagement.patientService.Services;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

import java.util.Base64;

@Service
public class SQSProducerService {
    private final SqsTemplate sqsTemplate;
    private static final String QUEUE_NAME = "patient-sqs";

    public SQSProducerService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void send(PatientEvent patientEvent) {
        String base64 = Base64.getEncoder().encodeToString(patientEvent.toByteArray());
        sqsTemplate.send(QUEUE_NAME, base64);
    }
}

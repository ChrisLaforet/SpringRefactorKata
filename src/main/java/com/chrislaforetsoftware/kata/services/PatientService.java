package com.chrislaforetsoftware.kata.services;

import java.util.List;

import com.chrislaforetsoftware.kata.domain.Patient;
import com.chrislaforetsoftware.kata.domain.TestResult;

public interface PatientService {

	Patient getPatient(String patientId);

	List<String> getPendingLabAccessionsForPatient(Patient patient);

	void updatePatientResults(Patient patient, List<TestResult> results);
}
package com.chrislaforetsoftware.kata.services.patient;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chrislaforetsoftware.kata.domain.Patient;
import com.chrislaforetsoftware.kata.domain.TestResult;
import com.chrislaforetsoftware.kata.repository.PatientRepository;
import com.chrislaforetsoftware.kata.services.PatientService;

@Service
public class PatientServiceImpl implements PatientService {
	
	@Autowired
	PatientRepository repository;
	
	@Override
	public Patient getPatient(String patientId) {
		return repository.findById(patientId);
	}
	
	@Override
	public List<String> getPendingLabAccessionsForPatient(Patient patient) {
		return Arrays.asList(repository.loadPendingLabAccessions(patient.getId()));
	}
	
	@Override
	public void updatePatientResults(Patient patient, List<TestResult> results) {
		repository.saveResults(patient.getId(), results);
	}
}

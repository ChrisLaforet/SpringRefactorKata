package com.chrislaforetsoftware.kata.repository;

import java.util.List;

import com.chrislaforetsoftware.kata.domain.Patient;
import com.chrislaforetsoftware.kata.domain.TestResult;

public interface PatientRepository {

	Patient findById(String patientId);

	String[] loadPendingLabAccessions(String patientId);

	void saveResults(String patientId, List<TestResult> results);

}
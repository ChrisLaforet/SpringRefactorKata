package com.chrislaforetsoftware.kata.repository.patient;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.chrislaforetsoftware.kata.domain.Patient;
import com.chrislaforetsoftware.kata.domain.TestResult;
import com.chrislaforetsoftware.kata.repository.PatientRepository;

@Repository
public class PatientRepositoryImpl implements PatientRepository {

	private static final String patientURL = "https://services.myservice.org";
	
	@Override
	public Patient findById(String patientId) {
	    RestTemplate restTemplate = new RestTemplate();
	    return restTemplate.getForObject(patientURL + "/id/" + patientId, Patient.class);
	}

	@Override
	public String[] loadPendingLabAccessions(String patientId) {
	    RestTemplate restTemplate = new RestTemplate();
	    return restTemplate.getForObject(patientURL + "/pendingAccessions/" + patientId, String[].class);	
	   }

	@Override
	public void saveResults(String patientId, List<TestResult> results) {
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.postForEntity(patientURL + "/results/" + patientId, results, Void.class);
	}
}

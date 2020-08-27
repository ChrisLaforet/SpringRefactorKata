package com.chrislaforetsoftware.kata.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.chrislaforetsoftware.kata.domain.LabTestResult;
import com.chrislaforetsoftware.kata.domain.Patient;
import com.chrislaforetsoftware.kata.domain.TestResult;
import com.chrislaforetsoftware.kata.dto.PatientRequest;
import com.chrislaforetsoftware.kata.services.LabResultsService;
import com.chrislaforetsoftware.kata.services.PatientService;

@Service
public class LoadResultsFromLabCommandHandler implements Command<PatientRequest, Void> {

	@Autowired
	LabResultsService labResultsService;
	
	@Autowired
	PatientService patientService;
	
	@Override
	public Void handle(PatientRequest command) {
		Patient patient = loadPatientOrFail(command.getPatientId());
		
		List<TestResult> testResults = loadTestResultsForPatient(patient);
		savePatientTestResults(patient, testResults);
		
		return null;
	}

	private Patient loadPatientOrFail(String patientId) {
		Patient patient = patientService.getPatient(patientId);
		if (patient == null) {
			throw new IllegalArgumentException("Patient not found");
		}
		return patient;
	}
	
	private List<TestResult> loadTestResultsForPatient(Patient patient) {
		List<TestResult> testResults = new ArrayList<>();
		List<String> accessions = patientService.getPendingLabAccessionsForPatient(patient);
		for (String accession : accessions) {
			List<LabTestResult> results = labResultsService.getLabResultsFor(accession);
			if (!CollectionUtils.isEmpty(results)) {
				testResults.addAll(mapValidTestResults(results));
			}
		}
		return testResults;
	}
	
	private List<TestResult> mapValidTestResults(List<LabTestResult> labResults) {
		List<TestResult> testResults = new ArrayList<>();
		for (LabTestResult labResult : labResults) {
			if (labResult.getTestCode().startsWith("HEM") ||
					labResult.getTestCode().startsWith("CHEM") ||
					labResult.getTestCode().startsWith("GENE") ||
					labResult.getTestCode().startsWith("UROL") ||
					labResult.getTestCode().startsWith("MICRO") ||
					labResult.getTestCode().startsWith("IMM")) {
				testResults.add(mapLabTestResult(labResult));
			} else if (labResult.getTestCode().equals("PATH0001") ||
					labResult.getTestCode().equals("PATH0002") ||
					labResult.getTestCode().equals("PATH0016") ||
					labResult.getTestCode().equals("PATH0017") ||
					labResult.getTestCode().equals("PATH1009")) {
				TestResult testResult = mapLabTestResult(labResult);
				testResult.setLow(false);
				testResult.setTestCode(testResult.getTestCode().substring(0,3) + testResult.getTestCode().substring(4));		// convert code to our internal PAT format
				testResults.add(testResult);
			} else if (labResult.getTestCode().startsWith("ALLER")) {
				TestResult testResult = mapLabTestResult(labResult);
				testResult.setLow(false);
				int testNumber = Integer.parseInt(testResult.getTestCode().substring(5));
				if (testNumber >= 100 && testNumber < 199) {
					testResult.setTestCode("IGE" + String.format("%04d", testNumber % 99));		// convert code to our internal IGE00xx format
				} else {
					testResult.setTestCode("ALGY" + testResult.getTestCode().substring(5));		// convert code to our internal ALGY format
				}
				testResults.add(testResult);
			} else if (labResult.getTestCode().equals("IMM0192")) {
				TestResult testResult = new TestResult();
				testResult.setTestCode("COV0001");
				testResult.setTestName("Covid-19 molecular");
				testResult.setDatePerformed(labResult.getDatePerformed());
				testResult.setResultValue(labResult.getValue());
				testResult.setResultUnits(labResult.getUnits());
				if (labResult.getLowNormalHigh().equalsIgnoreCase("POSITIVE")) {
					testResult.setHigh(true);
				}
				testResults.add(testResult);
			} else if (labResult.getTestCode().startsWith("IMM")) {
				testResults.add(mapLabTestResult(labResult));
			} else if (labResult.getTestCode().equals("IMM0189")) {
				TestResult testResult = new TestResult();
				testResult.setTestCode("COV0002");
				testResult.setTestName("Covid-19 serology");
				testResult.setDatePerformed(labResult.getDatePerformed());
				testResult.setResultValue(labResult.getValue());
				testResult.setResultUnits(labResult.getUnits());
				if (labResult.getLowNormalHigh().equalsIgnoreCase("POSITIVE")) {
					testResult.setHigh(true);
				}
				testResults.add(testResult);
			} else if (labResult.getTestCode().equals("VIR00001") ||
					labResult.getTestCode().equals("VIR00002") ||
					labResult.getTestCode().equals("VIR00003") ||
					labResult.getTestCode().equals("VIR00004") ||
					labResult.getTestCode().equals("VIR00005") ||
					labResult.getTestCode().equals("VIR00006") ||
					labResult.getTestCode().equals("VIR00007")) {
				TestResult testResult = mapLabTestResult(labResult);
				testResult.setTestCode(testResult.getTestCode().substring(0,3) + testResult.getTestCode().substring(5));		// convert code to our internal 4-digit format
				testResults.add(testResult);
			} else if (labResult.getTestCode().startsWith("TOXIC") && labResult.getTestCode().substring(6,7).equals("1")) {		// Only TOXIC01XX tests
				TestResult testResult = mapLabTestResult(labResult);
				testResult.setTestCode("TOX00" + testResult.getTestCode().substring(7));		// convert code to our internal TOX00xx format
				testResults.add(testResult);
			}
		}
		return testResults;
// JOE: Cannot use this simple logic any longer:		
//		return labResults.stream().filter(result -> result.getTestCode().startsWith("HEM") ||
//						result.getTestCode().startsWith("CHEM") || 
//						result.getTestCode().startsWith("GEN") ||
//						result.getTestCode().startsWith("MICRO"))
//				.map(this::mapLabTestResult)
//				.collect(Collectors.toList());
	}
	
	private TestResult mapLabTestResult(LabTestResult source) {
		TestResult result = new TestResult();
		result.setTestCode(source.getTestCode());
		result.setTestName(source.getTestName());
		result.setDatePerformed(source.getDatePerformed());
		result.setResultValue(source.getValue());
		result.setResultUnits(source.getUnits());
		if (source.getLowNormalHigh().equalsIgnoreCase("LOW")) {
			result.setLow(true);
		} else if (source.getLowNormalHigh().equalsIgnoreCase("HIGH")) {
			result.setHigh(true);
		}
		return result;
	}
	
	private void savePatientTestResults(Patient patient, List<TestResult> testResults) {
		patientService.updatePatientResults(patient, testResults);
	}
}

package com.chrislaforetsoftware.kata.repository.lab;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.chrislaforetsoftware.kata.domain.LabTestResult;
import com.chrislaforetsoftware.kata.repository.ExternalLabRepository;

@Repository
public class ExternalLabRepositoryImpl implements ExternalLabRepository {

	private static final String labURL = "https://customer.mymedicallabs.com/results";
	
	@Override
	public List<LabTestResult> getResultsByAccessionId(String accessionId) {
	    RestTemplate restTemplate = new RestTemplate();
	    return Arrays.asList(restTemplate.getForObject(labURL + "" + accessionId, LabTestResult[].class));
	}
}

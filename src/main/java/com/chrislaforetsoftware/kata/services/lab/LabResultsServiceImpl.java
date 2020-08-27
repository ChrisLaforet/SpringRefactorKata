package com.chrislaforetsoftware.kata.services.lab;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chrislaforetsoftware.kata.domain.LabTestResult;
import com.chrislaforetsoftware.kata.repository.ExternalLabRepository;
import com.chrislaforetsoftware.kata.services.LabResultsService;

@Service
public class LabResultsServiceImpl implements LabResultsService {
	
	@Autowired
	ExternalLabRepository repository;
	
	@Override
	public List<LabTestResult> getLabResultsFor(String accessionId) {
		return repository.getResultsByAccessionId(accessionId);
	}
}

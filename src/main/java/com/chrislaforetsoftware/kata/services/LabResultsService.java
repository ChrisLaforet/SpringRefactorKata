package com.chrislaforetsoftware.kata.services;

import java.util.List;

import com.chrislaforetsoftware.kata.domain.LabTestResult;

public interface LabResultsService {

	List<LabTestResult> getLabResultsFor(String accessionId);
}
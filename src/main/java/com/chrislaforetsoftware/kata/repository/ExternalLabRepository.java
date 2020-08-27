package com.chrislaforetsoftware.kata.repository;

import java.util.List;

import com.chrislaforetsoftware.kata.domain.LabTestResult;

public interface ExternalLabRepository {
	List<LabTestResult> getResultsByAccessionId(String accessionId);
}
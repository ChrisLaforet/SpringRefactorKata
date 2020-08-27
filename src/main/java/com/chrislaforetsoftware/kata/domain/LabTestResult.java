package com.chrislaforetsoftware.kata.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabTestResult {
	private String recordId;
	private String accessionId;
	private String specimenNumber;
	private String testCode;
	private String testName;
	private Date dateCollected;
	private Date dateReceived;
	private Date datePerformed;
	private Date dateReported;
	private String value;
	private String units;
	private String lowNormalHigh;
}


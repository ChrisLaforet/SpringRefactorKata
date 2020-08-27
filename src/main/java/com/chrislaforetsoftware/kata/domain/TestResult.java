package com.chrislaforetsoftware.kata.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResult {
	private String testCode;
	private String testName;
	private Date datePerformed;
	private String resultValue;
	private String resultUnits;
	private boolean isLow;
	private boolean isHigh;
}

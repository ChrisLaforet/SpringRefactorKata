package com.chrislaforetsoftware.kata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chrislaforetsoftware.kata.command.Command;
import com.chrislaforetsoftware.kata.dto.PatientRequest;

@RestController
public class PatientResultController {

	@Autowired
	Command<PatientRequest, Void> resultHandler;
	
	@PostMapping("/loadLabResults")
	public void loadResultsFromLab(@RequestBody PatientRequest patientRequest) {
		resultHandler.handle(patientRequest);
	}
}

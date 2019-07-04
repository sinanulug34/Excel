package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.domain.LoginType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Step;
import com.example.demo.domain.TestCase;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TestResultsReader {
	TestResultRepository repository;
	ObjectMapper mapper;
	
	String base;

	public TestResultsReader(TestResultRepository repository, @Value("${apppath}") String base) {
		super();
		this.repository = repository;
		this.mapper = new ObjectMapper();
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.base = base;
	}
	
	public void read() {
		
		try {
			Files.list(Paths.get(base + "report/")).forEach(this::readFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFile(Path filePath) {
		try {
			TestCase test = mapper.readValue(filePath.toFile(), TestCase.class);
			if(!test.getStatus().equalsIgnoreCase("passed")) {
				Optional<Step> firstFail = test.getTestStage().getSteps().stream().filter(s -> !s.getStatus().equalsIgnoreCase("passed")).findFirst();
				if(firstFail.isPresent()) {
					test.setFailingStep(firstFail.get().getName());
				}
			}
			String mergedSteps = test.getTestStage().getSteps().stream()
					.filter(s -> !s.getName().equalsIgnoreCase("before"))
					.filter(s -> !s.getName().equalsIgnoreCase("after"))
					.map(s -> s.getName()).collect(Collectors.joining("\r\n"));
			test.setMergedSteps(mergedSteps);
			test.setLoginType(LoginType.getTypeFromJson(mergedSteps));
			String scenarioId = test.getScenarioId() + test.getLoginType();
			scenarioId = scenarioId.replaceAll("\\s+","");
			repository.addTest(scenarioId, test);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

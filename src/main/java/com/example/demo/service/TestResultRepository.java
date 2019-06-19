package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.domain.TestCase;

@Service
public class TestResultRepository {
	
	private Map<String, TestCase> tests = new HashMap<>();
	
	public void addTest(String scenarioId, TestCase test) {
		tests.put(scenarioId, test);
	}
	
	public TestCase getTest(String scenarioId) {
		return tests.get(scenarioId);
	}

}

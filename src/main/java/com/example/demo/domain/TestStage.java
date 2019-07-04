package com.example.demo.domain;

import java.util.List;

public class TestStage {
	
	private String status;
	
	private List<Step> steps;

	public TestStage() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	@Override
	public String toString() {
		return "TestStage [status=" + status + ", steps=" + steps + "]";
	}

}

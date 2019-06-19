package com.example.demo.domain;

public class Step {
	
	private String name;
	
	private String status;

	public Step() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Step [name=" + name + ", status=" + status + "]";
	}
	
	

}

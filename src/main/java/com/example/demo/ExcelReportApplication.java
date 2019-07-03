package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.service.ExcelService;
import com.example.demo.service.JenkinsService;
import com.example.demo.service.TestResultsReader;

@SpringBootApplication
public class ExcelReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelReportApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(TestResultsReader reader, ExcelService excelService, JenkinsService jenkins) {
		return (args) -> {
			jenkins.loadReports();
			reader.read();
			excelService.createReport();
			jenkins.cleanup();
		};
	}
	
}

package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JenkinsService {

	private static final Logger log = LoggerFactory.getLogger(JenkinsService.class);

	String report;

	String base;

	HttpClient client;

	public JenkinsService(@Value("${apppath}") String baseFolder, @Value("${report}") String reportId) {
		client = HttpClientBuilder.create().build();

		this.base = baseFolder;
		this.report = reportId;

		File f = new File(base + "report");
		if (!(f.exists() && f.isDirectory()))
			f.mkdir();
	}

	public void loadReports() {
		// http://10.254.46.80:8080/job/TDSS/job/BDD%20-%20Mobil%20-%20Fizy%20-%20DeviceFarm%20-%20Android%20-%20NonGuest%20-%20Regression/14/execution/node/3/ws/tools/allure-reports/data/test-cases/*zip*/test-cases.zip

		String fizyPath = "http://10.254.46.80:8080/job/TDSS/job/BDD_Mobil_Fizy_DeviceFarm_Android_NonGuest_Regression_Rerun/19/execution/node/3/ws/tools/allure-reports/data/test-cases/*zip*/test-cases.zip";

		System.err.println(fizyPath);
		HttpGet request = new HttpGet(fizyPath);

		try {
			HttpResponse response = client.execute(request);
			InputStream is = response.getEntity().getContent();
			log.info("latest report downloaded. now extracting...");
			File destDir = new File(base + "report");
			byte[] buffer = new byte[1024];
			ZipInputStream zis = new ZipInputStream(is);
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				if (!zipEntry.getName().endsWith(".json")) {
					log.info("skippin non json file: {}", zipEntry.getName());
					zipEntry = zis.getNextEntry();
					continue;
				}
				File newFile = newFile(destDir, zipEntry);
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		String fileName = zipEntry.getName().split("/")[1];
		File destFile = new File(destinationDir, fileName);
		log.info("Extracting file to {}", destFile.getAbsolutePath());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + fileName);
		}

		return destFile;
	}

	public void cleanup() {
		log.info("removing allure json outputs");
		deleteDirectory(new File(base + "report"));
	}

	boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

}

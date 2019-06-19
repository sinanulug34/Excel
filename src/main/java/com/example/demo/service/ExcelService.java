package com.example.demo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.domain.SuiteSummary;
import com.example.demo.domain.TestCase;

@Component
public class ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelService.class);

    @Value("${apppath}")
    String base;

    @Autowired
    TestResultRepository repository;

    public void createReport() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
        String output = "FizyMiniRegresyon_" + now.format(formatter) + ".xlsx";

        try (FileInputStream file = new FileInputStream(new File(base + "base.xlsx"));
             Workbook workbook = WorkbookFactory.create(file);
             FileOutputStream outputStream = new FileOutputStream(base + output);) {

            List<String> sheets = new ArrayList<>();
            workbook.sheetIterator().forEachRemaining(s -> sheets.add(s.getSheetName()));

            CellStyle defaultCellStyle = workbook.createCellStyle();
            defaultCellStyle.setWrapText(true);
            defaultCellStyle.setAlignment(HorizontalAlignment.LEFT);
            defaultCellStyle.setVerticalAlignment(VerticalAlignment.TOP);
            defaultCellStyle.setBorderBottom(BorderStyle.THIN);
            defaultCellStyle.setBorderLeft(BorderStyle.THIN);
            defaultCellStyle.setBorderRight(BorderStyle.THIN);
            defaultCellStyle.setBorderTop(BorderStyle.THIN);


            Map<String, SuiteSummary> sheetSummary = new LinkedHashMap<>();
            for (String sheetName : sheets) {
                if (sheetName.equalsIgnoreCase("total")) {
                    continue;
                }
                System.err.println(sheetName);
                Sheet sheet = workbook.getSheet(sheetName);
                Iterator<Row> rowIterator = sheet.iterator();
                int counter = -1;
                while (rowIterator.hasNext()) {
                    counter++;
                    Row currentRow = rowIterator.next();
                    if (counter == 0) // skip top row
                        continue;

                    Cell scenarioCell = currentRow.getCell(3);

                    String scenarioIds = scenarioCell.getStringCellValue();
                    if (scenarioIds.trim().isEmpty()) // skip empty scenario id cells
                        continue;

                    // scenarios may be combined with -
                    String[] scenarios = scenarioIds.split("-");

                    // cells to be updated
                    Cell cellMergedSteps = currentRow.getCell(8);
                    Cell cellResult = currentRow.getCell(9);
                    Cell cellStep = currentRow.getCell(10);

                    // make sure the scenario steps and result cells style is correct
                    cellMergedSteps.setCellStyle(defaultCellStyle);
                    cellStep.setCellStyle(defaultCellStyle);

                    // default values
                    String mergedSteps = "";
                    String result = "";
                    String step = "";

                    for (String scenarioId : scenarios) {
                        TestCase test = repository.getTest(scenarioId);

                        SuiteSummary currentSummary = sheetSummary.getOrDefault(sheetName, new SuiteSummary());
                        if (test == null) {
                            log.info("Test {} not found", scenarioId);
                            sheetSummary.put(sheetName, currentSummary);
                            continue;
                        }
                        if(test.getStatus().equalsIgnoreCase("passed")) {
                            currentSummary.increateOk();
                        } else if(test.getStatus().equalsIgnoreCase("failed")) {
                            currentSummary.increateNok();
                        } else if(test.getStatus().equalsIgnoreCase("skipped")) {
                            currentSummary.increateSkipped();
                        } else{
                            currentSummary.increateNotRun();
                        }
                        sheetSummary.put(sheetName, currentSummary);

                        mergedSteps += test.getMergedSteps() + "\r\n";
                        result += test.getStatus() + "\r\n";
                        if(test.getFailingStep() != null)
                            step += test.getFailingStep() + "\r\n";

                    }

                    cellMergedSteps.setCellValue(mergedSteps);
                    cellResult.setCellValue(result);
                    cellStep.setCellValue(step);
                }
            }

            Sheet totalSheet = workbook.getSheet("total");
            int index = 1;
            SuiteSummary totalSum = new SuiteSummary();
            int totalCaseCount = 0;
            for(Entry<String, SuiteSummary> entries: sheetSummary.entrySet()) {
                System.err.println(entries.getKey());
                Row currentRow = totalSheet.getRow(index);
                Cell module = currentRow.getCell(0);
                Cell caseCount = currentRow.getCell(1);
                Cell caseNok = currentRow.getCell(2);
                Cell caseOk = currentRow.getCell(3);
                Cell caseSkipped = currentRow.getCell(4);
                Cell caseNotRun = currentRow.getCell(5);
                SuiteSummary summary = entries.getValue();
                module.setCellValue(entries.getKey());

                caseCount.setCellValue(String.valueOf(summary.getTotalCount()));
                totalCaseCount += summary.getTotalCount();

                caseNok.setCellValue(String.valueOf(summary.getNok()));
                totalSum.setNok(totalSum.getNok() + summary.getNok());

                caseOk.setCellValue(String.valueOf(summary.getOk()));
                totalSum.setOk(totalSum.getOk() + summary.getOk());

//				caseSkipped.setCellValue(String.valueOf(summary.getSkipped()));
//				totalSum.setSkipped(totalSum.getSkipped() + summary.getSkipped());

//				caseNotRun.setCellValue(String.valueOf(summary.getNotRun()));
//				totalSum.setNotRun(totalSum.getNotRun() + summary.getNotRun());

                index++;
            }

            Row currentRow = totalSheet.getRow(index);
            Cell caseCount = currentRow.getCell(1);
            Cell caseNok = currentRow.getCell(2);
            Cell caseOk = currentRow.getCell(3);
            Cell caseSkipped = currentRow.getCell(4);
            Cell caseNotRun = currentRow.getCell(5);

            caseCount.setCellValue(String.valueOf(totalCaseCount));
            caseNok.setCellValue(String.valueOf(totalSum.getNok()));
            caseOk.setCellValue(String.valueOf(totalSum.getOk()));
            caseSkipped.setCellValue(String.valueOf(totalSum.getSkipped()));
            caseNotRun.setCellValue(String.valueOf(totalSum.getNotRun()));
            workbook.write(outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

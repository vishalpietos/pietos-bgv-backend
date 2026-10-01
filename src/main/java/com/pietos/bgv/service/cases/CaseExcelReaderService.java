package com.pietos.bgv.service.cases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pietos.bgv.dto.imports.cases.CaseImportRow;

@Service
public class CaseExcelReaderService {

    public List<CaseImportRow> readExcel(
            MultipartFile file) throws IOException {

        List<CaseImportRow> rows = new ArrayList<>();

        try (
                Workbook workbook =
                        WorkbookFactory.create(
                                file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println(
                    "Sheet Name: " + sheet.getSheetName()
            );

            System.out.println(
                    "Last Row Index: " + sheet.getLastRowNum()
            );

            // Row 1 = Warning
            // Row 2 = Headers
            // Row 3 onwards = Data

            for (int rowIndex = 1;
                 rowIndex <= sheet.getLastRowNum();
                 rowIndex++) {

                Row row = sheet.getRow(rowIndex);

                System.out.println(
                        "Checking Excel Row: " + (rowIndex + 1)
                );

                if (row == null) {

                    System.out.println(
                            "Row " + (rowIndex + 1) + " = NULL"
                    );

                    continue;
                }

                if (isEmptyRow(row)) {

                    System.out.println(
                            "Row " + (rowIndex + 1) + " = EMPTY"
                    );

                    continue;
                }

                System.out.println(
                        "Row " + (rowIndex + 1) + " = HAS DATA"
                );

                CaseImportRow importRow =
                        new CaseImportRow();

                importRow.setRowNumber(
                        rowIndex + 1
                );

                importRow.setClientCode(
                        getCellValue(row.getCell(0))
                );

                importRow.setClientLocation(
                        getCellValue(row.getCell(1))
                );

                importRow.setCaseReceivedDate(
                        getCellValue(row.getCell(2))
                );

                importRow.setEmployment(
                        getCellValue(row.getCell(3))
                );

                importRow.setFirstName(
                        getCellValue(row.getCell(4))
                );

                importRow.setMiddleName(
                        getCellValue(row.getCell(5))
                );

                importRow.setLastName(
                        getCellValue(row.getCell(6))
                );

                importRow.setDateOfBirth(
                        getCellValue(row.getCell(7))
                );

                importRow.setFatherName(
                        getCellValue(row.getCell(8))
                );

                importRow.setClientEmployeeId(
                        getCellValue(row.getCell(9))
                );

                importRow.setDateOfJoining(
                        getCellValue(row.getCell(10))
                );

                importRow.setMobileNumber(
                        getCellValue(row.getCell(11))
                );

                importRow.setEmail(
                        getCellValue(row.getCell(12))
                );

                importRow.setGender(
                        getCellValue(row.getCell(13))
                );

                System.out.println(
                        "Client Code: "
                                + importRow.getClientCode()
                );

                System.out.println(
                        "Client Location: "
                                + importRow.getClientLocation()
                );

                System.out.println(
                        "First Name: "
                                + importRow.getFirstName()
                );

                System.out.println(
                        "Email: "
                                + importRow.getEmail()
                );

                rows.add(importRow);
            }
        }

        System.out.println(
                "Total Excel Rows Read: " + rows.size()
        );

        return rows;
    }


    private String getCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        DataFormatter formatter =
                new DataFormatter();

        String value =
                formatter.formatCellValue(cell);

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }


    private boolean isEmptyRow(Row row) {

        DataFormatter formatter =
                new DataFormatter();

        for (int i = 0; i < 14; i++) {

            Cell cell =
                    row.getCell(
                            i,
                            Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
                    );

            if (cell != null) {

                String value =
                        formatter.formatCellValue(cell);

                if (value != null
                        && !value.trim().isEmpty()) {

                    return false;
                }
            }
        }

        return true;
    }
}
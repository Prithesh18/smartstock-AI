package com.smartstock.service;

import com.smartstock.model.LowStockAlertResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelReportService {

    private final LowStockAlertService lowStockAlertService;

    public ExcelReportService(
            LowStockAlertService lowStockAlertService) {
        this.lowStockAlertService = lowStockAlertService;
    }

    public String generateReorderReport() throws IOException {

        List<LowStockAlertResult> alerts =
                lowStockAlertService.getLowStockAlerts();

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Reorder Report");

        // Header
        String[] headers = {
                "Product ID",
                "Product Name",
                "Current Stock",
                "Reorder Point",
                "Required Stock",
                "Lead Time (Days)",
                "Distributor",
                "Distributor Email",
                "Status"
        };

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            cell.setCellStyle(style);
        }

        // Data rows
        int rowNumber = 1;

        for (LowStockAlertResult alert : alerts) {

            Row row = sheet.createRow(rowNumber++);

            row.createCell(0).setCellValue(
                    alert.getProductId());

            row.createCell(1).setCellValue(
                    alert.getProductName());

            row.createCell(2).setCellValue(
                    alert.getCurrentStock());

            row.createCell(3).setCellValue(
                    alert.getReorderPoint());

            row.createCell(4).setCellValue(
                    alert.getRequiredStock());

            row.createCell(5).setCellValue(
                    alert.getLeadTimeDays());

            row.createCell(6).setCellValue(
                    alert.getDistributorName());

            row.createCell(7).setCellValue(
                    alert.getDistributorEmail());

            row.createCell(8).setCellValue(
                    alert.getStatus());
        }

        // Automatically adjust column width
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        String filePath =
                "SmartStock_Reorder_Report.xlsx";

        try (FileOutputStream outputStream =
                     new FileOutputStream(filePath)) {

            workbook.write(outputStream);
        }

        workbook.close();

        return filePath;
    }
}
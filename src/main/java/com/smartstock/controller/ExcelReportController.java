package com.smartstock.controller;

import com.smartstock.service.ExcelReportService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;

@RestController
@RequestMapping("/api/reports")

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://smartstock-n7m0kanh4-prithesh.vercel.app",
        "https://smartstock-ai-five.vercel.app"
})

public class ExcelReportController {

    private final ExcelReportService excelReportService;

    public ExcelReportController(
            ExcelReportService excelReportService) {
        this.excelReportService = excelReportService;
    }

    @GetMapping("/reorder")
    public ResponseEntity<Resource> generateReorderReport()
            throws Exception {

        String filePath =
                excelReportService.generateReorderReport();

        File file = new File(filePath);

        Resource resource =
                new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"SmartStock_Reorder_Report.xlsx\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .contentLength(file.length())
                .body(resource);
    }
}
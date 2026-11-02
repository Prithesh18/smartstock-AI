package com.smartstock.controller;

import com.smartstock.service.CsvService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/csv")
@CrossOrigin(origins = "http://localhost:5173")
public class CsvController {

    private final CsvService csvService;
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(
            @RequestParam("file") MultipartFile file) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Please select a CSV file");
            }

            String fileName = file.getOriginalFilename();

            if (fileName == null ||
                    !fileName.toLowerCase().endsWith(".csv")) {

                return ResponseEntity
                        .badRequest()
                        .body("Only CSV files are allowed");
            }

            // Upload CSV into MySQL
            int count = csvService.uploadCsv(file);

            return ResponseEntity.ok(
                    count + " sales records uploaded and preprocessed successfully"
            );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Failed to process CSV: " + e.getMessage());
        }
    }
}
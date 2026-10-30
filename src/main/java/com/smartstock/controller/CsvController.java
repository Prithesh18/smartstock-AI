package com.smartstock.controller;

import com.smartstock.service.CsvService;
import com.smartstock.service.PreprocessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/csv")

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://smartstock-n7m0kanh4-prithesh.vercel.app",
        "https://smartstock-ai-five.vercel.app"
})

public class CsvController {

    private final CsvService csvService;
    private final PreprocessingService preprocessingService;

    public CsvController(
            CsvService csvService,
            PreprocessingService preprocessingService) {

        this.csvService = csvService;
        this.preprocessingService = preprocessingService;
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

            // Automatically preprocess uploaded data
            String preprocessingResult =
                    preprocessingService.preprocessData();

            return ResponseEntity.ok(
                    count +
                            " sales records uploaded successfully. " +
                            preprocessingResult
            );

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Failed to process CSV: " + e.getMessage());
        }
    }
}
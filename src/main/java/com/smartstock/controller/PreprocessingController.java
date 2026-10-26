package com.smartstock.controller;

import com.smartstock.service.PreprocessingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preprocess")
public class PreprocessingController {

    private final PreprocessingService preprocessingService;

    public PreprocessingController(PreprocessingService preprocessingService) {
        this.preprocessingService = preprocessingService;
    }

    @PostMapping
    public String preprocess() {
        return preprocessingService.preprocessData();
    }
}
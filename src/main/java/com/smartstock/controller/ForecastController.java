package com.smartstock.controller;

import com.smartstock.model.ForecastResult;
import com.smartstock.service.ForecastService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forecast")
@CrossOrigin(origins = "http://localhost:5173")
public class ForecastController {

    private final ForecastService forecastingService;

    public ForecastController(
            ForecastService forecastingService) {

        this.forecastingService = forecastingService;
    }

    @GetMapping("/30-days")
    public List<ForecastResult> forecast30Days() {

        return forecastingService.forecast30Days();
    }
}
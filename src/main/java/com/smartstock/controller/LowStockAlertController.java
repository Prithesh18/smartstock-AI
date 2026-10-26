package com.smartstock.controller;

import com.smartstock.model.LowStockAlertResult;
import com.smartstock.service.LowStockAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:5173")
public class LowStockAlertController {

    private final LowStockAlertService lowStockAlertService;

    public LowStockAlertController(
            LowStockAlertService lowStockAlertService) {
        this.lowStockAlertService = lowStockAlertService;
    }

    @GetMapping("/low-stock")
    public List<LowStockAlertResult> getLowStockAlerts() {
        return lowStockAlertService.getLowStockAlerts();
    }
}
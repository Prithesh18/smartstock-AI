package com.smartstock.controller;

import com.smartstock.model.LowStockAlertResult;
import com.smartstock.service.EmailService;
import com.smartstock.service.LowStockAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:5173")
public class EmailController {

    private final EmailService emailService;
    private final LowStockAlertService lowStockAlertService;

    public EmailController(
            EmailService emailService,
            LowStockAlertService lowStockAlertService) {

        this.emailService = emailService;
        this.lowStockAlertService = lowStockAlertService;
    }

    @PostMapping("/low-stock")
    public ResponseEntity<String> sendLowStockEmails() {

        List<LowStockAlertResult> alerts =
                lowStockAlertService.getLowStockAlerts();

        int sentCount = 0;

        for (LowStockAlertResult alert : alerts) {
            emailService.sendLowStockAlert(alert);
            sentCount++;
        }

        return ResponseEntity
                .ok(sentCount + " low-stock alert emails sent successfully");
    }
}
package com.smartstock.controller;

import com.smartstock.model.Sales;
import com.smartstock.service.CsvService;
import com.smartstock.service.SalesService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;
    private final CsvService csvService;

    public SalesController(
            SalesService salesService,
            CsvService csvService) {

        this.salesService = salesService;
        this.csvService = csvService;
    }

    @PostMapping
    public Sales addSales(@RequestBody Sales sales) {
        return salesService.addSales(sales);
    }

    @GetMapping
    public List<Sales> getAllSales() {
        return salesService.getAllSales();
    }

    @PostMapping("/upload")
    public String uploadCsv(
            @RequestParam("file") MultipartFile file)
            throws Exception {

        int count = csvService.uploadCsv(file);

        return count + " sales records uploaded successfully";
    }
}
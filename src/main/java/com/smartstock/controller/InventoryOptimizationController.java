package com.smartstock.controller;

import com.smartstock.model.InventoryOptimizationResult;
import com.smartstock.service.InventoryOptimizationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryOptimizationController {

    private final InventoryOptimizationService inventoryOptimizationService;

    public InventoryOptimizationController(
            InventoryOptimizationService inventoryOptimizationService) {
        this.inventoryOptimizationService = inventoryOptimizationService;
    }

    @GetMapping("/optimization")
    public List<InventoryOptimizationResult> optimizeInventory() {
        return inventoryOptimizationService.optimizeInventory();
    }
}
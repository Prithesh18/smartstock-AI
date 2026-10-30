package com.smartstock.controller;

import com.smartstock.model.InventoryOptimizationResult;
import com.smartstock.service.InventoryOptimizationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://smartstock-n7m0kanh4-prithesh.vercel.app",
        "https://smartstock-ai-five.vercel.app"
})

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
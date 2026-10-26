package com.smartstock.service;

import com.smartstock.model.InventoryOptimizationResult;
import com.smartstock.model.LowStockAlertResult;
import com.smartstock.model.Sales;
import com.smartstock.repository.SalesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LowStockAlertService {

    private final SalesRepository salesRepository;
    private final InventoryOptimizationService inventoryOptimizationService;

    public LowStockAlertService(
            SalesRepository salesRepository,
            InventoryOptimizationService inventoryOptimizationService) {

        this.salesRepository = salesRepository;
        this.inventoryOptimizationService = inventoryOptimizationService;
    }

    public List<LowStockAlertResult> getLowStockAlerts() {

        List<InventoryOptimizationResult> inventoryResults =
                inventoryOptimizationService.optimizeInventory();

        List<Sales> salesList =
                salesRepository.findAllByOrderByProductIdAscDateAsc();

        Map<String, Sales> latestProductData = new HashMap<>();

        // Get the latest record for every product
        for (Sales sales : salesList) {
            latestProductData.put(sales.getProductId(), sales);
        }

        List<LowStockAlertResult> alerts = new ArrayList<>();

        for (InventoryOptimizationResult inventory : inventoryResults) {

            if (!"LOW STOCK".equals(inventory.getStatus())) {
                continue;
            }

            Sales latest =
                    latestProductData.get(inventory.getProductId());

            if (latest == null) {
                continue;
            }

            LowStockAlertResult alert =
                    new LowStockAlertResult(
                            inventory.getProductId(),
                            inventory.getProductName(),
                            inventory.getCurrentStock(),
                            inventory.getReorderPoint(),
                            inventory.getRequiredStock(),
                            latest.getLeadTimeDays(),
                            latest.getDistributorName(),
                            latest.getDistributorEmail(),
                            latest.getShopOwnerEmail(),
                            inventory.getStatus()
                    );

            alerts.add(alert);
        }

        return alerts;
    }
}
package com.smartstock.service;

import com.smartstock.model.InventoryOptimizationResult;
import com.smartstock.model.Sales;
import com.smartstock.repository.SalesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryOptimizationService {

    private final SalesRepository salesRepository;

    public InventoryOptimizationService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<InventoryOptimizationResult> optimizeInventory() {

        List<Sales> salesList =
                salesRepository.findAllByOrderByProductIdAscDateAsc();

        Map<String, List<Sales>> groups = new HashMap<>();

        // Group records by product
        for (Sales sales : salesList) {
            groups.computeIfAbsent(
                    sales.getProductId(),
                    key -> new ArrayList<>()
            ).add(sales);
        }

        List<InventoryOptimizationResult> results = new ArrayList<>();

        for (List<Sales> group : groups.values()) {

            if (group.isEmpty()) {
                continue;
            }

            Sales latest = group.get(group.size() - 1);

            // Calculate average daily demand from recent 7 days
            // Calculate demand using recent 7-day and 30-day averages
            int start7 = Math.max(0, group.size() - 7);
            int start30 = Math.max(0, group.size() - 30);

            double total7 = 0;
            double total30 = 0;

            int count7 = 0;
            int count30 = 0;

            for (int i = start7; i < group.size(); i++) {
                total7 += group.get(i).getSalesQuantity();
                count7++;
            }

            for (int i = start30; i < group.size(); i++) {
                total30 += group.get(i).getSalesQuantity();
                count30++;
            }

            double recent7DayAverage =
                    count7 > 0 ? total7 / count7 : 0;

            double recent30DayAverage =
                    count30 > 0 ? total30 / count30 : 0;

// Same demand calculation used by ForecastService
            double averageDailyDemand =
                    (recent7DayAverage * 0.7)
                            + (recent30DayAverage * 0.3);

            // Lead time from dataset
            int leadTimeDays = latest.getLeadTimeDays();

            // Demand during lead time
            double leadTimeDemand =
                    averageDailyDemand * leadTimeDays;

            // Safety stock
            double safetyStock =
                    averageDailyDemand
                            * Math.sqrt(leadTimeDays)
                            * 0.45;

            // Reorder point
            double reorderPoint =
                    leadTimeDemand + safetyStock;

            // 30-day predicted demand
            double predicted30DayDemand =
                    averageDailyDemand * 30;

            // Required stock
            double requiredStock =
                    Math.max(
                            0,
                            predicted30DayDemand
                                    + safetyStock
                                    - latest.getStockAvailable()
                    );

            // Inventory status
            String status;

            if (latest.getStockAvailable() < reorderPoint) {
                status = "LOW STOCK";
            } else {
                status = "SUFFICIENT";
            }

            InventoryOptimizationResult result =
                    new InventoryOptimizationResult(
                            latest.getProductId(),
                            latest.getProductName(),
                            averageDailyDemand,
                            leadTimeDemand,
                            safetyStock,
                            reorderPoint,
                            latest.getStockAvailable(),
                            requiredStock,
                            status
                    );

            results.add(result);
        }

        return results;
    }
}
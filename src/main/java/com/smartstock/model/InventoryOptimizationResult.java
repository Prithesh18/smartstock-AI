package com.smartstock.model;

public class InventoryOptimizationResult {

    private String productId;
    private String productName;
    private Double averageDailyDemand;
    private Double leadTimeDemand;
    private Double safetyStock;
    private Double reorderPoint;
    private Integer currentStock;
    private Double requiredStock;
    private String status;

    public InventoryOptimizationResult() {
    }

    public InventoryOptimizationResult(
            String productId,
            String productName,
            Double averageDailyDemand,
            Double leadTimeDemand,
            Double safetyStock,
            Double reorderPoint,
            Integer currentStock,
            Double requiredStock,
            String status) {

        this.productId = productId;
        this.productName = productName;
        this.averageDailyDemand = averageDailyDemand;
        this.leadTimeDemand = leadTimeDemand;
        this.safetyStock = safetyStock;
        this.reorderPoint = reorderPoint;
        this.currentStock = currentStock;
        this.requiredStock = requiredStock;
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Double getAverageDailyDemand() {
        return averageDailyDemand;
    }

    public Double getLeadTimeDemand() {
        return leadTimeDemand;
    }

    public Double getSafetyStock() {
        return safetyStock;
    }

    public Double getReorderPoint() {
        return reorderPoint;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public Double getRequiredStock() {
        return requiredStock;
    }

    public String getStatus() {
        return status;
    }
}
package com.smartstock.model;

public class LowStockAlertResult {

    private String productId;
    private String productName;
    private Integer currentStock;
    private Double reorderPoint;
    private Double requiredStock;
    private Integer leadTimeDays;
    private String distributorName;
    private String distributorEmail;
    private String shopOwnerEmail;
    private String status;

    public LowStockAlertResult() {
    }

    public LowStockAlertResult(
            String productId,
            String productName,
            Integer currentStock,
            Double reorderPoint,
            Double requiredStock,
            Integer leadTimeDays,
            String distributorName,
            String distributorEmail,
            String shopOwnerEmail,
            String status) {

        this.productId = productId;
        this.productName = productName;
        this.currentStock = currentStock;
        this.reorderPoint = reorderPoint;
        this.requiredStock = requiredStock;
        this.leadTimeDays = leadTimeDays;
        this.distributorName = distributorName;
        this.distributorEmail = distributorEmail;
        this.shopOwnerEmail = shopOwnerEmail;
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public Double getReorderPoint() {
        return reorderPoint;
    }

    public Double getRequiredStock() {
        return requiredStock;
    }

    public Integer getLeadTimeDays() {
        return leadTimeDays;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public String getDistributorEmail() {
        return distributorEmail;
    }

    public String getShopOwnerEmail() {
        return shopOwnerEmail;
    }

    public String getStatus() {
        return status;
    }
}
package com.smartstock.model;

public class ForecastResult {

    private String productId;
    private String productName;
    private Double averageDailyDemand;
    private Double predicted30DayDemand;
    private Integer currentStock;
    private Double mae;
    private Double rmse;
    private String demandTrend;
    private Double trendPercentage;

    public ForecastResult() {
    }

    public ForecastResult(
            String productId,
            String productName,
            Double averageDailyDemand,
            Double predicted30DayDemand,
            Integer currentStock) {

        this.productId = productId;
        this.productName = productName;
        this.averageDailyDemand = averageDailyDemand;
        this.predicted30DayDemand = predicted30DayDemand;
        this.currentStock = currentStock;
    }
    public String getDemandTrend() {
        return demandTrend;
    }

    public void setDemandTrend(String demandTrend) {
        this.demandTrend = demandTrend;
    }

    public Double getTrendPercentage() {
        return trendPercentage;
    }

    public void setTrendPercentage(Double trendPercentage) {
        this.trendPercentage = trendPercentage;
    }
    public Double getMae() {
        return mae;
    }

    public void setMae(Double mae) {
        this.mae = mae;
    }

    public Double getRmse() {
        return rmse;
    }

    public void setRmse(Double rmse) {
        this.rmse = rmse;
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

    public Double getPredicted30DayDemand() {
        return predicted30DayDemand;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }
}
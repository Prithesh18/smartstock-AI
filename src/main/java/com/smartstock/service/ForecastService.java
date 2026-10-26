package com.smartstock.service;

import com.smartstock.model.ForecastResult;
import com.smartstock.model.Sales;
import com.smartstock.repository.SalesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ForecastService {

    private final SalesRepository salesRepository;

    public ForecastService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    private double calculateMAE(List<Sales> sales) {

        if (sales.size() < 2) {
            return 0.0;
        }

        double totalError = 0;

        for (int i = 1; i < sales.size(); i++) {

            double actual = sales.get(i).getSalesQuantity();

            double predicted = sales.get(i - 1).getSalesQuantity();

            totalError += Math.abs(actual - predicted);
        }

        return totalError / (sales.size() - 1);
    }
    private double calculateRMSE(List<Sales> sales) {

        if (sales.size() < 2) {
            return 0.0;
        }

        double totalSquaredError = 0;

        for (int i = 1; i < sales.size(); i++) {

            double actual = sales.get(i).getSalesQuantity();

            double predicted = sales.get(i - 1).getSalesQuantity();

            double error = actual - predicted;

            totalSquaredError += error * error;
        }

        return Math.sqrt(
                totalSquaredError / (sales.size() - 1)
        );
    }
    public List<ForecastResult> forecast30Days() {

        List<Sales> salesList =
                salesRepository.findAllByOrderByProductIdAscDateAsc();

        // Group sales by Product
        Map<String, List<Sales>> groups = new HashMap<>();

        for (Sales sales : salesList) {

            String key = sales.getProductId();

            groups
                    .computeIfAbsent(key, k -> new ArrayList<>())
                    .add(sales);
        }

        List<ForecastResult> results = new ArrayList<>();

        // Forecast for each product
        for (List<Sales> group : groups.values()) {

            if (group.isEmpty()) {
                continue;
            }

            Sales latest = group.get(group.size() - 1);

            // Calculate average demand from latest 7 records
            // Calculate demand using recent 7-day and 30-day averages
            double recent7DayAverage = 0;
            double recent30DayAverage = 0;

            int count7 = 0;
            int count30 = 0;

            double total7 = 0;
            double total30 = 0;

            int start7 = Math.max(0, group.size() - 7);
            int start30 = Math.max(0, group.size() - 30);

            for (int i = start7; i < group.size(); i++) {

                total7 += group.get(i).getSalesQuantity();
                count7++;
            }

            for (int i = start30; i < group.size(); i++) {

                total30 += group.get(i).getSalesQuantity();
                count30++;
            }

            if (count7 > 0) {
                recent7DayAverage = total7 / count7;
            }

            if (count30 > 0) {
                recent30DayAverage = total30 / count30;
            }

// Combine recent and longer-term demand
            double averageDailyDemand =
                    (recent7DayAverage * 0.7) +
                            (recent30DayAverage * 0.3);
            double trendPercentage = 0;

            if (recent30DayAverage > 0) {
                trendPercentage =
                        ((recent7DayAverage - recent30DayAverage)
                                / recent30DayAverage) * 100;
            }

            String demandTrend;

            if (trendPercentage > 10) {
                demandTrend = "INCREASING";
            } else if (trendPercentage < -10) {
                demandTrend = "DECREASING";
            } else {
                demandTrend = "STABLE";
            }
            // Predict next 30 days
            double predicted30DayDemand =
                    averageDailyDemand * 30;
            double mae = calculateMAE(group);
            double rmse = calculateRMSE(group);

            ForecastResult result =
                    new ForecastResult(
                            latest.getProductId(),
                            latest.getProductName(),
                            averageDailyDemand,
                            predicted30DayDemand,
                            latest.getStockAvailable()
                    );

            result.setMae(mae);
            result.setRmse(rmse);
            result.setDemandTrend(demandTrend);
            result.setTrendPercentage(trendPercentage);

            results.add(result);
        }


        return results;
    }
}
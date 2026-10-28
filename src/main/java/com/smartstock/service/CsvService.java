package com.smartstock.service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstock.model.Sales;
import com.smartstock.repository.SalesRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class CsvService {

    private final SalesRepository salesRepository;

    public CsvService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public int uploadCsv(MultipartFile file) throws Exception {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        );

        CSVParser csvParser = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader);

        // 1. Check required columns
        String[] requiredColumns = {
                "Date",
                "Product_ID",
                "Product_Name",
                "Category",
                "Sales_Quantity",
                "Stock_Available",
                "Unit_Price_INR",
                "Reorder_Level",
                "Lead_Time_Days",
                "Distributor_Name",
                "Distributor_Email",
                "Shop_Owner_Email"
        };

        for (String column : requiredColumns) {

            if (!csvParser.getHeaderMap().containsKey(column)) {
                throw new IllegalArgumentException(
                        "Missing required column: " + column
                );
            }
        }

        List<Sales> salesList = new ArrayList<>();

        int rowNumber = 1;

        // 2. Validate every row
        for (CSVRecord record : csvParser) {

            rowNumber++;

            try {

                // Date
                LocalDate date = parseDate(record.get("Date"));

                // Required text fields
                String productId = record.get("Product_ID").trim();
                String productName = record.get("Product_Name").trim();
                String category = record.get("Category").trim();

                if (productId.isEmpty()) {
                    throw new IllegalArgumentException("Product_ID cannot be empty");
                }

                if (productName.isEmpty()) {
                    throw new IllegalArgumentException("Product_Name cannot be empty");
                }

                if (category.isEmpty()) {
                    throw new IllegalArgumentException("Category cannot be empty");
                }

                // Numeric fields
                int salesQuantity =
                        Integer.parseInt(record.get("Sales_Quantity"));

                int stockAvailable =
                        Integer.parseInt(record.get("Stock_Available"));

                double unitPrice =
                        Double.parseDouble(record.get("Unit_Price_INR"));

                int reorderLevel =
                        Integer.parseInt(record.get("Reorder_Level"));

                int leadTimeDays =
                        Integer.parseInt(record.get("Lead_Time_Days"));

                // Check negative values
                if (salesQuantity < 0) {
                    throw new IllegalArgumentException(
                            "Sales_Quantity cannot be negative"
                    );
                }

                if (stockAvailable < 0) {
                    throw new IllegalArgumentException(
                            "Stock_Available cannot be negative"
                    );
                }

                if (unitPrice < 0) {
                    throw new IllegalArgumentException(
                            "Unit_Price_INR cannot be negative"
                    );
                }

                if (reorderLevel < 0) {
                    throw new IllegalArgumentException(
                            "Reorder_Level cannot be negative"
                    );
                }

                if (leadTimeDays < 0) {
                    throw new IllegalArgumentException(
                            "Lead_Time_Days cannot be negative"
                    );
                }

                // Create Sales object
                Sales sales = new Sales();

                sales.setDate(date);
                sales.setProductId(productId);
                sales.setProductName(productName);
                sales.setCategory(category);
                sales.setSalesQuantity(salesQuantity);
                sales.setStockAvailable(stockAvailable);
                sales.setUnitPriceInr(unitPrice);
                sales.setReorderLevel(reorderLevel);
                sales.setLeadTimeDays(leadTimeDays);

                sales.setDistributorName(
                        record.get("Distributor_Name").trim()
                );

                sales.setDistributorEmail(
                        record.get("Distributor_Email").trim()
                );

                sales.setShopOwnerEmail(
                        record.get("Shop_Owner_Email").trim()
                );

                salesList.add(sales);

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "Invalid number at CSV row " + rowNumber
                );

            } catch (java.time.format.DateTimeParseException e) {

                throw new IllegalArgumentException(
                        "Invalid date at CSV row " + rowNumber +
                                ". Expected format: YYYY-MM-DD"
                );
            }
        }

        // 3. Make sure CSV contains data
        if (salesList.isEmpty()) {
            throw new IllegalArgumentException(
                    "CSV file contains no data"
            );
        }
        // 4. Replace existing dataset
        salesRepository.deleteAll();

        salesRepository.saveAll(salesList);

        return salesList.size();
    }
    private LocalDate parseDate(String dateValue) {

        String date = dateValue.trim();

        String[] formats = {
                "yyyy-MM-dd",
                "dd-MM-yyyy",
                "dd/MM/yyyy",
                "yyyy/MM/dd"
        };

        for (String format : formats) {

            try {
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(format);

                return LocalDate.parse(date, formatter);

            } catch (DateTimeParseException e) {
                // Try next format
            }
        }

        throw new IllegalArgumentException(
                "Invalid date: " + date +
                        ". Supported formats: YYYY-MM-DD, DD-MM-YYYY, DD/MM/YYYY"
        );
    }
}
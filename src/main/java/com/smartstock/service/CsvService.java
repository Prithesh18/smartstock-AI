package com.smartstock.service;

import com.smartstock.model.Sales;
import com.smartstock.repository.SalesRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private static final Logger log = LoggerFactory.getLogger(CsvService.class);
    private static final int BATCH_SIZE = 500;

    private static final String INSERT_SQL = """
            INSERT INTO `sales` (
                `date`, `product_id`, `product_name`, `category`,
                `sales_quantity`, `stock_available`, `unit_price_inr`,
                `reorder_level`, `lead_time_days`, `distributor_name`,
                `distributor_email`, `shop_owner_email`, `year`, `month`,
                `day`, `day_of_week`, `is_weekend`, `sales_lag1`,
                `sales_lag7`, `sales_rolling7_mean`, `sales_rolling30_mean`
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private final SalesRepository salesRepository;
    private final PreprocessingService preprocessingService;
    private final JdbcTemplate jdbcTemplate;

    public CsvService(
            SalesRepository salesRepository,
            PreprocessingService preprocessingService,
            JdbcTemplate jdbcTemplate) {
        this.salesRepository = salesRepository;
        this.preprocessingService = preprocessingService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public int uploadCsv(MultipartFile file) throws Exception {
        long totalStart = System.nanoTime();

        // Parse and validate the complete file before touching the current dataset.
        long parseStart = System.nanoTime();
        List<Sales> salesList = parseAndValidate(file);
        long parseEnd = System.nanoTime();

        // Calculate date, lag and rolling-average features once, in memory.
        long preprocessStart = System.nanoTime();
        preprocessingService.preprocessSales(salesList);
        long preprocessEnd = System.nanoTime();

        // Delete and insert share this transaction: a failed insert rolls back the delete.
        long deleteStart = System.nanoTime();
        salesRepository.deleteAllSalesInBulk();
        long deleteEnd = System.nanoTime();

        long insertStart = System.nanoTime();
        for (int start = 0; start < salesList.size(); start += BATCH_SIZE) {
            int end = Math.min(start + BATCH_SIZE, salesList.size());
            List<Sales> batch = salesList.subList(start, end);

            jdbcTemplate.batchUpdate(INSERT_SQL, batch, batch.size(),
                    (PreparedStatement ps, Sales sales) -> setInsertParameters(ps, sales));
        }
        long insertEnd = System.nanoTime();

        log.info("CSV upload complete: rows={}, parseValidateMs={}, preprocessMs={}, deleteMs={}, batchInsertMs={}, totalMs={}",
                salesList.size(),
                elapsedMs(parseStart, parseEnd),
                elapsedMs(preprocessStart, preprocessEnd),
                elapsedMs(deleteStart, deleteEnd),
                elapsedMs(insertStart, insertEnd),
                elapsedMs(totalStart, insertEnd));

        return salesList.size();
    }

    private List<Sales> parseAndValidate(MultipartFile file) throws Exception {
        String[] requiredColumns = {
                "Date", "Product_ID", "Product_Name", "Category",
                "Sales_Quantity", "Stock_Available", "Unit_Price_INR",
                "Reorder_Level", "Lead_Time_Days", "Distributor_Name",
                "Distributor_Email", "Shop_Owner_Email"
        };

        List<Sales> salesList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {

            for (String column : requiredColumns) {
                if (!csvParser.getHeaderMap().containsKey(column)) {
                    throw new IllegalArgumentException("Missing required column: " + column);
                }
            }

            int rowNumber = 1;
            for (CSVRecord record : csvParser) {
                rowNumber++;
                try {
                    LocalDate date = parseDate(record.get("Date"));
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

                    int salesQuantity = Integer.parseInt(record.get("Sales_Quantity").trim());
                    int stockAvailable = Integer.parseInt(record.get("Stock_Available").trim());
                    double unitPrice = Double.parseDouble(record.get("Unit_Price_INR").trim());
                    int reorderLevel = Integer.parseInt(record.get("Reorder_Level").trim());
                    int leadTimeDays = Integer.parseInt(record.get("Lead_Time_Days").trim());

                    if (salesQuantity < 0 || stockAvailable < 0 || unitPrice < 0
                            || reorderLevel < 0 || leadTimeDays < 0) {
                        throw new IllegalArgumentException("Numeric values cannot be negative");
                    }

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
                    sales.setDistributorName(record.get("Distributor_Name").trim());
                    sales.setDistributorEmail(record.get("Distributor_Email").trim());
                    sales.setShopOwnerEmail(record.get("Shop_Owner_Email").trim());
                    salesList.add(sales);

                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number at CSV row " + rowNumber, e);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException(
                            "Invalid date at CSV row " + rowNumber + ". Expected format: YYYY-MM-DD", e);
                }
            }
        }

        if (salesList.isEmpty()) {
            throw new IllegalArgumentException("CSV file contains no data");
        }
        return salesList;
    }

    private void setInsertParameters(PreparedStatement ps, Sales sales) throws java.sql.SQLException {
        ps.setDate(1, Date.valueOf(sales.getDate()));
        ps.setString(2, sales.getProductId());
        ps.setString(3, sales.getProductName());
        ps.setString(4, sales.getCategory());
        ps.setInt(5, sales.getSalesQuantity());
        ps.setInt(6, sales.getStockAvailable());
        ps.setDouble(7, sales.getUnitPriceInr());
        ps.setInt(8, sales.getReorderLevel());
        ps.setInt(9, sales.getLeadTimeDays());
        ps.setString(10, sales.getDistributorName());
        ps.setString(11, sales.getDistributorEmail());
        ps.setString(12, sales.getShopOwnerEmail());
        ps.setInt(13, sales.getYear());
        ps.setInt(14, sales.getMonth());
        ps.setInt(15, sales.getDay());
        ps.setInt(16, sales.getDayOfWeek());
        ps.setBoolean(17, Boolean.TRUE.equals(sales.getIsWeekend()));
        ps.setObject(18, sales.getSalesLag1(), Types.INTEGER);
        ps.setObject(19, sales.getSalesLag7(), Types.INTEGER);
        ps.setObject(20, sales.getSalesRolling7Mean(), Types.DOUBLE);
        ps.setObject(21, sales.getSalesRolling30Mean(), Types.DOUBLE);
    }

    private LocalDate parseDate(String dateValue) {
        String date = dateValue.trim();
        String[] formats = {"yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy/MM/dd"};

        for (String format : formats) {
            try {
                return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
            } catch (DateTimeParseException ignored) {
                // Try the next supported date format.
            }
        }

        throw new IllegalArgumentException(
                "Invalid date: " + date + ". Supported formats: YYYY-MM-DD, DD-MM-YYYY, DD/MM/YYYY");
    }

    private long elapsedMs(long start, long end) {
        return (end - start) / 1_000_000;
    }
}

package com.smartstock.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String productId;

    private String productName;

    private String category;

    private Integer salesQuantity;

    private Integer stockAvailable;

    private Double unitPriceInr;

    private Integer reorderLevel;

    private Integer leadTimeDays;

    private String distributorName;

    private String distributorEmail;

    private String shopOwnerEmail;

    // Preprocessing features

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer dayOfWeek;

    private Boolean isWeekend;

    private Integer salesLag1;

    private Integer salesLag7;

    private Double salesRolling7Mean;

    private Double salesRolling30Mean;


    // Default constructor
    public Sales() {
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Integer getSalesQuantity() {
        return salesQuantity;
    }

    public void setSalesQuantity(Integer salesQuantity) {
        this.salesQuantity = salesQuantity;
    }


    public Integer getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(Integer stockAvailable) {
        this.stockAvailable = stockAvailable;
    }


    public Double getUnitPriceInr() {
        return unitPriceInr;
    }

    public void setUnitPriceInr(Double unitPriceInr) {
        this.unitPriceInr = unitPriceInr;
    }


    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }


    public Integer getLeadTimeDays() {
        return leadTimeDays;
    }

    public void setLeadTimeDays(Integer leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }


    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }


    public String getDistributorEmail() {
        return distributorEmail;
    }

    public void setDistributorEmail(String distributorEmail) {
        this.distributorEmail = distributorEmail;
    }


    public String getShopOwnerEmail() {
        return shopOwnerEmail;
    }

    public void setShopOwnerEmail(String shopOwnerEmail) {
        this.shopOwnerEmail = shopOwnerEmail;
    }


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }


    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }


    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    public Boolean getIsWeekend() {
        return isWeekend;
    }

    public void setIsWeekend(Boolean isWeekend) {
        this.isWeekend = isWeekend;
    }


    public Integer getSalesLag1() {
        return salesLag1;
    }

    public void setSalesLag1(Integer salesLag1) {
        this.salesLag1 = salesLag1;
    }


    public Integer getSalesLag7() {
        return salesLag7;
    }

    public void setSalesLag7(Integer salesLag7) {
        this.salesLag7 = salesLag7;
    }


    public Double getSalesRolling7Mean() {
        return salesRolling7Mean;
    }

    public void setSalesRolling7Mean(Double salesRolling7Mean) {
        this.salesRolling7Mean = salesRolling7Mean;
    }


    public Double getSalesRolling30Mean() {
        return salesRolling30Mean;
    }

    public void setSalesRolling30Mean(Double salesRolling30Mean) {
        this.salesRolling30Mean = salesRolling30Mean;
    }
}
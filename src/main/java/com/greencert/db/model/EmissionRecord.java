package com.greencert.db.model;

import java.time.LocalDate;

public class EmissionRecord {
    
    private Integer id;
    private Integer userId;
    private String sourceType;
    private double amount;
    private double calculatedCarbon;
    private LocalDate recordDate;

    public EmissionRecord() {
    }

    public EmissionRecord(Integer userId, String sourceType, double amount, double calculatedCarbon, LocalDate recordDate) {
        this.userId = userId;
        this.sourceType = sourceType;
        this.amount = amount;
        this.calculatedCarbon = calculatedCarbon;
        this.recordDate = recordDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCalculatedCarbon() {
        return calculatedCarbon;
    }

    public void setCalculatedCarbon(double calculatedCarbon) {
        this.calculatedCarbon = calculatedCarbon;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
    
    @Override
    public String toString() {
        return "EmissionRecord{" +
                "id=" + id +
                ", source=" + sourceType +
                ", amount=" + amount +
                ", carbon=" + calculatedCarbon + " kgCO2e" +
                ", date=" + recordDate +
                '}';
    }
}

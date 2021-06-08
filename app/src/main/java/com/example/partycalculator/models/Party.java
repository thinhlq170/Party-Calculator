package com.example.partycalculator.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Party implements Serializable, Comparable<Party> {

    public static final String TABLE = "Party";
    public static final String KEY_ID = "id";
    public static final String NAME = "name";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String AVERAGE_AMOUNT = "average_amount";
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_DATE = "update_date";

    private Integer id;
    private String name;
    private BigDecimal totalAmount;
    private BigDecimal averageAmount;
    private String date;
    private String updateDate;

    public Party(String name) {
        this.name = name;
    }

    public Party() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Party o) {
        return this.getId().compareTo(o.getId());
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(BigDecimal averageAmount) {
        this.averageAmount = averageAmount;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}

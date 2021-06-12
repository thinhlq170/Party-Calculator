package com.example.partycalculator.models;

import android.database.Cursor;

import java.io.Serializable;
import java.math.BigDecimal;

public class Party implements Serializable, Comparable<Party> {

    public static final String TABLE = "Party";
    public static final String KEY_ID = "id";
    public static final String NAME = "name";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String AVERAGE_AMOUNT = "average_amount";
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_DATE = "update_date";

    private Long id;
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

    public Long getId() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void initFromCursor(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(Party.KEY_ID));
        this.name = cursor.getString(cursor.getColumnIndex(Party.NAME));
        this.averageAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Party.AVERAGE_AMOUNT)));
        this.totalAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Party.TOTAL_AMOUNT)));
        this.date = cursor.getString(cursor.getColumnIndex(Party.CREATE_DATE));
        this.updateDate = cursor.getString(cursor.getColumnIndex(Party.UPDATE_DATE));
    }
}

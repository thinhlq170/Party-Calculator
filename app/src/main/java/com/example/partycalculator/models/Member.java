package com.example.partycalculator.models;

import android.database.Cursor;

import com.example.partycalculator.repositories.GroceryRepo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Member implements Serializable {

    public static final String TABLE = "Member";
    public static final String KEY_ID = "id";
    public static final String PARTY_ID = "party_id";
    public static final String NAME = "name";
    public static final String PAID_AMOUNT = "paid_amount";
    public static final String CHANGE_AMOUNT = "change_amount";
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_DATE = "update_date";


    private Long id;
    private String name;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
    private String createDate;
    private String updateDate;
    private Long partyId;


    public Member() {
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

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void initFromCursor(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(Member.KEY_ID));
        this.name = cursor.getString(cursor.getColumnIndex(Member.NAME));
        this.paidAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Member.PAID_AMOUNT)));
        this.changeAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Member.CHANGE_AMOUNT)));
        this.partyId = cursor.getLong(cursor.getColumnIndex(Member.PARTY_ID));
        this.createDate = cursor.getString(cursor.getColumnIndex(Member.CREATE_DATE));
        this.updateDate = cursor.getString(cursor.getColumnIndex(Member.UPDATE_DATE));
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package com.example.partycalculator.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Member implements Serializable {

    public static final String TABLE = "Member";
    public static final String KEY_ID = "id";
    public static final String PARTY_ID = "party_id";
    public static final String NAME = "name";
    public static final String PAID_AMOUNT = "paid_amount";
    public static final String CHANGE_AMOUNT = "change_amount";
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_DATE = "update_date";


    private Integer id;
    private String name;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
    private String createDate;
    private String updateDate;
    private Integer partyId;


    public Member() {
    }

    public Member(String name) {
        this.name = name;
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

    public Integer getPartyId() {
        return partyId;
    }

    public void setPartyId(Integer partyId) {
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
}

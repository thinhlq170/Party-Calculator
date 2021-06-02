package com.example.partycalculator.models;

import java.io.Serializable;
import java.math.BigDecimal;

public class Member implements Serializable {
    public long id;
    public String name;
    public String phone;
    public BigDecimal paidAmount;
    public BigDecimal changeAmount;
    public String joinDate;

    public Member(String name, String phone, BigDecimal paidAmount, BigDecimal changeAmount, String joinDate) {
        this.name = name;
        this.phone = phone;
        this.paidAmount = paidAmount;
        this.changeAmount = changeAmount;
        this.joinDate = joinDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public Member() {}


}

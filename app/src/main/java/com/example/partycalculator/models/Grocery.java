package com.example.partycalculator.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Grocery implements Serializable {

    public static final String TABLE = "Grocery";
    public static final String KEY_ID = "id";
    public static final String MEMBER_ID = "member_id";
    public static final String NAME = "name";
    public static final String PRICE = "price";

    private Integer id;
    private String itemName;
    private BigDecimal price;
    private Integer memberId;

    public Grocery() {

    }

    public Grocery(String itemName, BigDecimal price) {
        this.itemName = itemName;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}

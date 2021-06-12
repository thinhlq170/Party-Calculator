package com.example.partycalculator.models;

import android.database.Cursor;

import java.io.Serializable;
import java.math.BigDecimal;

public class Grocery implements Serializable {

    public static final String TABLE = "Grocery";
    public static final String KEY_ID = "id";
    public static final String MEMBER_ID = "member_id";
    public static final String PARTY_ID = "party_id";
    public static final String NAME = "name";
    public static final String PRICE = "price";

    private Long id;
    private String itemName;
    private BigDecimal price;
    private Long memberId;
    private Long partyId;

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

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public void initFromCursor(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(Grocery.KEY_ID));
        this.itemName = cursor.getString(cursor.getColumnIndex(Grocery.NAME));
        this.memberId = cursor.getLong(cursor.getColumnIndex(Grocery.MEMBER_ID));
        this.partyId = cursor.getLong(cursor.getColumnIndex(Grocery.PARTY_ID));
        this.price = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Grocery.PRICE)));
    }
}

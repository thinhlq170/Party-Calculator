package com.example.partycalculator.dtos;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.partycalculator.models.Grocery;

import java.math.BigDecimal;

public class GroceryDTO {

    private Integer id;
    private String itemName;
    private BigDecimal price;
    private Integer memberId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public void initFromCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(Grocery.KEY_ID));
        this.itemName = cursor.getString(cursor.getColumnIndex(Grocery.NAME));
        this.memberId = cursor.getInt(cursor.getColumnIndex(Grocery.MEMBER_ID));
        this.price = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Grocery.PRICE)));
    }

    public void initData(ContentValues values) {

    }
}

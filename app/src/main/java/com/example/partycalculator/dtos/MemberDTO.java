package com.example.partycalculator.dtos;

import android.database.Cursor;

import com.example.partycalculator.models.Grocery;
import com.example.partycalculator.models.Member;
import com.example.partycalculator.repositories.GroceryRepo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class MemberDTO implements Serializable {

    private Integer id;
    private String name;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
    private String createDate;
    private String updateDate;
    private ArrayList<GroceryDTO> listGroceries;
    private Integer partyId;

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

    public ArrayList<GroceryDTO> getListGroceries() {
        return listGroceries;
    }

    public void setListGroceries(ArrayList<GroceryDTO> listGroceries) {
        this.listGroceries = listGroceries;
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

    public void initFromCursor(Cursor cursor) {
        GroceryRepo groceryRepo = new GroceryRepo();
        this.id = cursor.getInt(cursor.getColumnIndex(Member.KEY_ID));
        this.name = cursor.getString(cursor.getColumnIndex(Member.NAME));
        this.paidAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Member.PAID_AMOUNT)));
        this.changeAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Member.CHANGE_AMOUNT)));
        this.partyId = cursor.getInt(cursor.getColumnIndex(Member.PARTY_ID));
        this.createDate = cursor.getString(cursor.getColumnIndex(Member.CREATE_DATE));
        this.updateDate = cursor.getString(cursor.getColumnIndex(Member.UPDATE_DATE));
        this.listGroceries = groceryRepo.getListGroceryByMemberId(this.id);
    }
}

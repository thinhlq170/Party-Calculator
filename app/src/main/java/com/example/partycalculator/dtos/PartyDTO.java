package com.example.partycalculator.dtos;

import android.database.Cursor;

import com.example.partycalculator.models.Party;
import com.example.partycalculator.repositories.MemberRepo;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PartyDTO implements Comparable<PartyDTO> {

    private Integer id;
    private String name;
    private BigDecimal totalAmount;
    private BigDecimal averageAmount;
    private String date;
    private String updateDate;
    private ArrayList<MemberDTO> listMembers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public ArrayList<MemberDTO> getListMembers() {
        return listMembers;
    }

    public void setListMembers(ArrayList<MemberDTO> listMembers) {
        this.listMembers = listMembers;
    }

    public void initFromCursor(Cursor cursor) {
        MemberRepo memberRepo = new MemberRepo();
        this.id = cursor.getInt(cursor.getColumnIndex(Party.KEY_ID));
        this.name = cursor.getString(cursor.getColumnIndex(Party.NAME));
        this.averageAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Party.AVERAGE_AMOUNT)));
        this.totalAmount = BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(Party.TOTAL_AMOUNT)));
        this.date = cursor.getString(cursor.getColumnIndex(Party.CREATE_DATE));
        this.updateDate = cursor.getString(cursor.getColumnIndex(Party.UPDATE_DATE));
        this.listMembers = memberRepo.getListMemberByPartyId(this.id);
    }

    @Override
    public int compareTo(PartyDTO o) {
        return this.getId().compareTo(o.getId());
    }
}

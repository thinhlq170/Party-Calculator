package com.example.partycalculator.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Party implements Serializable {
    private long counter = 0;
    public long partyId;
    public String name;
    public String date;
    public ArrayList<Member> lstMember;

    public Party(long partyId, String name, String date, ArrayList<Member> lstMember) {
        this.partyId = partyId;
        this.name = name;
        this.date = date;
        this.lstMember = lstMember;
    }

    public Party() {
        this.partyId = ++counter;
    }

    public long getPartyId() {
        return partyId;
    }

    public void setPartyId(long partyId) {
        this.partyId = partyId;
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

    public ArrayList<Member> getLstMember() {
        return lstMember;
    }

    public void setLstMember(ArrayList<Member> lstMember) {
        this.lstMember = lstMember;
    }
}

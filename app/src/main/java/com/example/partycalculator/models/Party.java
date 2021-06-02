package com.example.partycalculator.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Party implements Serializable {
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
}

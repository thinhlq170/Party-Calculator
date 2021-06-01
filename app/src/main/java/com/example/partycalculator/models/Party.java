package com.example.partycalculator.models;

public class Party {
    public long partyId;
    public String name;
    public String date;

    public Party(long partyId, String name, String date) {
        this.partyId = partyId;
        this.name = name;
        this.date = date;
    }
}

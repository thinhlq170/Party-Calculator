package com.example.partycalculator.models;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Party implements Serializable, Comparable<Party> {
    private static AtomicInteger counter = new AtomicInteger(0);
    private Integer id;
    private String name;
    private String date;
    private String updateDate;
    private ArrayList<Member> lstMember;

    public Party(Integer partyId, String name, String date, ArrayList<Member> lstMember) {
        this.id = partyId;
        this.name = name;
        this.date = date;
        this.lstMember = lstMember;
    }

    public Party() {
        this.id = counter.incrementAndGet();
    }

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

    @Override
    public int compareTo(Party o) {
        return this.getId().compareTo(o.getId());
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}

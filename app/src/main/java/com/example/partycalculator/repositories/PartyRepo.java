package com.example.partycalculator.repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.partycalculator.databases.DatabaseManager;
import com.example.partycalculator.dtos.MemberDTO;
import com.example.partycalculator.dtos.PartyDTO;
import com.example.partycalculator.models.Member;
import com.example.partycalculator.models.Party;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PartyRepo {
    private Party party;

    public PartyRepo() {
        this.party = new Party();
    }

    public static String createTable() {
        return "CREATE TABLE " + Party.TABLE + "("
                + Party.KEY_ID + "   INTEGER PRIMARY KEY AUTOINCREMENT    ,"
                + Party.NAME + " TEXT,"
                + Party.TOTAL_AMOUNT + " REAL, "
                + Party.AVERAGE_AMOUNT + " REAL, "
                + Party.CREATE_DATE + " TEXT, "
                + Party.UPDATE_DATE + " TEXT "
                + ");";
    }

    public int insert(PartyDTO party) {
        int partyId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = initPartyData(party);
        partyId = (int)db.insert(Party.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return partyId;
    }

    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Party.TABLE, null, null);
        db.close();
    }

    public ArrayList<PartyDTO> getListAllParty() {
        ArrayList<PartyDTO> lstData = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sqlQuery = "SELECT * FROM Party";
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if(cursor.moveToFirst()) {
            do {
                PartyDTO dto = new PartyDTO();
                dto.initFromCursor(cursor);
                lstData.add(dto);
            } while(cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return lstData;
    }

    public PartyDTO getPartyById(Integer partyId) {
        PartyDTO dto = new PartyDTO();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(Party.TABLE, new String[] { Party.KEY_ID,
                        Party.NAME, Party.TOTAL_AMOUNT, Party.AVERAGE_AMOUNT,
                Party.CREATE_DATE, Party.UPDATE_DATE}, Party.KEY_ID + "=?",
                new String[] { String.valueOf(partyId) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            dto.initFromCursor(cursor);
        }
        DatabaseManager.getInstance().closeDatabase();
        return dto;
    }

    private ContentValues initPartyData(PartyDTO party) {
        ContentValues values = new ContentValues();
        values.put(Party.NAME, party.getName());
        values.put(Party.TOTAL_AMOUNT, party.getTotalAmount() == null ? null : party.getTotalAmount().toString());
        values.put(Party.AVERAGE_AMOUNT, party.getAverageAmount() == null ? null : party.getAverageAmount().toString());
        if(party.getId() != null) {
            values.put(Party.UPDATE_DATE, getCurrentTime());
        } else {
            values.put(Party.CREATE_DATE, getCurrentTime());
        }
        return values;
    }

    private String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int update(PartyDTO dto) {
        int updateSuccess = -1;
        if(dto != null) {
            ContentValues values = initPartyData(dto);
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            updateSuccess = db.update(Party.TABLE, values, Party.KEY_ID + "= ?", new String[]{String.valueOf(dto.getId())});
            DatabaseManager.getInstance().closeDatabase();
        }
        return updateSuccess;
    }
}

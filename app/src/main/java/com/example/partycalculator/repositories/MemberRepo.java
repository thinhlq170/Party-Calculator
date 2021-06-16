package com.example.partycalculator.repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.partycalculator.databases.DatabaseManager;
import com.example.partycalculator.models.Grocery;
import com.example.partycalculator.models.Member;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemberRepo {

    public MemberRepo() {
        Member member = new Member();
    }

    public static String createTable() {
        return "CREATE TABLE " + Member.TABLE + "("
                + Member.KEY_ID + "   INTEGER PRIMARY KEY AUTOINCREMENT    ,"
                + Member.NAME + " TEXT,"
                + Member.PARTY_ID + " INTEGER, "
                + Member.PAID_AMOUNT + " REAL, "
                + Member.CHANGE_AMOUNT + " REAL, "
                + Member.CREATE_DATE + " TEXT, "
                + Member.UPDATE_DATE + " TEXT "
                + ");";
    }

    public long insert(Member mem) {
        long memberId = -1;
        if(mem != null) {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Member.NAME, mem.getName());
            values.put(Member.PARTY_ID, mem.getPartyId());
            values.put(Member.PAID_AMOUNT, mem.getPaidAmount() == null ? null : mem.getPaidAmount().toString());
            values.put(Member.CHANGE_AMOUNT, mem.getChangeAmount() == null ? null : mem.getChangeAmount().toString());
            values.put(Member.CREATE_DATE, mem.getCreateDate());
            values.put(Member.UPDATE_DATE, mem.getUpdateDate());
            memberId = db.insert(Member.TABLE, null, values);
            DatabaseManager.getInstance().closeDatabase();
        }
        return memberId;
    }

    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Member.TABLE, null, null);
        db.close();
    }

    public void update(Member dto) {
        if(dto != null) {
            ContentValues values = initMemberData(dto);
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.update(Member.TABLE, values, Member.KEY_ID + "= ?", new String[]{String.valueOf(dto.getId())});
            DatabaseManager.getInstance().closeDatabase();
        } else {
            Log.v("Cannot update", ",cause id is null");
        }
    }

    private ContentValues initMemberData(Member mem) {
        ContentValues values = new ContentValues();
        values.put(Member.NAME, mem.getName());
        values.put(Member.PARTY_ID, mem.getPartyId());
        values.put(Member.PAID_AMOUNT, mem.getPaidAmount() == null ? null : mem.getPaidAmount().toString());
        values.put(Member.CHANGE_AMOUNT, mem.getChangeAmount() == null ? null : mem.getChangeAmount().toString());
        if(mem.getId() != null) {
            values.put(Member.UPDATE_DATE, getCurrentTime());
        } else {
            values.put(Member.CREATE_DATE, getCurrentTime());
        }
        return values;
    }

    public ArrayList<Member> getListMemberByPartyId(Long partyId) {
        ArrayList<Member> lstData = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sqlQuery = "SELECT mem.* FROM Member mem"
                + " JOIN Party party ON mem.party_id = party.id "
                + " WHERE party.id = " + partyId;
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Member dto = new Member();
                dto.initFromCursor(cursor);
                lstData.add(dto);
            } while(cursor.moveToNext());
        }
        return lstData;
    }

    public Member getMemberById(Long memberId) {
        Member dto = new Member();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(Member.TABLE, new String[] { Member.KEY_ID,
                        Member.NAME, Member.PAID_AMOUNT, Member.CHANGE_AMOUNT, Member.PARTY_ID,
                        Member.CREATE_DATE, Member.UPDATE_DATE}, Member.KEY_ID + "=?",
                new String[] { String.valueOf(memberId) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            dto.initFromCursor(cursor);
        }
        DatabaseManager.getInstance().closeDatabase();
        return dto;
    }

    private String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public void deleteARow(Long id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Member.TABLE, Member.KEY_ID + "=?", new String[]{String.valueOf(id)});
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteGroceriesByMemberId(Long memberId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Grocery.TABLE, Grocery.MEMBER_ID + "=?", new String[]{String.valueOf(memberId)});
        DatabaseManager.getInstance().closeDatabase();
        Log.v("Groceries deleted with","member id: " + memberId);
    }

}

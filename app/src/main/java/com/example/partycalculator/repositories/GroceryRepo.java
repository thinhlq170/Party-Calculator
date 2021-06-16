package com.example.partycalculator.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.partycalculator.databases.DatabaseManager;
import com.example.partycalculator.models.Grocery;

import java.util.ArrayList;

public class GroceryRepo {

    public static String createTable() {
        return "CREATE TABLE " + Grocery.TABLE + "("
                + Grocery.KEY_ID + "   INTEGER PRIMARY KEY AUTOINCREMENT    ,"
                + Grocery.MEMBER_ID + "   INTEGER    ,"
                + Grocery.PARTY_ID + "   INTEGER    ,"
                + Grocery.NAME + " TEXT,"
                + Grocery.PRICE + " REAL "
                + ");";
    }

    public long insert(Grocery grocery) {
        long groceryId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = initGroceryData(grocery);
        groceryId = db.insert(Grocery.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return groceryId;
    }

    public void deleteARow(Long id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Grocery.TABLE, Grocery.KEY_ID + "=?", new String[]{String.valueOf(id)});
        DatabaseManager.getInstance().closeDatabase();
    }

    public ArrayList<Grocery> getListGroceryByMemberId(Long memberId) {
        ArrayList<Grocery> lstData = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sqlQuery = "SELECT gro.* FROM Grocery gro"
                + " JOIN Member mem ON gro.member_id = mem.id "
                + " WHERE mem.id = ?";
        Cursor cursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(memberId)});
        if(cursor.moveToFirst()) {
            do {
                Grocery dto = new Grocery();
                dto.initFromCursor(cursor);
                lstData.add(dto);
            } while(cursor.moveToNext());
        }
        return lstData;
    }

    public void update(Grocery dto) {
        if(dto != null) {
            ContentValues values = initGroceryData(dto);
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.update(Grocery.TABLE, values, Grocery.KEY_ID + "= ?", new String[]{String.valueOf(dto.getId())});
            DatabaseManager.getInstance().closeDatabase();
        } else {
            Log.v("Cannot update", ",cause id is null");
        }
    }

    private ContentValues initGroceryData(Grocery grocery) {
        ContentValues values = new ContentValues();
        values.put(Grocery.NAME, grocery.getItemName());
        values.put(Grocery.MEMBER_ID, grocery.getMemberId());
        values.put(Grocery.PARTY_ID, grocery.getMemberId());
        values.put(Grocery.PRICE, grocery.getPrice().toString());
        return values;
    }

    public Grocery getGroceryById(Long groceryId) {
        Grocery dto = new Grocery();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(Grocery.TABLE, new String[] { Grocery.KEY_ID,
                        Grocery.NAME, Grocery.PRICE, Grocery.MEMBER_ID, Grocery.PARTY_ID}, Grocery.KEY_ID + "=?",
                new String[] { String.valueOf(groceryId) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            dto.initFromCursor(cursor);
        }
        return dto;
    }
}

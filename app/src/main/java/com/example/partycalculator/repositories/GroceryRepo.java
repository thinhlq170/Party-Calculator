package com.example.partycalculator.repositories;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.partycalculator.databases.DatabaseManager;
import com.example.partycalculator.dtos.GroceryDTO;
import com.example.partycalculator.dtos.PartyDTO;
import com.example.partycalculator.models.Grocery;
import com.example.partycalculator.models.Party;

import java.util.ArrayList;

public class GroceryRepo {

    public static String createTable() {
        return "CREATE TABLE " + Grocery.TABLE + "("
                + Grocery.KEY_ID + "   INTEGER PRIMARY KEY AUTOINCREMENT    ,"
                + Grocery.MEMBER_ID + "   INTEGER    ,"
                + Grocery.NAME + " TEXT,"
                + Grocery.PRICE + " REAL "
                + ");";
    }

    public int insert(GroceryDTO grocery) {
        int groceryId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = initGroceryData(grocery);
        groceryId = (int)db.insert(Grocery.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return groceryId;
    }

    public void deleteARow(Integer id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Grocery.TABLE, Grocery.KEY_ID + "=?", new String[]{String.valueOf(id)});
        DatabaseManager.getInstance().closeDatabase();
    }

    public ArrayList<GroceryDTO> getListGroceryByMemberId(Integer memberId) {
        ArrayList<GroceryDTO> lstData = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sqlQuery = "SELECT * FROM Grocery gro"
                + " JOIN Member mem ON gro.member_id = mem.id "
                + " WHERE mem.id = ?";
        Cursor cursor = db.rawQuery(sqlQuery, new String[]{String.valueOf(memberId)});
        if(cursor.moveToFirst()) {
            do {
                GroceryDTO dto = new GroceryDTO();
                dto.initFromCursor(cursor);
                lstData.add(dto);
            } while(cursor.moveToNext());
        }
        return lstData;
    }

    public int update(GroceryDTO dto) {
        int updateSuccess = -1;
        if(dto != null) {
            ContentValues values = initGroceryData(dto);
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            updateSuccess = db.update(Grocery.TABLE, values, Grocery.KEY_ID + "= ?", new String[]{String.valueOf(dto.getId())});
            DatabaseManager.getInstance().closeDatabase();
        }
        return updateSuccess;
    }

    private ContentValues initGroceryData(GroceryDTO grocery) {
        ContentValues values = new ContentValues();
        values.put(Grocery.NAME, grocery.getItemName());
        values.put(Grocery.MEMBER_ID, grocery.getMemberId());
        values.put(Grocery.PRICE, grocery.getPrice().toString());
        return values;
    }

    public GroceryDTO getGroceryById(Integer groceryId) {
        GroceryDTO dto = new GroceryDTO();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(Grocery.TABLE, new String[] { Grocery.KEY_ID,
                        Grocery.NAME, Grocery.PRICE}, Grocery.KEY_ID + "=?",
                new String[] { String.valueOf(groceryId) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            dto.initFromCursor(cursor);
        }
        return dto;
    }
}

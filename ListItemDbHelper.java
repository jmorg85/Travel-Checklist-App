package com.justice.travelchecklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ListItemDbHelper extends SQLiteOpenHelper {
    private static final String tableName = "Checklist_Items";

    public ListItemDbHelper(@Nullable Context context) {
        super(context, tableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + tableName + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Item_Name TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addChecklistItem(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor dbRows = getListContents();

        while(dbRows.moveToNext()) {
            if(itemName.equals(dbRows.getString(1)))
                return false;
        }

        values.put("Item_Name", itemName);

        long result = db.insert(tableName, null, values);

        if(result == -1){
            return false;
        }

        return true;
    }

    public int deleteChecklistItem(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(tableName, "Item_Name=?", new String[]{itemName});

        return result;
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + tableName, null);
        return data;
    }
}

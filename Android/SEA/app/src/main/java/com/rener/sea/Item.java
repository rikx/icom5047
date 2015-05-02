package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single "node" for a flowchart
 */
public class Item {

    public static final String RECOMMENDATION = "RECOM";
    public static final String END = "END";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String MULTIPLE_CHOICE = "MULTI";
    public static final String OPEN = "OPEN";
    public static final String CONDITIONAL = "CONDITIONAL";
    private long id;
    private List<Option> options;
    private DBHelper dbHelper = null;

    public Item(long id, String label, String type) {
        this.id = id;
        options = new ArrayList<>();
    }

    public Item(long id, DBHelper db) {
        this.dbHelper = db;
        invoke(id);
    }


    private long create(long flowchart_id, String lable, String type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ITEM_FLOWCHART_ID, flowchart_id);
        values.put(DBSchema.ITEM_LABEL, lable);
        values.put(DBSchema.ITEM_TYPE, type);
        long id = db.insert(DBSchema.TABLE_ITEM, null, values);
        db.close();
        return id;

    }


    private boolean exist(long item_id) {
        if (item_id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ITEM, new String[]{DBSchema.ITEM_ID},
                DBSchema.ITEM_ID + "=?", new String[]{String.valueOf(item_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    private boolean invoke(long user_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ITEM, new String[]{DBSchema.ITEM_ID},
                DBSchema.ITEM_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            this.id = cursor.getLong(0);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        invoke(id);
    }

    public String getLabel() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String label = null;
        Cursor cursor = db.query(DBSchema.TABLE_ITEM, new String[]{DBSchema.ITEM_LABEL},
                DBSchema.ITEM_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                label = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return label;
    }

    public long setLabel(String label) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ITEM_LABEL, String.valueOf(label));
        long id = db.update(DBSchema.TABLE_ITEM, values, DBSchema.ITEM_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getType() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String type = null;
        Cursor cursor = db.query(DBSchema.TABLE_ITEM, new String[]{DBSchema.ITEM_TYPE},
                DBSchema.ITEM_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                type = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return type;
    }

    public long setType(String type) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ITEM_TYPE, String.valueOf(type));
        long id = db.update(DBSchema.TABLE_ITEM, values, DBSchema.ITEM_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public List<Option> getOptions() {

        return dbHelper.getAllOptions(id);
    }


//    public void setOptions(List<Option> options) {
//        this.options = options;
//    }

//    public ArrayList<Item> getAllItems(long flowchartID){
//        //ArrayList<Item> items = new ArrayList<>();
//        return dbHelper.getAllItems(this.id);
//    }

    /**
     * this function will not be implemented
     * Add an option to this Item's option list
     *
     * @param option the Option to be added
     */
    public void addOption(Option option) {
        options.add(option);
    }
}

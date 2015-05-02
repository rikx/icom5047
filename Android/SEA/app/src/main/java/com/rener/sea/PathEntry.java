package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Wrapper class representing a trio of an Item, Option, and String.
 * This class is used as a helper to facilitate navigating through a flowchart.
 */
public class PathEntry {
    private Item item;// has flowchart id
    private Option option;// id
    private String data;
    private long report_id = -1;
    private long option_id = -1;
    private DBHelper dbHelper = null;

    public PathEntry(long report_id, long option_id, String data, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(report_id,option_id)) { // can also verify if id == -1

        } else {
            create(report_id,option_id,data);
        }
    }
    public PathEntry(long report_id, long option_id, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(report_id,option_id)) { // can also verify if id == -1
            invoke(report_id,option_id);
        } else {
            create(report_id,option_id);
        }
    }

    PathEntry(Item item, Option option) {
        this.item = item;
        this.option = option;
        this.data = "";
    }

    PathEntry(Item item, Option option, String data) {
        this.item = item;
        this.option = option;
        this.data = data;
    }

    private void create(long report_id, long option_id, String data){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PATH_REPORT_ID, report_id);
        values.put(DBSchema.PATH_OPTION_ID, option_id);
        values.put(DBSchema.PATH_DATA, data);
        long id = db.insert(DBSchema.TABLE_PATH, null, values);
        db.close();

        this.report_id = report_id;
        this.option_id = option_id;
    }
    private void create(long report_id, long option_id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PATH_REPORT_ID, report_id);
        values.put(DBSchema.PATH_OPTION_ID, option_id);
        long id = db.insert(DBSchema.TABLE_PATH, null, values);
        db.close();

        this.report_id = report_id;
        this.option_id = option_id;
    }

    private boolean exist(long report_id, long option_id) {
        if (report_id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PATH, new String[]{DBSchema.PATH_REPORT_ID},
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    private boolean invoke(long report_id, long option_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.PATH_REPORT_ID,DBSchema.PATH_OPTION_ID,DBSchema.PATH_DATA},
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                this.report_id = cursor.getLong(0);
            if(!cursor.isNull(1))
                this.option_id = cursor.getLong(1);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    public Item getItem() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long option = -1;
        Cursor cursor = db.query(DBSchema.TABLE_PATH, new String[]{DBSchema.PATH_OPTION_ID},
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                option = cursor.getLong(0);
            db.close();
            cursor.close();
        }
        return (new Option(option,dbHelper)).getParent();
    }

    public long setItem(Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PATH_OPTION_ID, item.getId());
        long id = db.update(DBSchema.TABLE_PATH, values,
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)});
        db.close();
        return id;// if -1 error during update
    }

    public Option getOption() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long item = -1;
        Cursor cursor = db.query(DBSchema.TABLE_PATH, new String[]{DBSchema.PATH_OPTION_ID},
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                item = cursor.getLong(0);
            db.close();
            cursor.close();
        }
        return new Option(item,dbHelper);
    }

    public long setOption(Option option) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PATH_OPTION_ID, option.getId());
        long id = db.update(DBSchema.TABLE_PATH, values,
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String data = "";
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.PATH_DATA},
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                data = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return data;
    }

    public long setData(String data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PATH_DATA, String.valueOf(data));
        long id = db.update(DBSchema.TABLE_PATH, values,
                DBSchema.PATH_REPORT_ID + "=? "+DBSchema.PATH_OPTION_ID + "=? ", new String[]{String.valueOf(report_id),String.valueOf(option_id)});
        db.close();
        return id;// if -1 error during update
    }
}
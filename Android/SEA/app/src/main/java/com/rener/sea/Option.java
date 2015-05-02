package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * A class representing an "edge" in a flowchart
 */
public class Option {

    private long id;
    private Item next;
    private String label;
    private DBHelper dbHelper = null;

    /**
     * Construct a new Option object with the given ID, next Item in the flow, and a text label.
     *
     * @param id    a unique ID
     * @param next  the next Item in the flow
     * @param label some text
     */
    public Option(long id, Item next, String label) {
        this.id = id;
        this.next = next;
        this.label = label;
    }

    /**
     * Construct a new Option object with the given ID and next Item in the flow.
     *
     * @param id   a unique ID
     * @param next the next Item object in thw flow
     */
    public Option(long id, Item next) {
        this.id = id;
        this.next = next;
        this.label = "";
    }

    public Option(long optionID, DBHelper db) {
        this.dbHelper = db;
        invoke(id);
    }
    public Option(long optionID,long partentID, long nextID,String label, DBHelper db) {
        this.dbHelper = db;
        if (exist(optionID)) { // can also verify if id == -1

        } else {
            this.id = create(optionID, partentID, nextID,label);
        }
    }

    private long create(long option_id, long parent_id, long next_id, String label) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.OPTION_ID, option_id);
        values.put(DBSchema.OPTION_PARENT_ID, parent_id);
        values.put(DBSchema.OPTION_NEXT_ID, next_id);
        values.put(DBSchema.OPTION_LABEL, label);
        long id = db.insert(DBSchema.TABLE_OPTION, null, values);
        db.close();
        return id;

    }

    private long create(long parent_id, long next_id, String label) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.OPTION_PARENT_ID, parent_id);
        values.put(DBSchema.OPTION_NEXT_ID, next_id);
        values.put(DBSchema.OPTION_LABEL, label);
        long id = db.insert(DBSchema.TABLE_OPTION, null, values);
        db.close();
        return id;

    }


    private boolean exist(long option_id) {

        if (option_id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{DBSchema.OPTION_ID},
                DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(option_id)}, null, null, null, null);
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
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{DBSchema.OPTION_ID},
                DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
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

    public Item getNext() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long nextItem = -1;
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{DBSchema.OPTION_NEXT_ID},
                DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
            nextItem = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        return new Item(nextItem, dbHelper);
    }

    public long setNext(Item next) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.OPTION_NEXT_ID, next.getId());
        long id = db.update(DBSchema.TABLE_OPTION, values, DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getLabel() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String label = "";
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{DBSchema.OPTION_NEXT_ID},
                DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
            label = cursor.getString(0);
            db.close();
            cursor.close();
        }

        return label;
    }

    public long setLabel(String label) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.OPTION_LABEL, String.valueOf(label));
        long id = db.update(DBSchema.TABLE_OPTION, values, DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    /**
     * Check whether the two Option objects are the equivalent by comparing their IDs
     *
     * @param other the other Option object
     * @return true if their IDs match
     */
    public boolean equals(Option other) {
        return this.id == other.getId();
    }

    public long setParent(Item parent) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.OPTION_PARENT_ID, parent.getId());
        long id = db.update(DBSchema.TABLE_OPTION, values, DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public Item getParent() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long nextItem = -1;
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{DBSchema.OPTION_PARENT_ID},
                DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                nextItem = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        return new Item(nextItem, dbHelper);
    }


}

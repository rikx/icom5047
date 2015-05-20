package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Represents a flowchart within the system
 */
public class Flowchart implements Comparable<Flowchart> {

    private long id = -1;
    private String dummy;
    private List<Item> items;
    private DBHelper dbHelper = null;

    public Flowchart(long id, DBHelper db) {
        this.dbHelper = db;
        invoke(id);
    }

    public Flowchart(String dummy) {
        this.dummy = dummy;
    }

    public Flowchart(long id, long first, long end, long creator, String name, String version, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(id)) { // can also verify if id == -1
            invoke(id);
        } else {
            this.id = create(first, end, creator, name, version);
        }
    }

    private long create(long first, long end, long creator, String name, String version) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.FLOWCHART_FIRST_ID, first);
        values.put(DBSchema.FLOWCHART_NAME, name);
        values.put(DBSchema.FLOWCHART_END_ID, end);
        values.put(DBSchema.FLOWCHART_CREATOR_ID, creator);
        values.put(DBSchema.FLOWCHART_VERSION, version);

        long id = db.insert(DBSchema.TABLE_FLOWCHART, null, values);
        db.close();
        return id;

    }

    private boolean exist(long id) {
        if (id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_ID},//,DBSchema.USER_USERNAME,DBSchema.USER_PASSHASH,DBSchema.USER_PASSHASH,DBSchema.USER_SALT
                DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    private boolean invoke(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_ID},
                DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
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

//    public void setId(long id) {
//        this.id = id;
//    }

    public Item getFirst() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long first_id = -1;
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_FIRST_ID},
                DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0)) {
                first_id = cursor.getLong(0);
            }

            db.close();
            cursor.close();
        }

        return new Item(first_id, dbHelper);


    }

    public long setFirst(Item first) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.FLOWCHART_FIRST_ID, first.getId());
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_FLOWCHART, values, DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public Item getEnd() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long last_id = -1;
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_END_ID},
                DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0)) {
                last_id = cursor.getLong(0);
            }

            db.close();
            cursor.close();
        }

        return new Item(last_id, dbHelper);
    }

    public long setEnd(Item end) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.FLOWCHART_END_ID, end.getId());
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_FLOWCHART, values, DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getName() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = "";
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_NAME},
                DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0)) {
                name = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return name;

    }

    public long setName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.FLOWCHART_END_ID, String.valueOf(name));
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_FLOWCHART, values, DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public User getCreator() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long id = -1;
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_CREATOR_ID},
                DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            id = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        return new User(id, dbHelper);
    }

    public long setCreator(User creator) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.FLOWCHART_CREATOR_ID, creator.getId());
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_FLOWCHART, values, DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getVersion() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String version = "";
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_VERSION},
                DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0)) {
                version = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return version;
    }

    public long setVersion(String version) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.FLOWCHART_VERSION, String.valueOf(version));
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_FLOWCHART, values, DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    // for compatibility
    public List<Item> getItems() {

        return dbHelper.getAllItems(this.id);

    }

//    public void setItems(List<Item> items) {
//        this.items = items;
//    }


    /**
     * this function will not be implemented
     * Add a Item object to the list of items for this flowchart.
     *
     * @param item the Item object to be added
     */
    public void addItem(Item item) {
        items.add(item);
    }

    public void fetchAllItems() {

        items = dbHelper.getAllItems(id);

    }

    public String toString() {
        return id == -1 ? dummy : getName().trim() + " - " + getVersion()
                .trim();
    }

    @Override
    public int compareTo(Flowchart other) {
        int compare = toString().compareTo(other.toString());
        return compare;
    }
}

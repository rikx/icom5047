package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * A class representing a location
 */
public class Location implements Comparable<Location> {

    public static final String PUERTO_RICO = "Puerto Rico";
    String dummy;
    private long id = -1;
    private Address address;
    private DBHelper dbHelper = null;

    public Location(long id, DBHelper db) {
        this.dbHelper = db;
        invoke(id);
    }

    // this method is only for the use of DBHelper
    public Location(long id,long address_id,String name, DBHelper db) {
        this.dbHelper = db;
        if(id > 0) {
            this.id = id;
            dummy = name;
            address = new Address(address_id, dbHelper);
        }
    }

    public Location(long id, String name, long addressID, long ownerID, long managerID, String license, long agentID, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(id)) {

        } else {
            this.id = create(name, addressID, ownerID, managerID, license, agentID);
            if(this.id>0){
                dummy = name;
            }
        }
    }

    public Location(String dummy) {
        this.dummy = dummy;
    }


    private long create(String name, long addressID, long ownerID, long managerID, String license, long agentID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_NAME, name);
        values.put(DBSchema.LOCATION_ADDRESS_ID, addressID);
        values.put(DBSchema.LOCATION_OWNER_ID, ownerID);
        values.put(DBSchema.LOCATION_MANAGER_ID, managerID);
        values.put(DBSchema.LOCATION_LICENSE, license);
        values.put(DBSchema.LOCATION_AGENT_ID, agentID);

        long id = db.insert(DBSchema.TABLE_LOCATION, null, values);
        db.close();

        this.address = new Address(addressID, dbHelper);
        return id;

    }


    private boolean exist(long id) {
        if (id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_ID},//,DBSchema.USER_USERNAME,DBSchema.USER_PASSHASH,DBSchema.USER_PASSHASH,DBSchema.USER_SALT
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
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
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_ID, DBSchema.LOCATION_ADDRESS_ID,DBSchema.LOCATION_NAME},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                this.id = cursor.getLong(0);
            if (!cursor.isNull(1))
                this.address = new Address(cursor.getLong(1), dbHelper);
            if (!cursor.isNull(2))
                this.dummy = cursor.getString(2);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    public long getId() {
        return id;
    }

    public String getName() {

//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String name = null;
//        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_NAME},
//                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//            if (!cursor.isNull(0)) {
//                name = cursor.getString(0);
//            }
//            db.close();
//            cursor.close();
//        }

        return dummy;
    }

    public long setName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_NAME, String.valueOf(name));
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        if(id>0)
            dummy = name;
        db.close();
        return id;// if -1 error during update
    }

    public Person getManager() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long managerID = -1;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_MANAGER_ID},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                managerID = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        return new Person(managerID, dbHelper);
    }

    public long setManager(Person manager) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_MANAGER_ID, manager.getId());
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public boolean hasManager() {
        return getManager().getId() != -1;
    }

    public Person getOwner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long ownerID = -1;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_OWNER_ID},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                ownerID = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        return new Person(ownerID, dbHelper);

    }

    public long setOwner(Person owner) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_OWNER_ID, owner.getId());
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public boolean hasOwner() {
        return getOwner().getId() != -1;
    }

    public Person getAgent() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long agentID = -1;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_AGENT_ID},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                agentID = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        return new Person(agentID, dbHelper);
    }

    public long setAgent(Person agent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_AGENT_ID, agent.getId());
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public boolean hasAgent() {
        return getAgent().getId() != -1;
    }


    public String getAddressLine(int n) {
        return address.getAddressLine(n);
    }

    public String setAddressLine(int n, String line) {
        if (line == null || line.equals(""))
            address.setAddressLine(n, line);
        else
            address.setAddressLine(n, line);
        return address.getAddressLine(n);
    }

    public String getCity() {
        return address.getCity();
    }

    public long setCity(String city) {

        return address.setCity(city);
    }

    public String getZipCode() {
        return String.valueOf(address.getZipcode());
    }

    public long setZipCode(String code) {
        return address.setZipcode(code);

    }

    public long getAddressId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long addressId = -1;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_ADDRESS_ID},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                addressId = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        return addressId;
    }

    public long setAddressId(long addressId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_ADDRESS_ID, String.valueOf(addressId));
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        this.address = new Address(id, dbHelper);
        return id;// if -1 error during update
    }

    public String getLicense() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String license = null;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_LICENSE},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                license = cursor.getString(0);
            db.close();
            cursor.close();
        }

        return license;
    }

    public long setLicense(String license) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_LICENSE, String.valueOf(license));
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = -1;
            id = db.updateWithOnConflict(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)},SQLiteDatabase.CONFLICT_IGNORE);

        db.close();
        return id;// if conflict during Update return 0 if error during update return -1
    }

    public String toString() {
        return id == -1 ? dummy : getName();
    }

    @Override
    public int compareTo(Location l) {
        return toString().toLowerCase().compareTo(l.toString().toLowerCase());
    }

    public boolean equals(Location l) {
        return this.id == l.getId();
    }
}

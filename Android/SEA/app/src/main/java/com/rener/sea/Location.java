package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * A class representing a location
 */
public class Location implements Comparable<Location> {

    public static final String PUERTO_RICO = "Puerto Rico";
    private long id;
    private String name = "";
    private Person manager = null;
    private Person owner = null;
    private Person agent = null;
    private Address address = null;
    private long address_id;
    private String license = "";
    private SEASchema dbHelper = null;

    public Location(long id, SEASchema db) {
        this.dbHelper = db;
        invoke(id);
    }

    /**
     * Constructs a new Location object with the given name.
     * Used for locations that have been created but haven't been assigned an ID
     *
     * @param name
     */
    public Location(String name) {
        this.name = name;
        this.address = Location.newAddress();
    }

    /**
     * Constructs a new Location object with the given ID and name
     *
     * @param id
     * @param name
     */
    public Location(long id, String name) {
        this.id = id;
        this.name = name;
        this.address = newAddress();
    }


    private static Address newAddress() {
        Address a = new Address(Locale.US);
        a.setAddressLine(0, "");
        a.setLocality("");
        a.setCountryName(PUERTO_RICO);
        a.setPostalCode("");
        return a;
    }
//    DBSchema.LOCATION_ID
//    DBSchema.LOCATION_NAME
//    DBSchema.LOCATION_ADDRESS_ID
//    DBSchema.LOCATION_OWNER_ID
//    DBSchema.LOCATION_MANAGER_ID
//    DBSchema.LOCATION_LICENSE
//    DBSchema.LOCATION_AGENT_ID

    private long create(String name, String addressID, long ownerID, String managerID, String license, long agentID) {
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
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_ID},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
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

    public String getName() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = null;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_NAME},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            name = cursor.getString(0);
            db.close();
            cursor.close();
        }

        return name;
    }

    public long setName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.LOCATION_NAME, String.valueOf(name));
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
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
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public boolean hasAgent() {
        return getAgent().getId() != -1;
    }

    public String toString() {
        return getName();
    }

    public Address getAddress() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long addressId = -1;
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{DBSchema.ADDRESS_LINE1,
                        DBSchema.ADDRESS_LINE2,
                        DBSchema.ADDRESS_CITY,
                        DBSchema.ADDRESS_ZIPCODE},
                DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            address.setAddressLine(0, cursor.getString(0));
            address.setAddressLine(1, cursor.getString(1));
            address.setLocality(cursor.getString(2));
            address.setPostalCode(cursor.getString(3));
            db.close();
            cursor.close();
        }

        return address;
    }

    public String getAddressLine(int n) {
        return address.getAddressLine(n - 1);
    }

    public String setAddressLine(int n, String line) {
        if (line == null || line.equals(""))
            address.setAddressLine(n - 1, null);
        else
            address.setAddressLine(n - 1, line);
        return address.getAddressLine(n - 1);
    }

    public String getCity() {
        return address.getLocality();
    }

    public String setCity(String city) {
        address.setLocality(city);
        return address.getLocality();
    }

    public String getZipCode() {
        return address.getPostalCode();
    }

    public String setZipCode(String code) {
        address.setPostalCode(code);
        return address.getPostalCode();
    }

    public long getAddressId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long addressId = -1;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_ADDRESS_ID},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
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
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getLicense() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String license = null;
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_LICENSE},
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
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
        long id = db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    /**
     * TODO: finish this
     *
     * @return
     */
    public String toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("location_id", id);
            json.put("name", name);
            json.put("manager_id", manager.getId());
            json.put("owner_id", owner.getId());
            json.put("agent_id", agent.getId());
            json.put("address_line1", this.getAddressLine(1));
            json.put("address_line2", this.getAddressLine(2));
            json.put("city", this.getCity());
            json.put("zip_code", this.getZipCode());
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return json.toString();
    }

    @Override
    public int compareTo(Location l) {
        int compare = toString().compareTo(l.toString());
        return compare;
    }

    public boolean equals(Location l) {
        return this.id == l.getId();
    }
}

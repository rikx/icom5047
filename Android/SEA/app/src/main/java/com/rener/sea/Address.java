package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by user on 5/1/15.
 */
public class Address {

    private DBHelper dbHelper;
    private long id = -1;


    public Address(long id, DBHelper dbHelper) {
        this.id = id;
        this.dbHelper = dbHelper;
    }

    public Address( long id, String address1, String address2, String city, String zipcode,DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(id)) { // can also verify if id == -1

        } else {
            this.id = create(address1,address2,city,zipcode);
        }

    }

    public long create(String address1, String address2, String city, String zipcode){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ADDRESS_LINE1, address1);
        values.put(DBSchema.ADDRESS_LINE2, address2);
        values.put(DBSchema.ADDRESS_CITY, city);
        values.put(DBSchema.ADDRESS_ZIPCODE, zipcode);

        long id = db.insert(DBSchema.TABLE_ADDRESS, null, values);
        db.close();
        return id;
    }



    private boolean exist(long address_id) {
        if (address_id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{DBSchema.ADDRESS_ID},
                DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(address_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    private boolean invoke(long address_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{DBSchema.ADDRESS_ID},
                DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(address_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)) {
                this.id = cursor.getLong(0);
            }
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    public long getId() {
        return id;
    }

    public String getAddress1() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String address = "";
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{DBSchema.ADDRESS_LINE1},
                DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                address = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return address;
    }

    public long setAddress1(String address1) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ADDRESS_LINE1, String.valueOf(address1));
        long id = db.update(DBSchema.TABLE_ADDRESS, values, DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getAddress2() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String address = "";
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{DBSchema.ADDRESS_LINE2},
                DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                address = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return address;
    }

    public long setAddress2(String address2) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ADDRESS_LINE2, String.valueOf(address2));
        long id = db.update(DBSchema.TABLE_ADDRESS, values, DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getAddressLine(int n) {
        switch (n){
            case 1:
                return getAddress1();
            case 2:
                return getAddress2();
            default:
                return "";
        }
    }
    public long setAddressLine(int n, String line) {
        switch (n){
            case 1:
                return setAddress1(line);
            case 2:
                return setAddress2(line);
            default:
                return -1;
        }
    }

    public String getCity() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String city = "";
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{DBSchema.ADDRESS_CITY},
                DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                city = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return city;
    }

    public long setCity(String city) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ADDRESS_CITY, String.valueOf(city));
        long id = db.update(DBSchema.TABLE_ADDRESS, values, DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getZipcode() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String zipcode = "";
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{DBSchema.ADDRESS_ZIPCODE},
                DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                zipcode = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }
        return zipcode;
    }

    public long setZipcode(String zipcode) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ADDRESS_ZIPCODE, String.valueOf(zipcode));
        long id = db.update(DBSchema.TABLE_ADDRESS, values, DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }
}

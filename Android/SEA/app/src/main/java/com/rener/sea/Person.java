package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * A class representing a person.
 */
public class Person implements Comparable<Person> {

    SEASchema dbHelper = null;
    private long id = -1;
    private String first_name = "";
    private String middle_name = "";
    private String last_name1 = "";
    private String last_name2 = "";
    private String email = "";
    private String phone_number = "";
    private int specializationID;

    public Person(long personID, SEASchema dbHelper) {
        this.dbHelper = dbHelper;
        invoke(personID);
    }

    public Person(long id, String first_name, String initial, String last_name1, String last_name2, String email, String phone_number, SEASchema dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(id)) {

        } else {
            this.id = create(first_name, initial, last_name1, last_name2, email, phone_number);
        }
    }

    public Person(long id) {
        this.id = id;
    }

    public Person(long id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name1 = last_name;
    }

    public Person(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name1 = last_name;
    }

    public Person(String name) {
        this.first_name = name;
    }

    private long create(String first_name, String initial, String last_name1, String last_name2, String email, String phone_number) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
//        values.put(DBSchema.PERSON_ID               , username);
        values.put(DBSchema.PERSON_FIRST_NAME, first_name);
        values.put(DBSchema.PERSON_MIDDLE_INITIAL, initial);
        values.put(DBSchema.PERSON_LAST_NAME1, last_name1);
        values.put(DBSchema.PERSON_LAST_NAME2, last_name2);
        values.put(DBSchema.PERSON_EMAIL, email);
//        values.put(DBSchema.PERSON_SPEC_ID          , password);
        values.put(DBSchema.PERSON_PHONE_NUMBER, phone_number);
        // generate a new salt
//            values.put(DBSchema.USER_SALT      , salt);
        long id = db.insert(DBSchema.TABLE_PERSON, null, values);
        db.close();
        return id;

    }


    private boolean exist(long person_id) {
        if (person_id == -1) return false;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_ID},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(person_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    private boolean invoke(long person_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_ID},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(person_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            this.id = cursor.getLong(0);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    public int getSpecializationID() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int specializationID = -1;
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_SPEC_ID},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            specializationID = cursor.getInt(0);
            db.close();
            cursor.close();
        }
        return specializationID;

    }

    public void setSpecializationID(int specializationID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_SPEC_ID, specializationID);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
//        return id;// if -1 error during update
    }

    public long getId() {

        return this.id;
    }

//    public long setID(long id) {
//        return this.id = id;
//    }

    public String getFirstName() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_FIRST_NAME},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            name = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return name;
    }

    public long setFirstName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_FIRST_NAME, name);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public String getMiddleName() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String initial = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_MIDDLE_INITIAL},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            initial = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return initial;
    }

    public long setMiddleName(String initial) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_MIDDLE_INITIAL, initial);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public boolean hasMiddleName() {
        String middle_name = getMiddleName();
        return middle_name != null || !middle_name.equals("");
    }

    public String getLastName1() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String last1 = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_LAST_NAME1},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            last1 = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return last1;
    }

    public long setLastName1(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_LAST_NAME1, name);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public String getLastName2() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String last2 = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_LAST_NAME1},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            last2 = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return last2;
    }

    public long setLastName2(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_LAST_NAME2, name);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    /**
     * Get the full name of a person with order "first name" and "last name".
     *
     * @return a string with the person's full name
     */
    public String getFullNameFirstLast() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String fullName = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_FIRST_NAME, DBSchema.PERSON_MIDDLE_INITIAL, DBSchema.PERSON_LAST_NAME1, DBSchema.PERSON_LAST_NAME2},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            if (cursor.getString(1) != null || !cursor.getString(1).equals(""))
                fullName = (cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3)).trim();
            else
                fullName = (cursor.getString(0) + " " + cursor.getString(2) + " " + cursor.getString(3)).trim();

            db.close();
            cursor.close();
        }
        return fullName;
    }

    public String getEmail() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String email = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_EMAIL},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            email = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return email;
    }

    public long setEmail(String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_EMAIL, email);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public boolean hasEmail() {
        String email = getEmail();
        return email != null || !email.equals("");
    }

    public String getPhoneNumber() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String phone = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_PHONE_NUMBER},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            phone = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return phone;
    }

    public long setPhoneNumber(String phone_number) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_PHONE_NUMBER, phone_number);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public boolean hasPhoneNumber() {
        String phone_number = getPhoneNumber();
        return phone_number != null || !phone_number.equals("");
    }

    public String toString() {
        return getFullNameFirstLast();
    }

    @Override
    public int compareTo(@NonNull Person other) {
        return toString().compareTo(other.toString());
    }

    /**
     * Determine whether the provided person is equivalent to this one by comparing their IDs
     *
     * @param other the other Person object
     * @return true if their IDs match
     */
    public boolean equals(Person other) {
        return this.id == other.getId();
    }
}

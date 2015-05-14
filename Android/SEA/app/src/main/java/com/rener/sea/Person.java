package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * A class representing a person.
 */
public class Person implements Comparable<Person> {

    DBHelper dbHelper = null;
    String dummy = null;
    String firstName = "";
    String midleName = "";
    String lastName1 = "";
    String lastName2 = "";

    private long id = -1;

    public Person(long personID, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        invoke(personID);
    }
    // this method is only for the use of DBHelper
    public Person(long id, String first_name, String initial, String last_name1, String last_name2, DBHelper db) {
        this.dbHelper = db;
        if(id > 0){
            this.id = id;
            this.firstName = first_name;
            this.midleName = initial;
            this.lastName1 = last_name1;
            this.lastName2 = last_name2;
        }
    }

    public Person(long id, String first_name, String initial, String last_name1, String last_name2, String email, String phone_number, DBHelper db) {
        this.dbHelper = db;
        //verify if exit
        if (exist(id)) {

        } else {
            this.id = create(first_name, initial, last_name1, last_name2, email, phone_number);
        }
    }

    public Person(String dummy) {
        this.dummy = dummy;
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
            if (!cursor.isNull(0))
                this.id = cursor.getLong(0);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

//    public int getSpecializationID() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        int specializationID = -1;
//        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_SPEC_ID},
//                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//            if (!cursor.isNull(0))
//                specializationID = cursor.getInt(0);
//            db.close();
//            cursor.close();
//        }
//        return specializationID;
//
//    }

//    public void setSpecializationID(int specializationID) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DBSchema.PERSON_SPEC_ID, specializationID);
//        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
//        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
//        db.close();
////        return id;// if -1 error during update
//    }

    public long getId() {

        return this.id;
    }

//    public long setID(long id) {
//        return this.id = id;
//    }

    public String getFirstName() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String name = "";
//        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_FIRST_NAME},
//                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//            if (!cursor.isNull(0))
//                name = cursor.getString(0);
//            db.close();
//            cursor.close();
//        }
        return firstName;
    }

    public long setFirstName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_FIRST_NAME, name);
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public String getMiddleName() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String initial = "";
//        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_MIDDLE_INITIAL},
//                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//            if (!cursor.isNull(0))
//                initial = cursor.getString(0);
//            db.close();
//            cursor.close();
//        }
        return midleName;
    }

    public long setMiddleName(String initial) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_MIDDLE_INITIAL, initial);
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        if(id > 0)
            midleName = initial;
        db.close();
        return id;
    }

    public boolean hasMiddleName() {
        String middle_name = getMiddleName();
        return middle_name != null || !"".equals(middle_name);
    }

    public String getLastName1() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String last1 = "";
//        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_LAST_NAME1},
//                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//            if (!cursor.isNull(0))
//                last1 = cursor.getString(0);
//            db.close();
//            cursor.close();
//        }
        return lastName1;
    }

    public long setLastName1(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_LAST_NAME1, name);
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        if(id > 0)
            lastName1 = name;
        return id;
    }

    public String getLastName2() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String last2 = "";
//        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_LAST_NAME2},
//                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//            if (!cursor.isNull(0))
//                last2 = cursor.getString(0);
//            db.close();
//            cursor.close();
//        }
        return lastName2;
    }

    public long setLastName2(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.PERSON_LAST_NAME2, name);
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        if(id > 0)
            lastName2 = name;
        return id;
    }

    /**
     * Get the full name of a person with order "first name" and "last name".
     *
     * @return a string with the person's full name
     */
    public String getFullNameFirstLast() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String fullName = "";
//        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_FIRST_NAME, DBSchema.PERSON_MIDDLE_INITIAL, DBSchema.PERSON_LAST_NAME1, DBSchema.PERSON_LAST_NAME2},
//                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//
//            if (cursor.isNull(1) || "".equals(cursor.getString(1)))
//                fullName = (cursor.getString(0) + " " + cursor.getString(2) + " " + (cursor.isNull(3) ? "" : cursor.getString(3))).trim();
//            else
//
//                fullName = (cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + (cursor.isNull(3) ? "" : cursor.getString(3))).trim();
//
//            db.close();
//            cursor.close();
//        }
        return (firstName + " " + ("".equals(midleName) ? "" : midleName + " ") + lastName1 +" "+ lastName2).trim();
    }

    public String getEmail() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String email = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_EMAIL},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
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
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public boolean hasEmail() {
        String email = getEmail();
        return email != null || !"".equals(email);
    }

    public String getPhoneNumber() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String phone = "";
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_PHONE_NUMBER},
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
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
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public boolean hasPhoneNumber() {
        String phone_number = getPhoneNumber();
        return phone_number != null || !"".equals(phone_number);
    }

    public String toString() {
        return id == -1 ? dummy : getFullNameFirstLast();
    }

    @Override
    public int compareTo(@NonNull Person other) {
        return toString().toLowerCase().compareTo(other.toString().toLowerCase());
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

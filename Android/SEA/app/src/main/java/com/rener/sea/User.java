package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * A class representing a single user.
 */
public class User {

    private long user_id = -1;
    private DBHelper dbHelper = null;


    public User(long user_id, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        invoke(user_id);
    }

    // to create a user
    public User(long user_id, long person_id, String username, String password, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(user_id)) { // can also verify if id == -1

        } else {
            this.user_id = create(person_id, username, password);
        }
    }

    private long create(long person_id, String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.USER_USERNAME, username);
        values.put(DBSchema.USER_PASSHASH, password);
        values.put(DBSchema.USER_PERSON_ID, person_id);
        // generate a new salt
//            values.put(DBSchema.USER_SALT      , salt);

        long id = db.insert(DBSchema.TABLE_USERS, null, values);
        db.close();
        return id;

    }


    private boolean exist(long user_id) {
        if (user_id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_ID},//,DBSchema.USER_USERNAME,DBSchema.USER_PASSHASH,DBSchema.USER_PASSHASH,DBSchema.USER_SALT
                DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
//            this.user_id = cursor.getLong(0);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    private boolean invoke(long user_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_ID},
                DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                this.user_id = cursor.getLong(0);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    public long getId() {

        return user_id;
    }


    public String getUsername() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = "";
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_USERNAME},
                DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                username = cursor.getString(0);
            db.close();
            cursor.close();
        }

        return username;
    }

    public long setUsername(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.USER_USERNAME, String.valueOf(username));
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_USERS, values, DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getPassword() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String pass = "";
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_PASSHASH},
                DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                pass = cursor.getString(0);
            db.close();
            cursor.close();
        }

        return pass;
    }

    public long setPassword(String password) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.USER_PASSHASH, String.valueOf(password));
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_USERS, values, DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)});
        db.close();
        return id;// if -1 error during update

    }

    public boolean authenticate(String password) {
        boolean auth = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String hash = "";
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_PASSHASH},
                DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                hash = cursor.getString(0);
            db.close();
            cursor.close();
        }
        //TODO: hashing algorithm
        if (hash != null) auth = password.equals(hash) ? true : false;
        return auth;
    }

    public Person getPerson() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long personID = -1;
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_PERSON_ID},
                DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                personID = cursor.getLong(0);
            db.close();
            cursor.close();
        }

        Person person = new Person(personID, dbHelper);

        return person;

    }

    public long setPerson(Person person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.USER_PERSON_ID, person.getId());
        values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_YES);
        long id = db.update(DBSchema.TABLE_USERS, values, DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getType() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String type = "";
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_TYPE},
                DBSchema.USER_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if (!cursor.isNull(0))
                type = cursor.getString(0);
            db.close();
            cursor.close();
        }

        return type;
    }
}

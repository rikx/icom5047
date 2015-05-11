package com.rener.sea;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 4/8/15.
 */
public final class DBHelper extends SQLiteOpenHelper {

    // declaration of all keys for the DB
    public static final String DATABASE_NAME = "seadb";
    private static int DATABASE_VERSION = 4;
    //    public static int SYNC_STATUS = 0;
    private static JSONObject dataSync = new JSONObject();
    private boolean dummyDB = false;
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
//        Log.i(this.toString(), "instanced " + context.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBSchema.ENABLE_FOREIGN_KEY);
        db.execSQL(DBSchema.CREATE_ADDRESS_TABLE);
        db.execSQL(DBSchema.CREATE_APPOINTMENTS_TABLE);
        db.execSQL(DBSchema.CREATE_CATEGORY_TABLE);
        db.execSQL(DBSchema.CREATE_DEVICES_TABLE);
        db.execSQL(DBSchema.CREATE_FLOWCHART_TABLE);
        db.execSQL(DBSchema.CREATE_ITEM_TABLE);
        db.execSQL(DBSchema.CREATE_LOCATION_TABLE);
        db.execSQL(DBSchema.CREATE_LOCATION_CATEGORY_TABLE);
        db.execSQL(DBSchema.CREATE_OPTION_TABLE);
        db.execSQL(DBSchema.CREATE_PERSON_TABLE);
        db.execSQL(DBSchema.CREATE_REPORT_TABLE);
        db.execSQL(DBSchema.CREATE_PATH_TABLE);
        db.execSQL(DBSchema.CREATE_SPECIALIZATION_TABLE);
        db.execSQL(DBSchema.CREATE_USERS_TABLE);
        db.execSQL(DBSchema.CREATE_USERS_SPECIALIZATION_TABLE);
        Log.i(this.toString(), "created");
        dummyDB = true;


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_FLOWCHART);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_LOCATION_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_OPTION);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_SPECIALIZATION);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_USERS_SPECIALIZATION);
        DATABASE_VERSION = newVersion;
        onCreate(db);
    }

    // TODO:
    public void deleteDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_FLOWCHART);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_LOCATION_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_OPTION);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_SPECIALIZATION);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.TABLE_USERS_SPECIALIZATION);
        onCreate(db);
    }
    // TODO: create table if not exist
    private void setSequence(long sequence){
        SQLiteDatabase db = getReadableDatabase();
        Log.i(this.toString(), "SQLITE_SEQUENCE = "+sequence);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_ADDRESS+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_APPOINTMENTS+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_CATEGORY+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_DEVICES+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_FLOWCHART+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_ITEM+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_LOCATION+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_LOCATION_CATEGORY+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_OPTION+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_PERSON+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_REPORT+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_PATH+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_SPECIALIZATION+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_USERS+"'");
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+sequence+" WHERE name = '"+DBSchema.TABLE_USERS_SPECIALIZATION+"'");
        db.close();
    }

    public List<User> getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_ID},
                null, null, null, null, null, null);
        ArrayList<User> users;
        users = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                users.add(new User(cursor.getLong(0), this));
            }

            db.close();
            cursor.close();

        }
        return users;


    }
    public Context getContext(){
        return this.context;
    }

    public List<Appointment> getAllAppointments() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_ID},
                null, null, null, null, DBSchema.APPOINTMENT_DATE + " DESC", null);
        ArrayList<Appointment> Appointments;
        Appointments = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Appointments.add(new Appointment(cursor.getLong(0), this,context.getResources().getString(R.string.date_format_medium)));
            }

            db.close();
            cursor.close();

        }
        return Appointments;

    }

    public List<Person> getAllPersons() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_ID},
                null, null, null, null, DBSchema.PERSON_FIRST_NAME + " COLLATE NOCASE", null);
        ArrayList<Person> persons;
        persons = new ArrayList<>();
//        Log.i(this.toString(), "Cursor " + cursor);
//        Log.i(this.toString(), "Cursor count " + cursor.getCount());
        if ((cursor != null) && (cursor.getCount() > 0)) {
//            Log.i(this.toString(), "Inside if");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                persons.add(new Person(cursor.getLong(0), this));
//                Log.i(this.toString(), "People created " + cursor.getLong(0));
            }

            db.close();
            cursor.close();

        }
//        Log.i(this.toString(), "persons not found");
        return persons;

    }

    public List<Flowchart> getAllFlowcharts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_ID},
                null, null, null, null, DBSchema.FLOWCHART_NAME + " COLLATE NOCASE", null);
        ArrayList<Flowchart> flowcharts;
        flowcharts = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                flowcharts.add(new Flowchart(cursor.getLong(0), this));
            }

            db.close();
            cursor.close();

        }
        return flowcharts;

    }

    public List<Location> getAllLocations() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_ID},
                null, null, null, null, DBSchema.LOCATION_NAME + " COLLATE NOCASE", null);
        ArrayList<Location> location;
        location = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                location.add(new Location(cursor.getLong(0), this));
            }

            db.close();
            cursor.close();

        }
        return location;

    }

    public List<Report> getAllReports() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_ID},
                null, null, null, null, DBSchema.REPORT_DATE_FILED + " COLLATE NOCASE", null);
        ArrayList<Report> reports;
        reports = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                reports.add(new Report(cursor.getLong(0), this));
            }

            db.close();
            cursor.close();

        }
        return reports;

    }

    public long createUser(String userName, String passwd, int pID, String salt) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBSchema.USER_USERNAME, userName);
        values.put(DBSchema.USER_PASSHASH, passwd);
        values.put(DBSchema.USER_PERSON_ID, pID);
        values.put(DBSchema.USER_SALT, salt);

        long id = db.insert(DBSchema.TABLE_USERS, null, values);
        db.close();
        return id;// if -1 error during insertion
    }

    public List<Item> getAllItems(long flowchartID) {
        SQLiteDatabase db = getReadableDatabase();
        long id = -1;
        Cursor cursor = db.query(DBSchema.TABLE_ITEM, new String[]{DBSchema.ITEM_ID},
                DBSchema.ITEM_FLOWCHART_ID + "=?", new String[]{String.valueOf(flowchartID)}, null, null, null, null);
        ArrayList<Item> items = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                items.add(new Item(cursor.getLong(0), this));
            }

        }
        return items;
    }

    public List<Option> getAllOptions(long itemID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{DBSchema.OPTION_ID},
                DBSchema.OPTION_PARENT_ID + "=?", new String[]{String.valueOf(itemID)}, null, null, null, null);
        ArrayList<Option> options = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                options.add(new Option(cursor.getLong(0), this));
            }

        }
        return options;
    }

    public User findUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_ID,
                        DBSchema.USER_USERNAME,
                        DBSchema.USER_PASSHASH,
                        DBSchema.USER_PERSON_ID,
                        DBSchema.USER_SALT
                },
                DBSchema.USER_USERNAME + "=?", new String[]{String.valueOf(username)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            User user = new User(cursor.getLong(0), this);
            db.close();
            cursor.close();

            return user;
        }
        return new User(-1, this);

    }

    //TODO: implement flag for loggin do not delete hash pass or username from pref file
    public boolean authLogin(String username, String password) {


        String oldUsername = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString(context.getString(R.string.key_saved_username), null);
        String oldPassword = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString(context.getString(R.string.key_saved_password), null);
        String oldHash = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString(context.getString(R.string.key_saved_passhash), "");
        User user = findUserByUsername(username);
        boolean userN = username.equals(oldUsername);
        boolean hash = oldHash.equals(user.getPassword());
        boolean pass = password.equals(oldPassword);
        boolean exist = user.getId() != -1 ? true:false;

        if(userN && pass && hash && exist){
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.setAction("AUTH_RESULT");
            intent.putExtra("RESULT", 1);
            context.sendBroadcast(intent);
        }else if((userN && pass && !hash && exist) || exist){ // password has changed ask server for auth

            loginAuthentication(username,password);

        }else { //ask db for credentials nothing local
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.setAction("AUTH_RESULT");
            intent.putExtra("RESULT", -200);
            context.sendBroadcast(intent);
        }
        return true;
//
//
//
//
//        if(username.equals(oldUsername)){ // the user exist and has ben logged before
//
//            if(password.equals(oldPassword)){
//                if(oldHash.equals(findUserByUsername(username).getPassword())){
//
//                }else{
//                    // the password has changed
//                    //call db
//                    // authenticate in the server
//                    logginAutentication(username,password);
//
////                    Intent intent = new Intent();
////                    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
////                    intent.setAction("AUTH_RESULT");
////                    intent.putExtra("RESULT", 0); // the password has changed
////                    context.sendBroadcast(intent);
//                }
//
//            }else { //the user exist but never logged in
//
//                // wrong password tray to sync
//
//
//
//
//
//            }
//
//            // send intent for for authentication
//        }else if(username.equals(findUserByUsername(username).getUsername())){  // the user name is in the local db but i don have the password
//
//
//
//        }else{ // the user is not in the local db sync users
//
//        }
//
//        return false;
////        User user = findUserByUsername(username);
//////		return user.authenticate(password);
////        return user.getId() == -1 ? false : true;
    }

    public Person findPersonById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_ID,
                },
                DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            Person person = new Person(cursor.getLong(0), this);
            db.close();
            cursor.close();

            return person;
        }
        return new Person(-1, this);
    }

    public Location findLocationById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{DBSchema.LOCATION_ID
                },
                DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            Location location = new Location(cursor.getLong(0), this);
            db.close();
            cursor.close();
            return location;
        }
        return new Location(-1, this);
    }


    public boolean fillDB() {
        Person person = new Person(1, "Temporal", null, "User", null, "temporal.user@rener.com", null,
                this);
        new Person(2, "Nelson", null, "Reyes", null, "nelson.reyes@upr.edu", null, this);
        new Person(3, "Enrique", null, "Rodriguez", null, "enrique.rodriguez2@upr.edu", null, this);
        new Person(4, "Ricardo", null, "Fuentes", null, "ricardo.fuentes@upr.edu", null, this);
        new Person(5, "Ramón", null, "Saldaña", null, "ramon.saldana@upr.edu", null, this);

        User blank = new User(1, 1, "", "", this);
        User nelson = new User(2, 2, "nelson.reyes", "iamnelson", this);
        User enrique = new User(3, 3, "enrique.rodriguez2", "iamenrique", this);
        User rick = new User(4, 4, "ricardo.fuentes", "iamricardo", this);
        User ramon = new User(5, 5, "ramon.saldana", "iamramon", this);

        //Dummy flowchart
        Flowchart fc = new Flowchart(1, 1, 10, 3, "Flowchart Test", "0", this);

        //Dummy items
        new Item(1, fc.getId(), "Is the cow sick?", Item.BOOLEAN, this);
        new Item(2, fc.getId(), "How would you categorize this problem?", Item.MULTIPLE_CHOICE, this);
        new Item(3, fc.getId(), "Record a description of the milk coloring, texture, and smell",
                Item.OPEN, this);
        new Item(4, fc.getId(), "Input amount of times cow eats a day", Item.CONDITIONAL, this);
        new Item(5, fc.getId(), "Recommendation 1", Item.RECOMMENDATION, this);
        new Item(6, fc.getId(), "Recommendation 2", Item.RECOMMENDATION, this);
        new Item(7, fc.getId(), "Recommendation 3", Item.RECOMMENDATION, this);
        new Item(8, fc.getId(), "Recommendation 4", Item.RECOMMENDATION, this);
        new Item(9, fc.getId(), "Recommendation 5", Item.RECOMMENDATION, this);
        new Item(10, fc.getId(), "End of flowchart test", Item.END, this);

        //Dummy options
        Option o1 = new Option(1, 1, 2, "Yes", this);
        Option o2 = new Option(2, 1, 5, "No", this);
        Option o3 = new Option(3, 2, 3, "Milk is discolored", this);
        Option o4 = new Option(4, 2, 6, "Injured leg", this);
        Option o5 = new Option(5, 2, 4, "Eating problems", this);
        Option o6 = new Option(6, 4, 8, "lt3", this);
        Option o7 = new Option(7, 4, 9, "ge3", this);
        Option o8 = new Option(8, 3, 7, "[user input that is a description]", this);
        Option o9 = new Option(9, 7, 10, "End", this);
        Option o10 = new Option(10, 6, 10, "End", this);
        Option o11 = new Option(11, 8, 10, "End", this);
        Option o12 = new Option(12, 5, 10, "End", this);
        Option o13 = new Option(13, 9, 10, "End", this);

        //Dummy location
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.ADDRESS_ID, 0);
        values.put(DBSchema.ADDRESS_LINE1, "Terace");
        values.put(DBSchema.ADDRESS_LINE2, "apt 1028");
        values.put(DBSchema.ADDRESS_CITY, "Mayagüez");
        values.put(DBSchema.ADDRESS_ZIPCODE, 682);
        long id = db.insert(DBSchema.TABLE_ADDRESS, null, values);
        Location loc = new Location(1, "El platanal", id, 1, 3, "jhagfljfdsg", 2, this);

        //Dummy report
        Report report = new Report(this, nelson);
        report.setName("Dummy Report");
//        report.setSubject(person);
        report.setLocation(loc);
        report.setFlowchart(fc);
        report.setNotes("these are some report notes");
        Path path = new Path(report.getId(), this);
        path.addEntry(o1);
        path.addEntry(o3);
        path.addEntry(o8, "description of something");
        path.addEntry(o9);

        return true;
    }

    public boolean getDummy() {
        // marron power
        SQLiteDatabase db = getReadableDatabase();
//        db.execSQL("select * from users where user_id = -1");
        db.close();
        return dummyDB;
    }

    private long setItem(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.ITEM_ID))
                    values.put(DBSchema.ITEM_ID, item.getLong(DBSchema.ITEM_ID));
                if (!item.isNull(DBSchema.ITEM_LABEL))
                    values.put(DBSchema.ITEM_LABEL, item.getString(DBSchema.ITEM_LABEL));
                if (!item.isNull(DBSchema.ITEM_TYPE))
                    values.put(DBSchema.ITEM_TYPE, item.getString(DBSchema.ITEM_TYPE));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_ITEM, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setPath(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.PATH_REPORT_ID))
                    values.put(DBSchema.PATH_REPORT_ID, item.getLong(DBSchema.PATH_REPORT_ID));
                if (!item.isNull(DBSchema.PATH_OPTION_ID))
                    values.put(DBSchema.PATH_OPTION_ID, item.getLong(DBSchema.PATH_OPTION_ID));
                if (!item.isNull(DBSchema.PATH_DATA))
                    values.put(DBSchema.PATH_DATA, item.getString(DBSchema.PATH_DATA));
                if(!item.isNull(DBSchema.PATH_SEQUENCE))
                    values.put(DBSchema.PATH_SEQUENCE, item.getLong(DBSchema.PATH_SEQUENCE));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.insertWithOnConflict(DBSchema.TABLE_PATH, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    //copy paste full
    private long setUsers_specialization(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                if(!item.isNull(DBSchema.USERS_SPECIALIZATION_USER_ID))
                    values.put(DBSchema.USERS_SPECIALIZATION_USER_ID, item.getLong(DBSchema.USERS_SPECIALIZATION_USER_ID));
                if(!item.isNull(DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID))
                    values.put(DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID, item.getString(DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID));
                db.insertWithOnConflict(DBSchema.TABLE_USERS_SPECIALIZATION, null, values, 5);
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return -1;
    }

    private long setOption(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.OPTION_ID))
                    values.put(DBSchema.OPTION_ID, item.getLong(DBSchema.OPTION_ID));
                if (!item.isNull(DBSchema.OPTION_PARENT_ID))
                    values.put(DBSchema.OPTION_PARENT_ID, item.getLong(DBSchema.OPTION_PARENT_ID));
                if (!item.isNull(DBSchema.OPTION_NEXT_ID))
                    values.put(DBSchema.OPTION_NEXT_ID, item.getLong(DBSchema.OPTION_NEXT_ID));
                if (!item.isNull(DBSchema.OPTION_LABEL))
                    values.put(DBSchema.OPTION_LABEL, item.getString(DBSchema.OPTION_LABEL));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_OPTION, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSpecialization(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.SPECIALIZATION_ID))
                    values.put(DBSchema.SPECIALIZATION_ID, item.getLong(DBSchema.SPECIALIZATION_ID));
                if (!item.isNull(DBSchema.SPECIALIZATION_NAME))
                    values.put(DBSchema.SPECIALIZATION_NAME, item.getString(DBSchema.SPECIALIZATION_NAME));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.insertWithOnConflict(DBSchema.TABLE_SPECIALIZATION, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;

    }

    private long setPerson(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.PERSON_ID))
                    values.put(DBSchema.PERSON_ID, item.getLong(DBSchema.PERSON_ID));
                if (!item.isNull(DBSchema.PERSON_LAST_NAME1))
                    values.put(DBSchema.PERSON_LAST_NAME1, item.getString(DBSchema.PERSON_LAST_NAME1));
                if (!item.isNull(DBSchema.PERSON_FIRST_NAME))
                    values.put(DBSchema.PERSON_FIRST_NAME, item.getString(DBSchema.PERSON_FIRST_NAME));
                if (!item.isNull(DBSchema.PERSON_EMAIL))
                    values.put(DBSchema.PERSON_EMAIL, item.getString(DBSchema.PERSON_EMAIL));
                // if(!item.isNull(DBSchema.PERSON_SPEC_ID))
//                values.put(DBSchema.PERSON_SPEC_ID, item.getLong(DBSchema.PERSON_SPEC_ID));
                if (!item.isNull(DBSchema.PERSON_LAST_NAME2))
                    values.put(DBSchema.PERSON_LAST_NAME2, item.getString(DBSchema.PERSON_LAST_NAME2));
                if (!item.isNull(DBSchema.PERSON_MIDDLE_INITIAL))
                    values.put(DBSchema.PERSON_MIDDLE_INITIAL, item.getString(DBSchema.PERSON_MIDDLE_INITIAL));
                if (!item.isNull(DBSchema.PERSON_PHONE_NUMBER))
                    values.put(DBSchema.PERSON_PHONE_NUMBER, item.getString(DBSchema.PERSON_PHONE_NUMBER));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_PERSON, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setAppointments(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.APPOINTMENT_ID))
                    values.put(DBSchema.APPOINTMENT_ID, item.getLong(DBSchema.APPOINTMENT_ID));
                if (!item.isNull(DBSchema.APPOINTMENT_DATE))
                    values.put(DBSchema.APPOINTMENT_DATE, item.getString(DBSchema.APPOINTMENT_DATE));
                if (!item.isNull(DBSchema.APPOINTMENT_TIME))
                    values.put(DBSchema.APPOINTMENT_TIME, item.getString(DBSchema.APPOINTMENT_TIME));
                if (!item.isNull(DBSchema.APPOINTMENT_REPORT_ID))
                    values.put(DBSchema.APPOINTMENT_REPORT_ID, item.getLong(DBSchema.APPOINTMENT_REPORT_ID));
                if (!item.isNull(DBSchema.APPOINTMENT_PURPOSE))
                    values.put(DBSchema.APPOINTMENT_PURPOSE, item.getString(DBSchema.APPOINTMENT_PURPOSE));
                if (!item.isNull(DBSchema.APPOINTMENT_MAKER_ID))
                    values.put(DBSchema.APPOINTMENT_MAKER_ID, item.getLong(DBSchema.APPOINTMENT_MAKER_ID));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_APPOINTMENTS, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    // not to be needed
    private long setDevices(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.DEVICE_ID))
                    values.put(DBSchema.DEVICE_ID, item.getLong(DBSchema.DEVICE_ID));
                if (!item.isNull(DBSchema.DEVICE_NAME))
                    values.put(DBSchema.DEVICE_NAME, item.getString(DBSchema.DEVICE_NAME));
                if (!item.isNull(DBSchema.DEVICE_ID_NUMBER))
                    values.put(DBSchema.DEVICE_ID_NUMBER, item.getLong(DBSchema.DEVICE_ID_NUMBER));
                if (!item.isNull(DBSchema.DEVICE_USER_ID))
                    values.put(DBSchema.DEVICE_USER_ID, item.getLong(DBSchema.DEVICE_USER_ID));
                // if(!item.isNull(DBSchema.DEVICE_LATEST_SYNC))
//                values.put(DBSchema.DEVICE_LATEST_SYNC, item.getLong(DBSchema.DEVICE_LATEST_SYNC));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.insertWithOnConflict(DBSchema.TABLE_DEVICES, null, values, 5);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setAddress(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.ADDRESS_ID))
                    values.put(DBSchema.ADDRESS_ID, item.getLong(DBSchema.ADDRESS_ID));
                if (!item.isNull(DBSchema.ADDRESS_LINE1))
                    values.put(DBSchema.ADDRESS_LINE1, item.getString(DBSchema.ADDRESS_LINE1));
                if (!item.isNull(DBSchema.ADDRESS_CITY))
                    values.put(DBSchema.ADDRESS_CITY, item.getString(DBSchema.ADDRESS_CITY));
                if (!item.isNull(DBSchema.ADDRESS_ZIPCODE))
                    values.put(DBSchema.ADDRESS_ZIPCODE, item.getString(DBSchema.ADDRESS_ZIPCODE));
                if (!item.isNull(DBSchema.ADDRESS_LINE2))
                    values.put(DBSchema.ADDRESS_LINE2, item.getString(DBSchema.ADDRESS_LINE2));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_ADDRESS, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setCategory(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.CATEGORY_ID))
                    values.put(DBSchema.CATEGORY_ID, item.getLong(DBSchema.CATEGORY_ID));
                if (!item.isNull(DBSchema.CATEGORY_NAME))
                    values.put(DBSchema.CATEGORY_NAME, item.getString(DBSchema.CATEGORY_NAME));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_CATEGORY, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setLocation_category(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.LOCATION_CATEGORY_LOCATION_ID))
                    values.put(DBSchema.LOCATION_CATEGORY_LOCATION_ID, item.getLong(DBSchema.LOCATION_CATEGORY_LOCATION_ID));
                if (!item.isNull(DBSchema.LOCATION_CATEGORY_CATEGORY_ID))
                    values.put(DBSchema.LOCATION_CATEGORY_CATEGORY_ID, item.getLong(DBSchema.LOCATION_CATEGORY_CATEGORY_ID));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.insertWithOnConflict(DBSchema.TABLE_LOCATION_CATEGORY, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setLocation(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.LOCATION_ID))
                    values.put(DBSchema.LOCATION_ID, item.getLong(DBSchema.LOCATION_ID));
                if (!item.isNull(DBSchema.LOCATION_NAME))
                    values.put(DBSchema.LOCATION_NAME, item.getString(DBSchema.LOCATION_NAME));
                if (!item.isNull(DBSchema.LOCATION_ADDRESS_ID))
                    values.put(DBSchema.LOCATION_ADDRESS_ID, item.getLong(DBSchema.LOCATION_ADDRESS_ID));
                if(!item.isNull(DBSchema.LOCATION_OWNER_ID))
                    values.put(DBSchema.LOCATION_OWNER_ID, item.getLong(DBSchema.LOCATION_OWNER_ID));
                if(!item.isNull(DBSchema.LOCATION_MANAGER_ID))
                    values.put(DBSchema.LOCATION_MANAGER_ID, item.getLong(DBSchema.LOCATION_MANAGER_ID));
                if (!item.isNull(DBSchema.LOCATION_LICENSE))
                    values.put(DBSchema.LOCATION_LICENSE, item.getString(DBSchema.LOCATION_LICENSE));
                if(!item.isNull(DBSchema.LOCATION_AGENT_ID))
                    values.put(DBSchema.LOCATION_AGENT_ID, item.getLong(DBSchema.LOCATION_AGENT_ID));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_LOCATION, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setReport(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.REPORT_ID))
                    values.put(DBSchema.REPORT_ID, item.getLong(DBSchema.REPORT_ID));
                if (!item.isNull(DBSchema.REPORT_CREATOR_ID))
                    values.put(DBSchema.REPORT_CREATOR_ID, item.getLong(DBSchema.REPORT_CREATOR_ID));
                if (!item.isNull(DBSchema.REPORT_LOCATION_ID))
                    values.put(DBSchema.REPORT_LOCATION_ID, item.getLong(DBSchema.REPORT_LOCATION_ID));
                // if(!item.isNull(DBSchema.REPORT_SUBJECT_ID))
//                values.put(DBSchema.REPORT_SUBJECT_ID, item.getLong(DBSchema.REPORT_SUBJECT_ID));
                if (!item.isNull(DBSchema.REPORT_FLOWCHART_ID))
                    values.put(DBSchema.REPORT_FLOWCHART_ID, item.getLong(DBSchema.REPORT_FLOWCHART_ID));
                if (!item.isNull(DBSchema.REPORT_NOTE))
                    values.put(DBSchema.REPORT_NOTE, item.getString(DBSchema.REPORT_NOTE));
                if (!item.isNull(DBSchema.REPORT_DATE_FILED))
                    values.put(DBSchema.REPORT_DATE_FILED, item.getString(DBSchema.REPORT_DATE_FILED));
                if (!item.isNull(DBSchema.REPORT_NAME))
                    values.put(DBSchema.REPORT_NAME, item.getString(DBSchema.REPORT_NAME));
                if (!item.isNull(DBSchema.REPORT_STATUS))
                    values.put(DBSchema.REPORT_STATUS, item.getLong(DBSchema.REPORT_STATUS));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_REPORT, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setUsers(JSONArray data) {
//        Log.i(this.toString(), "USERS : " + data);
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.USER_ID))
                    values.put(DBSchema.USER_ID, item.getLong(DBSchema.USER_ID));
                if (!item.isNull(DBSchema.USER_USERNAME))
                    values.put(DBSchema.USER_USERNAME, item.getString(DBSchema.USER_USERNAME));
                if (!item.isNull(DBSchema.USER_PASSHASH))
                    values.put(DBSchema.USER_PASSHASH, item.getString(DBSchema.USER_PASSHASH));
                if (!item.isNull(DBSchema.USER_PERSON_ID))
                    values.put(DBSchema.USER_PERSON_ID, item.getLong(DBSchema.USER_PERSON_ID));
                if (!item.isNull(DBSchema.USER_SALT))
                    values.put(DBSchema.USER_SALT, item.getString(DBSchema.USER_SALT));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_USERS, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setFlowchart(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                if (!item.isNull(DBSchema.FLOWCHART_ID))
                    values.put(DBSchema.FLOWCHART_ID, item.getLong(DBSchema.FLOWCHART_ID));
                if (!item.isNull(DBSchema.FLOWCHART_FIRST_ID))
                    values.put(DBSchema.FLOWCHART_FIRST_ID, item.getString(DBSchema.FLOWCHART_FIRST_ID));
                if (!item.isNull(DBSchema.FLOWCHART_NAME))
                    values.put(DBSchema.FLOWCHART_NAME, item.getString(DBSchema.FLOWCHART_NAME));
                if (!item.isNull(DBSchema.FLOWCHART_END_ID))
                    values.put(DBSchema.FLOWCHART_END_ID, item.getLong(DBSchema.FLOWCHART_END_ID));
                if (!item.isNull(DBSchema.FLOWCHART_CREATOR_ID))
                    values.put(DBSchema.FLOWCHART_CREATOR_ID, item.getString(DBSchema.FLOWCHART_CREATOR_ID));
                if (!item.isNull(DBSchema.FLOWCHART_VERSION))
                    values.put(DBSchema.FLOWCHART_VERSION, item.getString(DBSchema.FLOWCHART_VERSION));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_FLOWCHART, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private JSONArray getItem() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ITEM, new String[]{
                        DBSchema.ITEM_ID,
                        DBSchema.ITEM_FLOWCHART_ID,
                        DBSchema.ITEM_LABEL,
                        DBSchema.ITEM_TYPE
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.ITEM_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.ITEM_FLOWCHART_ID, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.ITEM_LABEL, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.ITEM_TYPE, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getPath() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PATH, new String[]{
                        DBSchema.PATH_REPORT_ID,
                        DBSchema.PATH_OPTION_ID,
                        DBSchema.PATH_DATA,
                        DBSchema.PATH_SEQUENCE
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.PATH_REPORT_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.PATH_OPTION_ID, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.PATH_DATA, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.PATH_SEQUENCE, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getUsers_specialization() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS_SPECIALIZATION,new String[] {
                        DBSchema.USERS_SPECIALIZATION_USER_ID,
                        DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if(!cursor.isNull(0))//
                            map.put(DBSchema.USERS_SPECIALIZATION_USER_ID,    cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(!cursor.isNull(1))//
                            map.put(DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID,    cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getOption() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{
                        DBSchema.OPTION_ID,
                        DBSchema.OPTION_PARENT_ID,
                        DBSchema.OPTION_NEXT_ID,
                        DBSchema.OPTION_LABEL
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.OPTION_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.OPTION_PARENT_ID, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.OPTION_NEXT_ID, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.OPTION_LABEL, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getSpecialization() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_SPECIALIZATION, new String[]{
                        DBSchema.SPECIALIZATION_ID,
                        DBSchema.SPECIALIZATION_NAME
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.SPECIALIZATION_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.SPECIALIZATION_NAME, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getPerson() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{
                        DBSchema.PERSON_ID,
                        DBSchema.PERSON_LAST_NAME1,
                        DBSchema.PERSON_FIRST_NAME,
                        DBSchema.PERSON_EMAIL,
//                        DBSchema.PERSON_SPEC_ID,
                        DBSchema.PERSON_LAST_NAME2,
                        DBSchema.PERSON_MIDDLE_INITIAL,
                        DBSchema.PERSON_PHONE_NUMBER
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.PERSON_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.PERSON_LAST_NAME1, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.PERSON_FIRST_NAME, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.PERSON_EMAIL, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        if (!cursor.isNull(4))
//                            map.put(DBSchema.PERSON_SPEC_ID, cursor.getString(4));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.PERSON_LAST_NAME2, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(5))
                            map.put(DBSchema.PERSON_MIDDLE_INITIAL, cursor.getString(5));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(6))
                            map.put(DBSchema.PERSON_PHONE_NUMBER, cursor.getString(6));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getAppointments() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{
                        DBSchema.APPOINTMENT_ID,
                        DBSchema.APPOINTMENT_DATE,
                        DBSchema.APPOINTMENT_TIME,
                        DBSchema.APPOINTMENT_REPORT_ID,
                        DBSchema.APPOINTMENT_PURPOSE,
                        DBSchema.APPOINTMENT_MAKER_ID
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.APPOINTMENT_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.APPOINTMENT_DATE, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.APPOINTMENT_TIME, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.APPOINTMENT_REPORT_ID, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.APPOINTMENT_PURPOSE, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(5))
                            map.put(DBSchema.APPOINTMENT_MAKER_ID, cursor.getString(5));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getDevices() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_DEVICES, new String[]{
                        DBSchema.DEVICE_ID,
                        DBSchema.DEVICE_NAME,
                        DBSchema.DEVICE_ID_NUMBER,
                        DBSchema.DEVICE_USER_ID,
                        DBSchema.DEVICE_LATEST_SYNC
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.DEVICE_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.DEVICE_NAME, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.DEVICE_ID_NUMBER, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.DEVICE_USER_ID, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.DEVICE_LATEST_SYNC, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getAddress() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS, new String[]{
                        DBSchema.ADDRESS_ID,
                        DBSchema.ADDRESS_LINE1,
                        DBSchema.ADDRESS_CITY,
                        DBSchema.ADDRESS_ZIPCODE,
                        DBSchema.ADDRESS_LINE2
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.ADDRESS_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.ADDRESS_LINE1, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.ADDRESS_CITY, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.ADDRESS_ZIPCODE, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.ADDRESS_LINE2, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getCategory() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_CATEGORY, new String[]{
                        DBSchema.CATEGORY_ID,
                        DBSchema.CATEGORY_NAME
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.CATEGORY_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.CATEGORY_NAME, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getLocation_category() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION_CATEGORY, new String[]{
                        DBSchema.LOCATION_CATEGORY_LOCATION_ID,
                        DBSchema.LOCATION_CATEGORY_CATEGORY_ID
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.LOCATION_CATEGORY_LOCATION_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.LOCATION_CATEGORY_CATEGORY_ID, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getLocation() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION, new String[]{
                        DBSchema.LOCATION_ID,
                        DBSchema.LOCATION_NAME,
                        DBSchema.LOCATION_ADDRESS_ID,
                        DBSchema.LOCATION_OWNER_ID,
                        DBSchema.LOCATION_MANAGER_ID,
                        DBSchema.LOCATION_LICENSE,
                        DBSchema.LOCATION_AGENT_ID
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.LOCATION_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.LOCATION_NAME, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.LOCATION_ADDRESS_ID, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.LOCATION_OWNER_ID, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.LOCATION_MANAGER_ID, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(5))
                            map.put(DBSchema.LOCATION_LICENSE, cursor.getString(5));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(6))
                            map.put(DBSchema.LOCATION_AGENT_ID, cursor.getString(6));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getReport() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{
                        DBSchema.REPORT_ID,
                        DBSchema.REPORT_CREATOR_ID,
                        DBSchema.REPORT_LOCATION_ID,
//                        DBSchema.REPORT_SUBJECT_ID,
                        DBSchema.REPORT_FLOWCHART_ID,
                        DBSchema.REPORT_NOTE,
                        DBSchema.REPORT_DATE_FILED,
                        DBSchema.REPORT_NAME,
                        DBSchema.REPORT_STATUS
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.REPORT_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.REPORT_CREATOR_ID, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.REPORT_LOCATION_ID, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        if (!cursor.isNull(3))
//                            map.put(DBSchema.REPORT_SUBJECT_ID, cursor.getString(3));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.REPORT_FLOWCHART_ID, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.REPORT_NOTE, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(5))
                            map.put(DBSchema.REPORT_DATE_FILED, cursor.getString(5));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(6))
                            map.put(DBSchema.REPORT_NAME, cursor.getString(6));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(6))
                            map.put(DBSchema.REPORT_STATUS, cursor.getString(7));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getUsers() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{
                        DBSchema.USER_ID,
                        DBSchema.USER_USERNAME,
                        DBSchema.USER_PASSHASH,
                        DBSchema.USER_PERSON_ID,
                        DBSchema.USER_SALT
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.USER_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.USER_USERNAME, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.USER_PASSHASH, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.USER_PERSON_ID, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.USER_SALT, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONArray getFlowchart() {

        JSONArray data;
        data = new JSONArray();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{
                        DBSchema.FLOWCHART_ID,
                        DBSchema.FLOWCHART_FIRST_ID,
                        DBSchema.FLOWCHART_NAME,
                        DBSchema.FLOWCHART_END_ID,
                        DBSchema.FLOWCHART_CREATOR_ID,
                        DBSchema.FLOWCHART_VERSION
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES}, null, null, null, null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    JSONObject map = new JSONObject();
                    try {
                        if (!cursor.isNull(0))
                            map.put(DBSchema.FLOWCHART_ID, cursor.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(1))
                            map.put(DBSchema.FLOWCHART_FIRST_ID, cursor.getString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(2))
                            map.put(DBSchema.FLOWCHART_NAME, cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(3))
                            map.put(DBSchema.FLOWCHART_END_ID, cursor.getString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(4))
                            map.put(DBSchema.FLOWCHART_CREATOR_ID, cursor.getString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!cursor.isNull(5))
                            map.put(DBSchema.FLOWCHART_VERSION, cursor.getString(5));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    data.put(map);
                }
        }
        db.close();
        cursor.close();
        return data;

    }

    private JSONObject getData() {

//        HashMap<String, String> json;
//        json = new HashMap<String, String >();
        JSONObject json = new JSONObject();

        try {

            json.put("flowchart", getFlowchart());
            json.put("item", getItem());
            json.put("path", getPath());
            json.put("users_specialization", getUsers_specialization());
            json.put("option", getOption());
            json.put("specialization", getSpecialization());
            json.put("person", getPerson());
            json.put("appointments", getAppointments());
            json.put("devices", getDevices());
            json.put("address", getAddress());
            json.put("category", getCategory());
            json.put("location_category", getLocation_category());
            json.put("location", getLocation());
            json.put("report", getReport());
            json.put("users", getUsers());
            Gson gson = new GsonBuilder().create();
            String temp = gson.toJson(json);
//            Log.i(this.toString(), "DUMP  = " + json.toString());
            return json;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private long setSyncItem(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_ITEM, values, DBSchema.ITEM_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.ITEM_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncPath(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_PATH, values, DBSchema.PATH_REPORT_ID + " =? AND " + DBSchema.PATH_OPTION_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.PATH_REPORT_ID)), String.valueOf(item.getLong(DBSchema.PATH_OPTION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    //copy paste full
    private long setSyncUsers_specialization(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_USERS_SPECIALIZATION, values, DBSchema.USERS_SPECIALIZATION_USER_ID + " =? AND " + DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.USERS_SPECIALIZATION_USER_ID)), String.valueOf(item.getLong(DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return -1;
    }

    private long setSyncOption(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_OPTION, values, DBSchema.OPTION_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.OPTION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncSpecialization(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_SPECIALIZATION, values, DBSchema.SPECIALIZATION_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.SPECIALIZATION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;

    }

    private long setSyncPerson(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_PERSON, values, DBSchema.PERSON_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.PERSON_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncAppointments(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_APPOINTMENTS, values, DBSchema.APPOINTMENT_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.APPOINTMENT_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    // not to be needed
    private long setSyncDevices(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_DEVICES, values, DBSchema.DEVICE_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.DEVICE_ID))});

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncAddress(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_ADDRESS, values, DBSchema.ADDRESS_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.ADDRESS_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncCategory(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_CATEGORY, values, DBSchema.CATEGORY_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.CATEGORY_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncLocation_category(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_LOCATION_CATEGORY, values, DBSchema.LOCATION_CATEGORY_LOCATION_ID + " =? AND " + DBSchema.LOCATION_CATEGORY_CATEGORY_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.LOCATION_CATEGORY_LOCATION_ID)), String.valueOf(item.getLong(DBSchema.LOCATION_CATEGORY_CATEGORY_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncLocation(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_LOCATION, values, DBSchema.LOCATION_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.LOCATION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncReport(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.REPORT_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncUsers(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_USERS, values, DBSchema.USER_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.USER_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    private long setSyncFlowchart(JSONArray data) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
                db.update(DBSchema.TABLE_FLOWCHART, values, DBSchema.FLOWCHART_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.FLOWCHART_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteItem(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_ITEM, DBSchema.ITEM_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.ITEM_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deletePath(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_PATH, DBSchema.PATH_REPORT_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.PATH_REPORT_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteUsers_specialization(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                db.delete(DBSchema.TABLE_USERS_SPECIALIZATION,DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID + " =? AND " + DBSchema.USERS_SPECIALIZATION_USER_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.USERS_SPECIALIZATION_SPECIALIZATION_ID)),String.valueOf(item.getLong(DBSchema.USERS_SPECIALIZATION_USER_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteOption(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                db.delete(DBSchema.TABLE_OPTION, DBSchema.OPTION_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.OPTION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteSpecialization(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_SPECIALIZATION, DBSchema.SPECIALIZATION_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.SPECIALIZATION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deletePerson(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_PERSON, DBSchema.PERSON_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.PERSON_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteAppointments(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_APPOINTMENTS, DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.APPOINTMENT_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteAddress(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_ADDRESS, DBSchema.ADDRESS_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.ADDRESS_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteCategory(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_CATEGORY, DBSchema.CATEGORY_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.CATEGORY_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteLocation_category(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_LOCATION_CATEGORY, DBSchema.LOCATION_CATEGORY_LOCATION_ID + " =? AND " + DBSchema.LOCATION_CATEGORY_CATEGORY_ID + " =? ", new String[]{String.valueOf(item.getLong(DBSchema.LOCATION_CATEGORY_LOCATION_ID)),String.valueOf(item.getLong(DBSchema.LOCATION_CATEGORY_CATEGORY_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteLocation(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_LOCATION, DBSchema.LOCATION_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.LOCATION_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteReport(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_REPORT, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.REPORT_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteUsers(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_USERS, DBSchema.USER_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.USER_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long deleteFlowchart(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for (i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject(i);
                long id = db.delete(DBSchema.TABLE_FLOWCHART, DBSchema.FLOWCHART_ID + "=?", new String[]{String.valueOf(item.getLong(DBSchema.FLOWCHART_ID))});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }

    public boolean isEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_ID},
                null, null, null, null, null, null);
        boolean flag = cursor != null ? (cursor.getCount() > 0) : false;
        db.close();
        cursor.close();
        // TODO: tes it
        // return flag;
        return flag || !dummyDB;

    }

    // TODO: set sync status
    public void setSyncDone() {

        try {
            setSyncItem(dataSync.getJSONArray(DBSchema.TABLE_ITEM));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncUsers_specialization(dataSync.getJSONArray(DBSchema.TABLE_USERS_SPECIALIZATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncOption(dataSync.getJSONArray(DBSchema.TABLE_OPTION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncSpecialization(dataSync.getJSONArray(DBSchema.TABLE_SPECIALIZATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncPerson(dataSync.getJSONArray(DBSchema.TABLE_PERSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncAppointments(dataSync.getJSONArray(DBSchema.TABLE_APPOINTMENTS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncAddress(dataSync.getJSONArray(DBSchema.TABLE_ADDRESS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncCategory(dataSync.getJSONArray(DBSchema.TABLE_CATEGORY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncLocation_category(dataSync.getJSONArray(DBSchema.TABLE_LOCATION_CATEGORY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncLocation(dataSync.getJSONArray(DBSchema.TABLE_LOCATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncReport(dataSync.getJSONArray(DBSchema.TABLE_REPORT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncPath(dataSync.getJSONArray(DBSchema.TABLE_PATH));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncUsers(dataSync.getJSONArray(DBSchema.TABLE_USERS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSyncFlowchart(dataSync.getJSONArray(DBSchema.TABLE_FLOWCHART));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setLocalData(JSONObject serverJSONObject){

        try {
            setItem(serverJSONObject.getJSONArray(DBSchema.TABLE_ITEM));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setUsers_specialization(serverJSONObject.getJSONArray(DBSchema.TABLE_USERS_SPECIALIZATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setOption(serverJSONObject.getJSONArray(DBSchema.TABLE_OPTION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setSpecialization(serverJSONObject.getJSONArray(DBSchema.TABLE_SPECIALIZATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setPerson(serverJSONObject.getJSONArray(DBSchema.TABLE_PERSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setAppointments(serverJSONObject.getJSONArray(DBSchema.TABLE_APPOINTMENTS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setAddress(serverJSONObject.getJSONArray(DBSchema.TABLE_ADDRESS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setCategory(serverJSONObject.getJSONArray(DBSchema.TABLE_CATEGORY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setLocation_category(serverJSONObject.getJSONArray(DBSchema.TABLE_LOCATION_CATEGORY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setLocation(serverJSONObject.getJSONArray(DBSchema.TABLE_LOCATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setReport(serverJSONObject.getJSONArray(DBSchema.TABLE_REPORT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setPath(serverJSONObject.getJSONArray(DBSchema.TABLE_PATH));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setUsers(serverJSONObject.getJSONArray(DBSchema.TABLE_USERS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            setFlowchart(serverJSONObject.getJSONArray(DBSchema.TABLE_FLOWCHART));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void deleteLocalData(JSONObject serverJSONObject){

        try {
            deleteItem(serverJSONObject.getJSONArray(DBSchema.TABLE_ITEM));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deletePath(serverJSONObject.getJSONArray(DBSchema.TABLE_PATH));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteUsers_specialization(serverJSONObject.getJSONArray(DBSchema.TABLE_USERS_SPECIALIZATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteOption(serverJSONObject.getJSONArray(DBSchema.TABLE_OPTION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteSpecialization(serverJSONObject.getJSONArray(DBSchema.TABLE_SPECIALIZATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deletePerson(serverJSONObject.getJSONArray(DBSchema.TABLE_PERSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteAppointments(serverJSONObject.getJSONArray(DBSchema.TABLE_APPOINTMENTS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteAddress(serverJSONObject.getJSONArray(DBSchema.TABLE_ADDRESS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteCategory(serverJSONObject.getJSONArray(DBSchema.TABLE_CATEGORY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteLocation_category(serverJSONObject.getJSONArray(DBSchema.TABLE_LOCATION_CATEGORY));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteLocation(serverJSONObject.getJSONArray(DBSchema.TABLE_LOCATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteReport(serverJSONObject.getJSONArray(DBSchema.TABLE_REPORT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteUsers(serverJSONObject.getJSONArray(DBSchema.TABLE_USERS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            deleteFlowchart(serverJSONObject.getJSONArray(DBSchema.TABLE_FLOWCHART));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    // TODO: set tuple as sync and set the path
    // puede devolver true todo el tiempo
    // send user id;

    public void syncDB() {
        String device_id = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);

        Log.i(this.toString(), "HTTP Sync called ");
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(DBSchema.POST_DEVICE_ID, device_id);

        if (!isEmpty()) { // initial sync is FULL
            params.put(DBSchema.POST_SYNC_TYPE, DBSchema.SYNC_FULL);
            client.post(DBSchema.SYNC_URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONObject = " + response.toString());
                    SQLiteDatabase db = getWritableDatabase();
                    try {

                        JSONObject status = response.getJSONObject(DBSchema.POST_SYNC_INF);
                        if(status.getInt(DBSchema.SYNC_STATUS) == 1) {
                            setLocalData(response.getJSONObject(DBSchema.POST_SERVER_DATA_NEW));
                        }else {

                            Log.i(this.toString(), "HTTP Sync success Transaction fail");
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    try {
                        JSONObject localJSONObject = response.getJSONObject(DBSchema.POST_SYNC_INF);
                        Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", localJSON = " + localJSONObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    intent.setAction("SYNC");
                    intent.putExtra("SYNC_RESULT", 200);
                    context.sendBroadcast(intent);

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONArray = " + response.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                    Log.i(this.toString(), "HTTP Sync failure : statusCode = " + statusCode + ", Header = " + headers.toString() + ", response = " + response);
                    switch (statusCode) {
                        case 404:
                            Intent intent404 = new Intent();
                            intent404.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            intent404.setAction("SYNC");
                            intent404.putExtra("SYNC_RESULT", 404);
                            context.sendBroadcast(intent404);
                            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();// resource Not Found
                            break;
                        case 500:
                            Intent intent500 = new Intent();
                            intent500.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            intent500.setAction("SYNC");
                            intent500.putExtra("SYNC_RESULT", 500);
                            context.sendBroadcast(intent500);
                            Toast.makeText(context, "Internal server error", Toast.LENGTH_LONG).show();// Internal Server Error
                            break;
                        default:
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            intent.setAction("SYNC");
                            intent.putExtra("SYNC_RESULT", -1);
                            context.sendBroadcast(intent);
                            Toast.makeText(context, "NPI", Toast.LENGTH_LONG).show();// no se que paso
                            break;


                    }
                }


            });

//            result.isFinished();
//            setSyncDone(data);

        } else { // database incremental
            String prefKey = context.getResources().getString(R.string.preference_file_key);
            String usernameKey = context.getResources().getString(R.string.key_saved_username);
            SharedPreferences sharedPref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
            String sUsername = sharedPref.getString(usernameKey, null);
            User currentUser = findUserByUsername(sUsername);
            long userID = currentUser.getId();
            String type = currentUser.getType();
            Log.i(this.toString(), "DEVICE ID = " + String.valueOf(device_id));
            params.put(DBSchema.POST_SYNC_TYPE, DBSchema.SYNC_INC);
            params.put(DBSchema.POST_USER_ID, userID);
            params.put(DBSchema.POST_USER_TYPE, type);

            dataSync = getData();

            params.put(DBSchema.POST_LOCAL_DATA, dataSync);

            client.post(DBSchema.SYNC_URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONObject = " + response.toString());
                    SQLiteDatabase db = getWritableDatabase();

                    try {


                        JSONObject status = response.getJSONObject(DBSchema.POST_SYNC_INF);
                        if(status.getInt(DBSchema.SYNC_STATUS) == 1) {

                            setLocalData(response.getJSONObject(DBSchema.POST_SERVER_DATA_NEW));
                            deleteLocalData(response.getJSONObject(DBSchema.POST_SERVER_DATA_DELETED));

                        }else {

                            Log.i(this.toString(), "HTTP Sync success Transaction fail");
                        }


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    intent.setAction("SYNC");
                    intent.putExtra("SYNC_RESULT", 200);
                    context.sendBroadcast(intent);
                    try {
                        JSONObject syncInf = response.getJSONObject(DBSchema.POST_SYNC_INF);
                        long status = syncInf.getLong(DBSchema.SYNC_STATUS);
                        Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + "server sync status response = " + status);
                        String  local_data = syncInf.getString("local_data");
                        Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + "JSON of local data from server = " + local_data);
                        if(status == DBSchema.STATUS_SUCCESS){
                            setSyncDone();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject syncJSONObject = response.getJSONObject(DBSchema.POST_SYNC_INF);
                        Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", sysncJSON = " + syncJSONObject.toString());
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONArray = " + response.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                    Log.i(this.toString(), "HTTP Sync failure : statusCode = " + statusCode + ", Header = " + headers.toString() + ", response = " + response);
                    switch (statusCode) {
                        case 404:
                            Intent intent404 = new Intent();
                            intent404.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            intent404.setAction("SYNC");
                            intent404.putExtra("SYNC_RESULT", 404);
                            context.sendBroadcast(intent404);
                            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();// resource Not Found
                            break;
                        case 500:
                            Intent intent500 = new Intent();
                            intent500.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            intent500.setAction("SYNC");
                            intent500.putExtra("SYNC_RESULT", 500);
                            context.sendBroadcast(intent500);
                            Toast.makeText(context, "Internal server error", Toast.LENGTH_LONG).show();// Internal Server Error
                            break;
                        default:
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            intent.setAction("SYNC");
                            intent.putExtra("SYNC_RESULT", -1);
                            context.sendBroadcast(intent);
                            Toast.makeText(context, "NPI", Toast.LENGTH_LONG).show();// no se que paso
                            break;


                    }
                }


            });

        }
    }


    public void syncDBFull() {
        deleteDB();
        getDummy();
        String device_id = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        String prefKey = context.getResources().getString(R.string.preference_file_key);
        String usernameKey = context.getResources().getString(R.string.key_saved_username);
        SharedPreferences sharedPref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        String sUsername = sharedPref.getString(usernameKey, null);
        long userID = findUserByUsername(sUsername).getId();
        Log.i(this.toString(), "HTTP Sync called ");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(DBSchema.POST_SYNC_TYPE, DBSchema.SYNC_FULL);
        params.put(DBSchema.POST_DEVICE_ID, device_id);

        client.post(DBSchema.SYNC_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONObject = " + response.toString());


            try {
                setUsers(response.getJSONObject(DBSchema.POST_SERVER_DATA_NEW).getJSONArray(DBSchema.TABLE_USERS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
                // initialize sequences
                try {
                    JSONObject status = response.getJSONObject(DBSchema.POST_SYNC_INF);
                    if(status.getInt(DBSchema.SYNC_STATUS) == 0) {
                        if (status.getInt("flag") == 1) {
                            Log.i(this.toString(), "SETTING BASE SEQUENCE");
                            long seq = status.getLong("base_sequence");
                            SQLiteDatabase db = getWritableDatabase();
                            Log.i(this.toString(), "SQLITE_SEQUENCE = "+seq);
//                            db.execSQL("CREATE TABLE IF NOT EXISTS SQLITE_SEQUENCE(name,seq)");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_ADDRESS+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_APPOINTMENTS+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_CATEGORY+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_DEVICES+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_FLOWCHART+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_ITEM+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_LOCATION+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_LOCATION_CATEGORY+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_OPTION+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_PERSON+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_REPORT+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_PATH+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_SPECIALIZATION+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_USERS+"'");
//                            db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = "+seq+" WHERE name = '"+DBSchema.TABLE_USERS_SPECIALIZATION+"'");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_ADDRESS+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_APPOINTMENTS+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_CATEGORY+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_FLOWCHART+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_ITEM+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_LOCATION+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_OPTION+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_PERSON+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_REPORT+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_PATH+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_SPECIALIZATION+"')");
                            db.execSQL("INSERT INTO sqlite_sequence(seq,name) VALUES("+seq+",'"+DBSchema.TABLE_USERS+"')");
//                            db.close();

//                            setSequence(status.getLong("base_sequence"));

                        }
                    }else {

                        Log.i(this.toString(), "HTTP Sync success Transaction fail");
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                intent.setAction("SYNC");
                intent.putExtra("SYNC_RESULT", 200);
                context.sendBroadcast(intent);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONArray = " + response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
//                Log.i(this.toString(), "HTTP Sync failure : statusCode = " + statusCode + ", Header = " + headers.toString() + ", response = " + response);
                switch (statusCode) {
                    case 404:
                        Intent intent404 = new Intent();
                        intent404.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        intent404.setAction("SYNC");
                        intent404.putExtra("SYNC_RESULT", 404);
                        context.sendBroadcast(intent404);
                        Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();// resource Not Found
                        break;
                    case 500:
                        Intent intent500 = new Intent();
                        intent500.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        intent500.setAction("SYNC");
                        intent500.putExtra("SYNC_RESULT", 500);
                        context.sendBroadcast(intent500);
                        Toast.makeText(context, "Internal server error", Toast.LENGTH_LONG).show();// Internal Server Error
                        break;
                    default:
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                        intent.setAction("SYNC");
                        intent.putExtra("SYNC_RESULT", -1);
                        context.sendBroadcast(intent);
                        Toast.makeText(context, "NPI", Toast.LENGTH_LONG).show();// no se que paso
                        break;


                }
            }


        });

    }



    public void loginAuthentication(String username, String password) {

        String device_id = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);

        Log.i(this.toString(), "HTTP Sync called ");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(DBSchema.AUTH_USER, DBSchema.SYNC_FULL);
        params.put(DBSchema.AUTH_PASS  , password);
        params.put(DBSchema.AUTH_DEVICE, device_id);

        client.post(DBSchema.AUTH_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONObject = " + response.toString());
//                SQLiteDatabase db = getWritableDatabase();
//                try {
//                    JSONObject status = response.getJSONObject(DBSchema.POST_SYNC_INF);
//                    if(status.getInt(DBSchema.SYNC_STATUS) == 0) {
//                        if (status.getInt("flag") == 1) {
//                            Log.i(this.toString(), "SETTING BASE SEQUENCE");
//                            setSequence(status.getLong("base_sequence"));
//
//                        }
//                    }else {
//
//                        Log.i(this.toString(), "HTTP Sync success Transaction fail");
//                    }
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//                }
//
//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//                intent.setAction("SYNC");
//                intent.putExtra("SYNC_RESULT", 200);
//                context.sendBroadcast(intent);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONArray = " + response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Log.i(this.toString(), "HTTP Sync failure : statusCode = " + statusCode + ", Header = " + headers.toString() + ", response = " + response);
//                switch (statusCode) {
//                    case 404:
//                        Intent intent404 = new Intent();
//                        intent404.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//                        intent404.setAction("SYNC");
//                        intent404.putExtra("SYNC_RESULT", 404);
//                        context.sendBroadcast(intent404);
//                        Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();// resource Not Found
//                        break;
//                    case 500:
//                        Intent intent500 = new Intent();
//                        intent500.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//                        intent500.setAction("SYNC");
//                        intent500.putExtra("SYNC_RESULT", 500);
//                        context.sendBroadcast(intent500);
//                        Toast.makeText(context, "Internal server error", Toast.LENGTH_LONG).show();// Internal Server Error
//                        break;
//                    default:
//                        Intent intent = new Intent();
//                        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//                        intent.setAction("SYNC");
//                        intent.putExtra("SYNC_RESULT", -1);
//                        context.sendBroadcast(intent);
//                        Toast.makeText(context, "NPI", Toast.LENGTH_LONG).show();// no se que paso
//                        break;
//
//
//                }
            }


        });

    }


}

package com.rener.sea;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by user on 4/8/15.
 */
public final class DBHelper extends SQLiteOpenHelper {

    // declaration of all keys for the DB
    public static final String DATABASE_NAME = "seadb";
    private static int DATABASE_VERSION = 1;
    private boolean dummyDB = false;
    private Context context;
    public static int SYNC_STATUS = 0;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Log.i(this.toString(), "instanced "+ context.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBSchema.CREATE_ADDRESS_TABLE);
        db.execSQL(DBSchema.CREATE_APPOINTMENTS_TABLE);
        db.execSQL(DBSchema.CREATE_CATEGORY_TABLE);
        db.execSQL(DBSchema.CREATE_DEVICES_TABLE);
        db.execSQL(DBSchema.CREATE_FLOWCHART_TABLE);
        db.execSQL(DBSchema.CREATE_ITEM_TABLE);
        db.execSQL(DBSchema.CREATE_LOCATION_TABLE);
        db.execSQL(DBSchema.CREATE_LOCATION_CATEGORY_TABLE);
        db.execSQL(DBSchema.CREATE_OPTION_TABLE);
        db.execSQL(DBSchema.CREATE_PATH_TABLE);
        db.execSQL(DBSchema.CREATE_PERSON_TABLE);
        db.execSQL(DBSchema.CREATE_REPORT_TABLE);
        db.execSQL(DBSchema.CREATE_SPECIALIZATION_TABLE);
        db.execSQL(DBSchema.CREATE_USERS_TABLE);
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
        onCreate(db);
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
    public List<Person> getAllPersons() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PERSON, new String[]{DBSchema.PERSON_ID},
                null, null, null, null, null, null);
        ArrayList<Person> persons;
        persons = new ArrayList<>();
        Log.i(this.toString(), "Cursor "+ cursor);
        Log.i(this.toString(), "Cursor count "+ cursor.getCount());
        if ((cursor != null) && (cursor.getCount() > 0)) {
            Log.i(this.toString(), "Inside if");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                persons.add(new Person(cursor.getLong(0), this));
                Log.i(this.toString(), "People created "+ cursor.getLong(0));
            }

            db.close();
            cursor.close();

        }
        Log.i(this.toString(), "persons not found");
        return persons;

    }
    public List<Flowchart> getAllFlowcharts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_ID},
                null, null, null, null, null, null);
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
                null, null, null, null, null, null);
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
                null, null, null, null, null, null);
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

    public User findUserByUsername(String username){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(DBSchema.TABLE_USERS,new String[] { DBSchema.USER_ID,
                        DBSchema.USER_USERNAME,
                        DBSchema.USER_PASSHASH,
                        DBSchema.USER_PERSON_ID,
                        DBSchema.USER_SALT
                },
                DBSchema.USER_USERNAME + "=?", new String[] {String.valueOf(username)},null,null,null,null);
        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            User user = new User(cursor.getLong(0), this);
            db.close();
            cursor.close();

            return user;
        }
        return new User(-1,this);

    }

    public boolean authLogin(String username, String password) {
        User user = findUserByUsername(username);
//		return user.authenticate(password);
        return true;
    }

    public Person findPersonById(long id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(DBSchema.TABLE_PERSON,new String[] {DBSchema.PERSON_ID,
                },
                DBSchema.PERSON_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            Person person = new Person(cursor.getLong(0), this);
            db.close();
            cursor.close();

            return person;
        }
        return new Person(-1,this);
    }

    public Location findLocationById(long id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(DBSchema.TABLE_LOCATION,new String[] {DBSchema.LOCATION_ID
                },
                DBSchema.LOCATION_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            Location location = new Location(cursor.getLong(0), this);
            db.close();
            cursor.close();
            return location;
        }
        return new Location(-1,this);
    }

    public boolean fillDB(){
        Person person = new Person(1,"Temporal",null,"User",null,"temporal.user@rener.com",null,
                this);
        new Person(2,"Nelson",null,"Reyes",null,"nelson.reyes@upr.edu",null,this);
        new Person(3,"Enrique",null,"Rodriguez",null,"enrique.rodriguez2@upr.edu",null,this);
        new Person(4,"Ricardo",null,"Fuentes",null,"ricardo.fuentes@upr.edu",null,this);
        new Person(5,"Ramón",null,"Saldaña",null,"ramon.saldana@upr.edu",null,this);

        User blank = new User(1,1,"","",this);
        User nelson = new User(2,2,"nelson.reyes","iamnelson",this);
        User enrique = new User(3,3,"enrique.rodriguez2","iamenrique",this);
        User rick = new User(4,4,"ricardo.fuentes","iamricardo",this);
        User ramon = new User(5,5,"ramon.saldana","iamramon",this);

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
        new Item(10,fc.getId(), "End of flowchart test", Item.END, this);

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
        Location loc =new Location(1,"El platanal", id, 1, 3,"jhagfljfdsg",2,this);

        //Dummy report
        Report report = new Report(this, nelson);
        report.setName("Dummy Report");
        report.setSubject(person);
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

    public boolean getDummy(){
        // marron power
        SQLiteDatabase db = getReadableDatabase();
//        db.execSQL("select * from users where user_id = -1");
        db.close();
        return dummyDB;
    }

    private long setItem(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.ITEM_ID, item.getLong(DBSchema.ITEM_ID));
                values.put(DBSchema.ITEM_LABEL, item.getString(DBSchema.ITEM_LABEL));
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
    private long setPath(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.PATH_REPORT_ID, item.getLong(DBSchema.PATH_REPORT_ID));
                values.put(DBSchema.PATH_OPTION_ID, item.getLong(DBSchema.PATH_OPTION_ID));
                values.put(DBSchema.PATH_DATA, item.getString(DBSchema.PATH_DATA));
//                values.put(DBSchema.PATH_SEQUENCE, item.getLong(DBSchema.PATH_SEQUENCE));
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
    private long setUsers_specialization(JSONArray data){
//        SQLiteDatabase db = this.getWritableDatabase();
//        int i = -1;
//        try {
//            for ( i = 0; i < data.length(); i++){
//                JSONObject item = data.getJSONObject(i);
//                ContentValues values = new ContentValues();
//                values.put(DBSchema.SPECIALIZATION_ID, item.getLong(DBSchema.SPECIALIZATION_ID));
//                values.put(DBSchema.SPECIALIZATION_NAME, item.getString(DBSchema.SPECIALIZATION_NAME));
//                db.insertWithOnConflict(DBSchema.TABLE_SPECIALIZATION, null, values, 5);
//                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        db.close();
        return -1;
    }
    private long setOption(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.OPTION_ID, item.getLong(DBSchema.OPTION_ID));
                values.put(DBSchema.OPTION_PARENT_ID, item.getLong(DBSchema.OPTION_PARENT_ID));
                values.put(DBSchema.OPTION_NEXT_ID, item.getLong(DBSchema.OPTION_NEXT_ID));
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
    private long setSpecialization(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DBSchema.SPECIALIZATION_ID, item.getLong(DBSchema.SPECIALIZATION_ID));
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
    private long setPerson(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.PERSON_ID, item.getLong(DBSchema.PERSON_ID));
                values.put(DBSchema.PERSON_LAST_NAME1, item.getString(DBSchema.PERSON_LAST_NAME1));
                values.put(DBSchema.PERSON_FIRST_NAME, item.getString(DBSchema.PERSON_FIRST_NAME));
                values.put(DBSchema.PERSON_EMAIL, item.getString(DBSchema.PERSON_EMAIL));
//                values.put(DBSchema.PERSON_SPEC_ID, item.getLong(DBSchema.PERSON_SPEC_ID));
                values.put(DBSchema.PERSON_LAST_NAME2, item.getString(DBSchema.PERSON_LAST_NAME2));
                values.put(DBSchema.PERSON_MIDDLE_INITIAL, item.getString(DBSchema.PERSON_MIDDLE_INITIAL));
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
    private long setAppointments(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.APPOINTMENT_ID, item.getLong(DBSchema.APPOINTMENT_ID));
                values.put(DBSchema.APPOINTMENT_DATE, item.getString(DBSchema.APPOINTMENT_DATE));
                values.put(DBSchema.APPOINTMENT_TIME, item.getString(DBSchema.APPOINTMENT_TIME));
                values.put(DBSchema.APPOINTMENT_REPORT_ID, item.getLong(DBSchema.APPOINTMENT_REPORT_ID));
                values.put(DBSchema.APPOINTMENT_PURPOSE, item.getString(DBSchema.APPOINTMENT_PURPOSE));
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
    private long setDevices(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.DEVICE_ID, item.getLong(DBSchema.DEVICE_ID));
                values.put(DBSchema.DEVICE_NAME, item.getString(DBSchema.DEVICE_NAME));
                values.put(DBSchema.DEVICE_ID_NUMBER, item.getLong(DBSchema.DEVICE_ID_NUMBER));
                values.put(DBSchema.DEVICE_USER_ID, item.getLong(DBSchema.DEVICE_USER_ID));
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
    private long setAddress(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.ADDRESS_ID, item.getLong(DBSchema.ADDRESS_ID));
                values.put(DBSchema.ADDRESS_LINE1, item.getString(DBSchema.ADDRESS_LINE1));
                values.put(DBSchema.ADDRESS_CITY, item.getString(DBSchema.ADDRESS_CITY));
                values.put(DBSchema.ADDRESS_ZIPCODE, item.getString(DBSchema.ADDRESS_ZIPCODE));
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
    private long setCategory(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.CATEGORY_ID, item.getLong(DBSchema.CATEGORY_ID));
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
    private long setLocation_category(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.LOCATION_CATEGORY_LOCATION_ID, item.getLong(DBSchema.LOCATION_CATEGORY_LOCATION_ID));
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
    private long setLocation(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.LOCATION_ID, item.getLong(DBSchema.LOCATION_ID));
                values.put(DBSchema.LOCATION_NAME, item.getString(DBSchema.LOCATION_NAME));
                values.put(DBSchema.LOCATION_ADDRESS_ID, item.getLong(DBSchema.LOCATION_ADDRESS_ID));
//                values.put(DBSchema.LOCATION_OWNER_ID, item.getLong(DBSchema.LOCATION_OWNER_ID));
//                values.put(DBSchema.LOCATION_MANAGER_ID, item.getLong(DBSchema.LOCATION_MANAGER_ID));
                values.put(DBSchema.LOCATION_LICENSE, item.getString(DBSchema.LOCATION_LICENSE));
//                values.put(DBSchema.LOCATION_AGENT_ID, item.getLong(DBSchema.LOCATION_AGENT_ID));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_LOCATION, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long setReport(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.REPORT_ID, item.getLong(DBSchema.REPORT_ID));
                values.put(DBSchema.REPORT_CREATOR_ID, item.getLong(DBSchema.REPORT_CREATOR_ID));
                values.put(DBSchema.REPORT_LOCATION_ID, item.getLong(DBSchema.REPORT_LOCATION_ID));
//                values.put(DBSchema.REPORT_SUBJECT_ID, item.getLong(DBSchema.REPORT_SUBJECT_ID));
                values.put(DBSchema.REPORT_FLOWCHART_ID, item.getLong(DBSchema.REPORT_FLOWCHART_ID));
                values.put(DBSchema.REPORT_NOTE, item.getString(DBSchema.REPORT_NOTE));
                values.put(DBSchema.REPORT_DATE_FILED, item.getString(DBSchema.REPORT_DATE_FILED));
                values.put(DBSchema.REPORT_NAME, item.getString(DBSchema.REPORT_NAME));
                values.put(DBSchema.MODIFIED, DBSchema.MODIFIED_NO);

                db.insertWithOnConflict(DBSchema.TABLE_REPORT, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.close();
        return i;
    }
    private long setUsers(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.USER_ID, item.getLong(DBSchema.USER_ID));
                values.put(DBSchema.USER_USERNAME, item.getString(DBSchema.USER_USERNAME));
                values.put(DBSchema.USER_PASSHASH, item.getString(DBSchema.USER_PASSHASH));
                values.put(DBSchema.USER_PERSON_ID, item.getLong(DBSchema.USER_PERSON_ID));
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

    private long setFlowchart(JSONArray data){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = -1;
        try {
            for ( i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(DBSchema.FLOWCHART_ID, item.getLong(DBSchema.FLOWCHART_ID));
                values.put(DBSchema.FLOWCHART_FIRST_ID, item.getString(DBSchema.FLOWCHART_FIRST_ID));
                values.put(DBSchema.FLOWCHART_NAME, item.getString(DBSchema.FLOWCHART_NAME));
                values.put(DBSchema.FLOWCHART_END_ID, item.getLong(DBSchema.FLOWCHART_END_ID));
                values.put(DBSchema.FLOWCHART_CREATOR_ID, item.getString(DBSchema.FLOWCHART_CREATOR_ID));
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


    private String getItem(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ITEM,new String[] {
                        DBSchema.ITEM_ID,
                        DBSchema.ITEM_FLOWCHART_ID,
                        DBSchema.ITEM_LABEL,
                        DBSchema.ITEM_TYPE
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ITEM_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ITEM_FLOWCHART_ID,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ITEM_LABEL,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ITEM_TYPE,    cursor.getString(3));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getPath(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PATH,new String[] {
                        DBSchema.PATH_REPORT_ID,
                        DBSchema.PATH_OPTION_ID,
                        DBSchema.PATH_DATA,
                        DBSchema.PATH_SEQUENCE
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PATH_REPORT_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PATH_OPTION_ID,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PATH_DATA,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PATH_SEQUENCE,    cursor.getString(3));
                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        String temp = gson.toJson(data);
        Log.i(this.toString(), "getPath  = " + temp);
        return temp;

    }
    private String getUsers_specialization(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(DBSchema.TABLE_SPECIALIZATION,new String[] {
//                        DBSchema.SPECIALIZATION_ID,
//                        DBSchema.SPECIALIZATION_NAME
//                },
//                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
//        if (cursor.moveToFirst()) {
//            if ((cursor != null) && (cursor.getCount() > 0))
//                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    if(!cursor.isNull(0))//
//                        map.put(DBSchema.SPECIALIZATION_ID,    cursor.getString(0));
//                    if(!cursor.isNull(0))//
//                        map.put(DBSchema.SPECIALIZATION_NAME,    cursor.getString(1));
//
//                    data.add(map);
//                }
//        }
//        db.close();
//        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getOption(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_OPTION,new String[] {
                        DBSchema.OPTION_ID,
                        DBSchema.OPTION_PARENT_ID,
                        DBSchema.OPTION_NEXT_ID,
                        DBSchema.OPTION_LABEL
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.OPTION_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.OPTION_PARENT_ID,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.OPTION_NEXT_ID,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.OPTION_LABEL,    cursor.getString(3));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getSpecialization(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_SPECIALIZATION,new String[] {
                        DBSchema.SPECIALIZATION_ID,
                        DBSchema.SPECIALIZATION_NAME
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.SPECIALIZATION_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.SPECIALIZATION_NAME,    cursor.getString(1));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getPerson(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PERSON,new String[] {
                        DBSchema.PERSON_ID,
                        DBSchema.PERSON_LAST_NAME1,
                        DBSchema.PERSON_FIRST_NAME,
                        DBSchema.PERSON_EMAIL,
                        DBSchema.PERSON_SPEC_ID,
                        DBSchema.PERSON_LAST_NAME2,
                        DBSchema.PERSON_MIDDLE_INITIAL,
                        DBSchema.PERSON_PHONE_NUMBER
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_LAST_NAME1,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_FIRST_NAME,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_EMAIL,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_SPEC_ID,    cursor.getString(4));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_LAST_NAME2,    cursor.getString(5));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_MIDDLE_INITIAL,    cursor.getString(6));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.PERSON_PHONE_NUMBER,    cursor.getString(7));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getAppointments(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS,new String[] {
                        DBSchema.APPOINTMENT_ID,
                        DBSchema.APPOINTMENT_DATE,
                        DBSchema.APPOINTMENT_TIME,
                        DBSchema.APPOINTMENT_REPORT_ID,
                        DBSchema.APPOINTMENT_PURPOSE,
                        DBSchema.APPOINTMENT_MAKER_ID
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.APPOINTMENT_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.APPOINTMENT_DATE,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.APPOINTMENT_TIME,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.APPOINTMENT_REPORT_ID,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.APPOINTMENT_PURPOSE,    cursor.getString(4));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.APPOINTMENT_MAKER_ID,    cursor.getString(5));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getDevices(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_DEVICES,new String[] {
                        DBSchema.DEVICE_ID,
                        DBSchema.DEVICE_NAME,
                        DBSchema.DEVICE_ID_NUMBER,
                        DBSchema.DEVICE_USER_ID,
                        DBSchema.DEVICE_LATEST_SYNC
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.DEVICE_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.DEVICE_NAME,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.DEVICE_ID_NUMBER,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.DEVICE_USER_ID,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.DEVICE_LATEST_SYNC,    cursor.getString(4));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getAddress(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_ADDRESS,new String[] {
                        DBSchema.ADDRESS_ID,
                        DBSchema.ADDRESS_LINE1,
                        DBSchema.ADDRESS_CITY,
                        DBSchema.ADDRESS_ZIPCODE,
                        DBSchema.ADDRESS_LINE2
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ADDRESS_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ADDRESS_LINE1,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ADDRESS_CITY,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ADDRESS_ZIPCODE,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.ADDRESS_LINE2,    cursor.getString(4));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getCategory(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_CATEGORY,new String[] {
                        DBSchema.CATEGORY_ID,
                        DBSchema.CATEGORY_NAME
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.CATEGORY_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.CATEGORY_NAME,    cursor.getString(1));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getLocation_category(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION_CATEGORY,new String[] {
                        DBSchema.LOCATION_CATEGORY_LOCATION_ID,
                        DBSchema.LOCATION_CATEGORY_CATEGORY_ID
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_CATEGORY_LOCATION_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_CATEGORY_CATEGORY_ID,    cursor.getString(1));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getLocation(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_LOCATION,new String[] {
                        DBSchema.LOCATION_ID,
                        DBSchema.LOCATION_NAME,
                        DBSchema.LOCATION_ADDRESS_ID,
                        DBSchema.LOCATION_OWNER_ID,
                        DBSchema.LOCATION_MANAGER_ID,
                        DBSchema.LOCATION_LICENSE,
                        DBSchema.LOCATION_AGENT_ID
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_NAME,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_ADDRESS_ID,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_OWNER_ID,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_MANAGER_ID,    cursor.getString(4));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_LICENSE,    cursor.getString(5));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.LOCATION_AGENT_ID,    cursor.getString(6));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getReport(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_REPORT,new String[] {
                        DBSchema.REPORT_ID,
                        DBSchema.REPORT_CREATOR_ID,
                        DBSchema.REPORT_LOCATION_ID,
                        DBSchema.REPORT_SUBJECT_ID,
                        DBSchema.REPORT_FLOWCHART_ID,
                        DBSchema.REPORT_NOTE,
                        DBSchema.REPORT_DATE_FILED,
                        DBSchema.REPORT_NAME
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_CREATOR_ID,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_LOCATION_ID,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_SUBJECT_ID,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_FLOWCHART_ID,    cursor.getString(4));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_NOTE,    cursor.getString(5));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_DATE_FILED,    cursor.getString(6));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.REPORT_NAME,    cursor.getString(7));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getUsers(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS,new String[] {
                        DBSchema.USER_ID,
                        DBSchema.USER_USERNAME,
                        DBSchema.USER_PASSHASH,
                        DBSchema.USER_PERSON_ID,
                        DBSchema.USER_SALT
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.USER_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.USER_USERNAME,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.USER_PASSHASH,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.USER_PERSON_ID,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.USER_SALT,    cursor.getString(4));

                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getFlowchart(){

        ArrayList<HashMap<String, String>> data;
        data = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART,new String[] {
                        DBSchema.FLOWCHART_ID,
                        DBSchema.FLOWCHART_FIRST_ID,
                        DBSchema.FLOWCHART_NAME,
                        DBSchema.FLOWCHART_END_ID,
                        DBSchema.FLOWCHART_CREATOR_ID,
                        DBSchema.FLOWCHART_VERSION
                },
                DBSchema.MODIFIED + "=?", new String[]{DBSchema.MODIFIED_YES},null,null,null,null);
        if (cursor.moveToFirst()) {
            if ((cursor != null) && (cursor.getCount() > 0))
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    if(!cursor.isNull(0))
                        map.put(DBSchema.FLOWCHART_ID,    cursor.getString(0));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.FLOWCHART_FIRST_ID,    cursor.getString(1));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.FLOWCHART_NAME,    cursor.getString(2));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.FLOWCHART_END_ID,    cursor.getString(3));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.FLOWCHART_CREATOR_ID,    cursor.getString(4));
                    if(!cursor.isNull(0))
                        map.put(DBSchema.FLOWCHART_VERSION,    cursor.getString(5));


                    data.add(map);
                }
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);

    }
    private String getData(){

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
            Log.i(this.toString(), "DUMP  = " + temp);
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isEmpty(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS,new String[] {DBSchema.USER_ID},
                null,null,null,null,null,null);
        boolean flag = cursor != null ? (cursor.getCount() > 0) : false;
        db.close();
        cursor.close();
        return flag;

    }
    // TODO: set sync status
    public void setSyncDone(HashMap<String, String> data){
//
//        try {
//            JSONArray items;
//            items = new JSONArray(data.get("flowchart"));
//            items
//            items = new JSONArray(data.get("item"));
//            items = new JSONArray(data.get("path"));
//            items = new JSONArray(data.get("users_specialization"));
//            items = new JSONArray(data.get("option");
//            items = new JSONArray(data.get("specialization"));
//            items = new JSONArray(data.get("person"));
//            items = new JSONArray(data.get("appointments"));
//            items = new JSONArray(data.get("devices"));
//            items = new JSONArray(data.get("address"));
//            items = new JSONArray(data.get("category"));
//            items = new JSONArray(data.get("location_category"));
//            items = new JSONArray(data.get("location"));
//            items = new JSONArray(data.get("report"));
//            items = new JSONArray(data.get("users"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//


    }
//    $user_ID = $_POST["user_id"];
//    $user_Type = $_POST["type"];
//    $local_Data = $_POST["data"]
//    $type_Of_Sync = $_POST["sync"];
    // TODO: set tuple as sync
    // puede devolver true todo el tiempo
    public boolean syncDB(){
        String prefKey = context.getResources().getString(R.string.preference_file_key);
        String usernameKey = context.getResources().getString(R.string.key_saved_username);
        SharedPreferences sharedPref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        String sUsername = sharedPref.getString(usernameKey, null);
        long userID = findUserByUsername(sUsername).getId();

        Log.i(this.toString(), "HTTP Sync called ");
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(!isEmpty()){
//            params.put("user_id", userID);
//            params.put("type", "admin");
//            params.put("sync", "update");

            // send user id
            //http://136.145.116.231:3000/synchronization/

            RequestHandle result  = client.post("http://136.145.116.231/mobile/test1.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONObject = " + response.toString());
                    SQLiteDatabase db = getWritableDatabase();
                    try {


                        setItem(response.getJSONArray("item"));
                        setPath(response.getJSONArray("path"));
                        setUsers_specialization(response.getJSONArray("users_specialization"));
                        setFlowchart(response.getJSONArray("flowchart"));
                        setOption(response.getJSONArray("option"));
                        setSpecialization(response.getJSONArray("specialization"));
                        setPerson(response.getJSONArray("person"));
                        setAppointments(response.getJSONArray("appointments"));
                        setDevices(response.getJSONArray("devices"));
                        setAddress(response.getJSONArray("address"));
                        setCategory(response.getJSONArray("category"));
                        setLocation_category(response.getJSONArray("location_category"));
                        setLocation(response.getJSONArray("location"));
                        setReport(response.getJSONArray("report"));
                        setUsers(response.getJSONArray("users"));
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    SYNC_STATUS = statusCode;

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONArray = " + response.toString());
//                    JSONObject firstEvent = null;
//                    try {
//                        for(int i = 0 ; i < response.length(); i++) {
//                            firstEvent = response.getJSONObject(i);
//                            String userName = firstEvent.getString(DBSchema.USER_USERNAME);
////                                System.out.println(userName);
//                            Log.i(this.toString(), "Username : "+userName);
//                            // update the db
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    SYNC_STATUS = statusCode;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                    Log.i(this.toString(), "HTTP Sync failure : statusCode = " + statusCode + ", Header = " + headers.toString() + ", response = " + response);
                    switch (statusCode) {
                        case 404:
                            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();// resource Not Found
                            break;
                        case 500:
                            Toast.makeText(context, "Internal server error", Toast.LENGTH_LONG).show();// Internal Server Error
                            break;
                        default:
                            Toast.makeText(context, "NPI", Toast.LENGTH_LONG).show();// no se que paso
                            break;


                    }
                    SYNC_STATUS = statusCode;
                }


            });
//            while (!SYNC_STATUS){
//
//            }
//            result.isFinished();
//            setSyncDone(data);

        }else{
//            The Database is empty
//            params.put("user_id", userID);
//            params.put("type", "admin");
//            params.put("data", getData());
            params.put("sync", "full");
            Gson gson = new GsonBuilder().create();
            String data = getData();
            params.put("data", gson.toJson(data));
            // send user id
            //http://136.145.116.231:3000/synchronization/
            client.post("http://136.145.116.231/mobile/test1.php",params ,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONObject = " + response.toString());
                    SQLiteDatabase db = getWritableDatabase();

                    try {


                        setItem(response.getJSONArray("item"));
                        setPath(response.getJSONArray("path"));
                        setUsers_specialization(response.getJSONArray("users_specialization"));
                        setFlowchart(response.getJSONArray("flowchart"));
                        setOption(response.getJSONArray("option"));
                        setSpecialization(response.getJSONArray("specialization"));
                        setPerson(response.getJSONArray("person"));
                        setAppointments(response.getJSONArray("appointments"));
                        setDevices(response.getJSONArray("devices"));
                        setAddress(response.getJSONArray("address"));
                        setCategory(response.getJSONArray("category"));
                        setLocation_category(response.getJSONArray("location_category"));
                        setLocation(response.getJSONArray("location"));
                        setReport(response.getJSONArray("report"));
                        setUsers(response.getJSONArray("users"));
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONArray = " + response.toString());
//                    JSONObject firstEvent = null;
//                    try {
//                        for(int i = 0 ; i < response.length(); i++) {
//                            firstEvent = response.getJSONObject(i);
//                            String userName = firstEvent.getString(DBSchema.USER_USERNAME);
////                                System.out.println(userName);
//                            Log.i(this.toString(), "Username : "+userName);
//                            // update the db
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable error){
                    Log.i(this.toString(), "HTTP Sync failure : statusCode = "+statusCode+", Header = "+headers.toString()+", response = "+response);
                    switch (statusCode) {
                        case 404:
                            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();// resource Not Found
                            break;
                        case 500:
                            Toast.makeText(context, "Internal server error", Toast.LENGTH_LONG).show();// Internal Server Error
                            break;
                        default:
                            Toast.makeText(context, "NPI", Toast.LENGTH_LONG).show();// no se que paso
                            break;


                    }

                }


            });

        }
        return SYNC_STATUS == 1;
    }


}

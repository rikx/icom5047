package com.rener.sea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
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

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    //
//        public long createAddress(Location loc){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.ADDRESS_ID      , loc.getId());
//            values.put(DBSchema.ADDRESS_LINE1   , loc.getAddressLine(1));
//            values.put(DBSchema.ADDRESS_CITY    , loc.getCity());
//            values.put(DBSchema.ADDRESS_ZIPCODE , loc.getZipCode());
//            values.put(DBSchema.ADDRESS_LINE2   , loc.getAddressLine(2));
//
//            long id = db.insert(DBSchema.TABLE_ADDRESS,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public void createLocationFull(Location loc){
//
//            long addressID = createAddress(loc);
//            loc.setAddressId(addressID);
//            long locationID = createLocation(loc);
//
//
//        }
//        // APPOINTMENT missing
//        public long createAppointments(){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.APPOINTMENT_ID          , VALUE);
//            values.put(DBSchema.APPOINTMENT_DATE        , VALUE);
//            values.put(DBSchema.APPOINTMENT_TIME        , VALUE);
//            values.put(DBSchema.APPOINTMENT_LOCATION_ID , VALUE);
//            values.put(DBSchema.APPOINTMENT_REPORT_ID   , VALUE);
//            values.put(DBSchema.APPOINTMENT_PURPOSE     , VALUE);
//
//            long id = db.insert(DBSchema.TABLE_APPOINTMENTS,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createCategory(){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.CATEGORY_ID   , VALUE);
//            values.put(DBSchema.CATEGORY_NAME , VALUE);
//
//            long id = db.insert(DBSchema.TABLE_CATEGORY,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        // not to be implemented in device
//        public long createDevices(){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.DEVICE_ID          , VALUE);
//            values.put(DBSchema.DEVICE_NAME        , VALUE);
//            values.put(DBSchema.DEVICE_ID_NUMBER   , VALUE);
//            values.put(DBSchema.DEVICE_USER_ID     , VALUE);
//            values.put(DBSchema.DEVICE_LATEST_SYNC , VALUE);
//
//            long id = db.insert(DBSchema.TABLE_DEVICES,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        // for testing purposes
//        public long createFlowchart(Flowchart flow){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.FLOWCHART_ID         , flow.getId());
//            values.put(DBSchema.FLOWCHART_FIRST_ID   , flow.getFirst().getId());
//            values.put(DBSchema.FLOWCHART_NAME       , flow.getName());
//            values.put(DBSchema.FLOWCHART_END_ID     , VALUE);// a flowchart has more than one possible end
//            values.put(DBSchema.FLOWCHART_CREATOR_ID , flow.getCreator().getId());
//            values.put(DBSchema.FLOWCHART_VERSION    , flow.getVersion());
//
//            long id = db.insert(DBSchema.TABLE_FLOWCHART,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createItem(Item item){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.ITEM_ID              , item.getId());
//            values.put(DBSchema.ITEM_FLOWCHART_ID    , VALUE);
//            values.put(DBSchema.ITEM_LABEL           , item.getLabel());
//            values.put(DBSchema.ITEM_POS_TOP         , VALUE);// ???
//            values.put(DBSchema.ITEM_POS_LEFT        , VALUE);// ???
//            values.put(DBSchema.ITEM_TYPE            , item.getType());
//
//            long id = db.insert(DBSchema.TABLE_ITEM,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createLocation(Location loc){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.LOCATION_ID                   , loc.getId());
//            values.put(DBSchema.LOCATION_NAME                 , loc.getName());
//            values.put(DBSchema.LOCATION_ADDRESS_ID           , loc.getAddressId());
//            values.put(DBSchema.LOCATION_OWNER_ID             , loc.getOwner().getId());
//            values.put(DBSchema.LOCATION_MANAGER_ID           , loc.getManager().getId());
//            values.put(DBSchema.LOCATION_LICENSE              , VALUE);
//            values.put(DBSchema.LOCATION_AGENT_ID             , loc.getAgent().getId());
//
//
//            long id = db.insert(DBSchema.TABLE_LOCATION,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createLocation_category(){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.LOCATION_CATEGORY_LOCATION_ID , VALUE);
//            values.put(DBSchema.LOCATION_CATEGORY_CATEGORY_ID , VALUE);
//
//            long id = db.insert(DBSchema.TABLE_LOCATION_CATEGORY,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createOption(Option option){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.OPTION_ID        ,   option.getId());
//            //values.put(DBSchema.OPTION_PARENT_ID ,   option.getParent());
//            values.put(DBSchema.OPTION_NEXT_ID   ,   option.getNext().getId());
//            values.put(DBSchema.OPTION_LABEL     ,   option.getLabel());
//
//            long id = db.insert(DBSchema.TABLE_OPTION,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        //    public long createPath(Path.Answer answer, long report){
//    //        SQLiteDatabase db = getWritableDatabase();
//    //        ContentValues values = new ContentValues();
//    //
//    //        values.put(PATH_REPORT_ID , report);
//    //        values.put(PATH_OPTION_ID, answer.getSelected().getId());
//    //        values.put(PATH_DATA, answer.getData());
//    //
//    //        long id = db.insert(TABLE_PATH,null,values);
//    //        db.close();
//    //        return id;// if -1 error during insertion
//    //
//    //    }
//        public long createPerson(Person person){
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.PERSON_ID             , person.getId());
//            values.put(DBSchema.PERSON_LAST_NAME1     , person.getLastName1());
//            values.put(DBSchema.PERSON_FIRST_NAME     , person.getFirstName());
//            values.put(DBSchema.PERSON_EMAIL          , person.getEmail());
//    //        values.put(DBSchema.PERSON_SPEC_ID        , VALUE);
//            values.put(DBSchema.PERSON_LAST_NAME2     , person.getLastName2());
//            values.put(DBSchema.PERSON_MIDDLE_INITIAL , person.getMiddleName());
//            values.put(DBSchema.PERSON_PHONE_NUMBER   , person.getPhoneNumber());
//
//            long id = db.insert(DBSchema.TABLE_PERSON,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createReport(Report report){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.REPORT_ID           , report.getId());
//            values.put(DBSchema.REPORT_CREATOR_ID   , report.getCreator().getId());
//            values.put(DBSchema.REPORT_LOCATION_ID  , report.getLocation().getId());
//            values.put(DBSchema.REPORT_SUBJECT_ID   , report.getSubject().getId());
//            values.put(DBSchema.REPORT_FLOWCHART_ID , report.getFlowchart().getId());
//    //        values.put(DBSchema.REPORT_NOTE         , report.getNote());
//            values.put(DBSchema.REPORT_DATE_FILED   , report.getDate().getTime());
//
//            long id = db.insert(DBSchema.TABLE_REPORT,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createSpecialization(){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.SPECIALIZATION_ID   , VALUE);
//            values.put(DBSchema.SPECIALIZATION_NAME , VALUE);
//
//            long id = db.insert(DBSchema.TABLE_SPECIALIZATION,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createUser(String username,String passw){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.USER_USERNAME  , username);
//            values.put(DBSchema.USER_PASSHASH  , passw);
//
//            long id = db.insert(DBSchema.TABLE_USERS,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        public long createUser(User user){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.USER_USERNAME  , user.getUsername());
//            values.put(DBSchema.USER_PASSHASH  , user.getPassword());
//
//            long id = db.insert(DBSchema.TABLE_USERS,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
//        // for testing purposes
//        public long createUsers(User user){
//            SQLiteDatabase db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//
//            values.put(DBSchema.USER_ID         , user.getId());
//            values.put(DBSchema.USER_USERNAME  , user.getUsername());
//            values.put(DBSchema.USER_PASSHASH  , user.getPassword());
//            values.put(DBSchema.USER_PERSON_ID , user.getPerson().getId());// redundant ???
//            values.put(DBSchema.USER_SALT      , VALUE);
//
//            long id = db.insert(DBSchema.TABLE_USERS,null,values);
//            db.close();
//            return id;// if -1 error during insertion
//
//        }
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
//
//        public Item findItemById(long id){
//            SQLiteDatabase db = getReadableDatabase();
//            Cursor cursor= db.query(DBSchema.TABLE_ITEM,new String[] { DBSchema.ITEM_FLOWCHART_ID,
//                            DBSchema.ITEM_ID,
//                            DBSchema.ITEM_LABEL,
//                            DBSchema.ITEM_TYPE
//                    },
//                    DBSchema.LOCATION_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
//            if((cursor != null) && (cursor.getCount() > 0)) {
//                cursor.moveToFirst();
//                Item item = new Item(cursor.getLong(1),cursor.getString(2),cursor.getString(3));
//
//                db.close();
//                cursor.close();
//
//                return item;
//            }
//            return null;
//
//        }
//    //    public List<Option> getOptions(long flowchartID){
//    //        SQLiteDatabase db = getReadableDatabase();
//    //        Cursor cursor= db.query(TABLE_OPTION,new String[] { OPTION_ID,
//    //                        OPTION_PARENT_ID,
//    //                        OPTION_NEXT_ID,
//    //                        OPTION_LABEL
//    //                },
//    //                LOCATION_ID + "=?", new String[] {String.valueOf(flowchartID)},null,null,null,null);
//    //        if((cursor != null) && (cursor.getCount() > 0)) {
//    //            cursor.moveToFirst();
//    //            List<Option> options = new ArrayList<Option>();
//    //            options.add(new Option(cursor.getLong(0),));
//    //            db.close();
//    //            cursor.close();
//    //
//    //            return options;
//    //        }
//    //        return null;
//    //
//    //    }
//
//
//

    //    public void createLocation(Location contact) {
    ////        json.put("location_id", id);
    ////        json.put("name", name);
    ////        json.put("manager_id", manager.getId());
    ////        json.put("owner_id", owner.getId());
    ////        json.put("agent_id", agent.getId());
    ////        json.put("address_line1", this.getAddressLine(1));
    ////        json.put("address_line2", this.getAddressLine(2));
    ////        json.put("city", this.getCity());
    ////        json.put("zip_code", this.getZipCode());
    //    }
    //
    //
    //        public List<Person> getPeople() {
    //            return people;
    //        }
    //
    //        public Person findPersonById(long id) {
    //            Person person = null;
    //            //Check if person is in memory
    //            for(Person p : people) {
    //                if(p.getId() == id) person = p;
    //            }
    //            if(person != null) return person;
    //            //TODO: Check if person is in local files
    //            //TODO: Query the server database
    //            return person;
    //        }
    //
    //        public List<Location> getLocations() {
    //            return locations;
    //        }
    //
    //        public Location findLocationById(long id) {
    //            Location location = null;
    //            for(Location l : locations) {
    //                if(l.getId() == id) location = l;
    //            }
    //            return location;
    //        }
    //
    //        public List<Report> getReports() {
    //            return reports;
    //        }
    //
    //        public Report findReportById(long id) {
    //            Report report = null;
    //            for (Report r : reports) {
    //                if(r.getId() == id) report = r;
    //            }
    //            return report;
    //        }
    //
    //        public List<Flowchart> getFlowcharts() {
    //            return flowcharts;
    //        }
    //
    //        public Flowchart findFlowchartById(long id) {
    //            Flowchart flowchart = null;
    //            for (Flowchart f : flowcharts) {
    //                if(f.getId() == id) flowchart = f;
    //            }
    //            return flowchart;
    //        }
    //
    //        public List<Item> getItems() {
    //            return items;
    //        }
    //
    //        public Item findItemById(long id) {
    //            Item item = null;
    //            for (Item i : items) {
    //                if(i.getId() == id) item = i;
    //            }
    //            return item;
    //        }
    //
    //        public List<Option> getOptions() {
    //            return options;
    //        }
    //
    //        public Option findOptionById(long id) {
    //            Option option = null;
    //            for(Option o : options) {
    //                if(o.getId() == id) option = o;
    //            }
    //            return option;
    //        }
    //
    //        public List<User> getUsers() {
    //            return users;
    //        }
    //
    //        public User findUserById(long id) {
    //            User user = null;
    //            for(User u : users) {
    //                if(u.getId() == id) user = u;
    //            }
    //            return user;
    //        }
    //



    public boolean getDummy(){
        // marron power
        SQLiteDatabase db = getReadableDatabase();
//        db.execSQL("select * from users where user_id = -1");
        db.close();
        return dummyDB;
    }
    //TODO: test it
    public ArrayList<HashMap<String, String>> getUsers() {
        ArrayList<HashMap<String, String>> users;
        users = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(DBSchema.TABLE_USERS,new String[] { DBSchema.USER_ID,
                        DBSchema.USER_USERNAME,
                        DBSchema.USER_PASSHASH,
                        DBSchema.USER_PERSON_ID,
                        DBSchema.USER_SALT
                },
                null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(DBSchema.USER_ID, cursor.getString(0));
                map.put(DBSchema.USER_USERNAME, cursor.getString(1));
                map.put(DBSchema.USER_PASSHASH, cursor.getString(2));
                map.put(DBSchema.USER_PERSON_ID, cursor.getString(3));
                map.put(DBSchema.USER_SALT, cursor.getString(4));
                users.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return users;
    }
    // TODO: add selection statement to only het un-sync elements
    public String unSyncUsersJson(){
        ArrayList<HashMap<String, String>> users;
        users = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(DBSchema.TABLE_USERS,new String[] { DBSchema.USER_ID,
                        DBSchema.USER_USERNAME,
                        DBSchema.USER_PASSHASH,
                        DBSchema.USER_PERSON_ID,
                        DBSchema.USER_SALT
                },
                null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(DBSchema.USER_ID, cursor.getString(0));
                map.put(DBSchema.USER_USERNAME, cursor.getString(1));
                map.put(DBSchema.USER_PASSHASH, cursor.getString(2));
                map.put(DBSchema.USER_PERSON_ID, cursor.getString(3));
                map.put(DBSchema.USER_SALT, cursor.getString(4));
                users.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(users);
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

                db.insertWithOnConflict(DBSchema.TABLE_ITEM, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                db.insertWithOnConflict(DBSchema.TABLE_PATH, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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
//                values.put(DBSchema.OPTION_LABEL, item.getString(DBSchema.OPTION_LABEL));

                db.insertWithOnConflict(DBSchema.TABLE_OPTION, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                db.insertWithOnConflict(DBSchema.TABLE_SPECIALIZATION, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                db.insertWithOnConflict(DBSchema.TABLE_PERSON, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                db.insertWithOnConflict(DBSchema.TABLE_APPOINTMENTS, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return i;
    }
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
                db.insertWithOnConflict(DBSchema.TABLE_DEVICES, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
//                values.put(DBSchema.ADDRESS_ZIPCODE, item.getLong(DBSchema.ADDRESS_ZIPCODE));
                values.put(DBSchema.ADDRESS_LINE2, item.getString(DBSchema.ADDRESS_LINE2));

                db.insertWithOnConflict(DBSchema.TABLE_ADDRESS, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                db.insertWithOnConflict(DBSchema.TABLE_CATEGORY, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                db.insertWithOnConflict(DBSchema.TABLE_LOCATION_CATEGORY, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                db.insertWithOnConflict(DBSchema.TABLE_LOCATION, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                db.insertWithOnConflict(DBSchema.TABLE_REPORT, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                db.insertWithOnConflict(DBSchema.TABLE_USERS, null, values, 5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return i;
    }

    private String getItem(JSONObject data){
        return "";
    }
    private String getPath(JSONObject data){
        return "";
    }
    private String getUsers_specialization(JSONObject data){
        return "";
    }
    private String getOption(JSONObject data){
        return "";
    }
    private String getSpecialization(JSONObject data){
        return "";
    }
    private String getPerson(JSONObject data){
        return "";
    }
    private String getAppointments(JSONObject data){
        return "";
    }
    private String getDevices(JSONObject data){
        return "";
    }
    private String getAddress(JSONObject data){
        return "";
    }
    private String getCategory(JSONObject data){
        return "";
    }
    private String getLocation_category(JSONObject data){
        return "";
    }
    private String getLocation(JSONObject data){
        return "";
    }
    private String getReport(JSONObject data){
        return "";
    }
    private String getUsers(JSONObject data){
        return "";
    }
    public void syncSQLiteMySQLDB(){
        Log.i(this.toString(), "HTTP Sync called ");
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  getUsers();
        if(dummyDB || userList.size()!=0){
//            if(// verify if local changes){
            Gson gson = new GsonBuilder().create();
            params.put("usersJSON", gson.toJson(userList));
            params.put("type", "admin");
            client.post("http://136.145.116.231/mobile/test.php",params ,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.i(this.toString(), "HTTP Sync success : i = " + statusCode + ", Header = " + headers.toString() + ", JSONObject = " + response.toString());
                    SQLiteDatabase db = getWritableDatabase();

                    try {


                        setItem(response.getJSONArray("item"));
                        setPath(response.getJSONArray("path"));
                        setUsers_specialization(response.getJSONArray("users_specialization"));
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
                        case 404:// resource Not Found
                            break;
                        case 500:// Internal Server Error
                            break;
                        default:// no se que paso
                            break;


                    }

                }


            });
//            }else{
//               The databases are in sync
//            }
        }else{
//            The Database is empty
        }
    }

}

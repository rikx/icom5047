package com.rener.sea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 4/8/15.
 */
public final class DBHelper extends SQLiteOpenHelper {

    // declaration of all keys for the DB
    public static final String DATABASE_NAME = "seadb";
    private static int DATABASE_VERSION = 1;

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

    public List<User> getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_USERS, new String[]{DBSchema.USER_ID},
                null, null, null, null, null, null);
        ArrayList<User> users;
        users = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext()) {
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
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext()) {
                persons.add(new Person(cursor.getLong(0), this));
            }

            db.close();
            cursor.close();

        }
        return persons;

    }
    public List<Flowchart> getAllFlowcharts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_FLOWCHART, new String[]{DBSchema.FLOWCHART_ID},
                null, null, null, null, null, null);
        ArrayList<Flowchart> flowcharts;
        flowcharts = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext()) {
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

            for (cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext()) {
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

            for (cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext()) {
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


            for (cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext()) {
                items.add(new Item(cursor.getLong(0), this));
            }

        }
        return items;
    }

    public List<Option> getAllOptions(long itemID) {
        SQLiteDatabase db = getReadableDatabase();
        long id = -1;
        Cursor cursor = db.query(DBSchema.TABLE_OPTION, new String[]{DBSchema.OPTION_ID},
                DBSchema.OPTION_PARENT_ID + "=?", new String[]{String.valueOf(itemID)}, null, null, null, null);
        ArrayList<Option> options = new ArrayList<>();
        if ((cursor != null) && (cursor.getCount() > 0)) {


            for (cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext()) {
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

    private boolean fillDB(){


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
    //        public boolean writeJSONToFile(String filename, String json) {
    //            FileOutputStream outputStream;
    //            try {
    //                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
    //                outputStream.write(json.getBytes());
    //                outputStream.close();
    //                return true;
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //                return false;
    //            }
    //        }
    //
    //        public void setDummyData() {
    //            people = new ArrayList<>();
    //            people.add(new Person(0, "Nelson", "Reyes"));
    //            people.add(new Person(1, "Enrique", "Rodriguez"));
    //            people.add(new Person(2, "Ricardo", "Fuentes"));
    //            people.add(new Person(3, "Ramón", "Saldaña"));
    //            people.add(new Person(6, "Betzabe", "Rodriguez"));
    //            people.add(new Person(4, "Gustavo", "Fring"));
    //            people.add(new Person(5, "Dennis", "Markowski"));
    //            people.add(new Person(6, "Generic", "Person"));
    //            people.add(new Person(7, "Generic", "Agent"));
    //            findPersonById(0).setEmail("nelson.reyes@upr.edu");
    //            findPersonById(0).setPhoneNumber("787-403-1082");
    //            findPersonById(6).setEmail("generic.person@upr.edu");
    //            findPersonById(6).setPhoneNumber("555-555-5555");
    //            findPersonById(7).setEmail("generic.agent@upr.edu");
    //            findPersonById(7).setPhoneNumber("555-555-5555");
    //
    //            locations = new ArrayList<>();
    //            locations.add(new Location(0, "Recinto Universitario de Mayagüez"));
    //            locations.add(new Location(1, "Finca Alzamorra"));
    //            locations.add(new Location(2, "Los Pollos Hermanos"));
    //            locations.add(new Location(3, "Betzabe's Office"));
    //            locations.add(new Location(4, "Generic Location"));
    //            findLocationById(2).setOwner(findPersonById(4));
    //            findLocationById(2).setManager(findPersonById(5));
    //            findLocationById(3).setOwner(findPersonById(6));
    //
    //            users = new ArrayList<>();
    //            users.add(new User(0, "", "", findPersonById(6)));
    //            users.add(new User(1, "nelson.reyes", "iamnelson", findPersonById(0)));
    //            users.add(new User(2, "enrique.rodriguez2", "iamenrique", findPersonById(1)));
    //            users.add(new User(3, "ricardo.fuentes", "iamricardo", findPersonById(2)));
    //            users.add(new User(4, "ramon.saldana", "iamramon", findPersonById(3)));
    //            users.add(new User(5, "betzabe.rodriguez", "iambetzabe", findPersonById(6)));
    //
    //            items = new ArrayList<>();
    //            items.add(new Item(1, "Is the cow sick?", Item.BOOLEAN));
    //            items.add(new Item(2, "How would you categorize this problem?", Item.MULTIPLE_CHOICE));
    //            items.add(new Item(3, "Record a description of the milk coloring, texture and smell",
    //                    Item.OPEN));
    //            items.add(new Item(4, "Input amount of times cow eats a day", Item.CONDITIONAL));
    //            items.add(new Item(5, "Recommendation 1", Item.RECOMMENDATION));
    //            items.add(new Item(6, "Recommendation 2", Item.RECOMMENDATION));
    //            items.add(new Item(7, "Recommendation 3", Item.RECOMMENDATION));
    //            items.add(new Item(8, "Recommendation 4", Item.RECOMMENDATION));
    //            items.add(new Item(9, "Recommendation 5", Item.RECOMMENDATION));
    //            items.add(new Item(10, "End of flowchart test", Item.END));
    //
    //            options = new ArrayList<>();
    //            options.add(new Option(1, findItemById(2),"Yes"));
    //            options.add(new Option(2, findItemById(5),"No"));
    //            findItemById(1).addOption(findOptionById(1));
    //            findItemById(1).addOption(findOptionById(2));
    //            options.add(new Option(3, findItemById(3),"Milk is discolored"));
    //            options.add(new Option(4, findItemById(6),"Injured leg"));
    //            options.add(new Option(5, findItemById(4),"Eating problems"));
    //            findItemById(2).addOption(findOptionById(3));
    //            findItemById(2).addOption(findOptionById(4));
    //            findItemById(2).addOption(findOptionById(5));
    //            options.add(new Option(6, findItemById(8),"USER INPUT"));
    //            options.add(new Option(7, findItemById(9),"USER INPUT"));
    //            findItemById(4).addOption(findOptionById(6));
    //            findItemById(4).addOption(findOptionById(7));
    //            options.add(new Option(8, findItemById(7), "USER INPUT"));
    //            findItemById(3).addOption(findOptionById(8));
    //
    //            flowcharts = new ArrayList<>();
    //            Flowchart fc1 = new Flowchart(1, "Test Flowchart");
    //            flowcharts.add(fc1);
    //            for(Item i : items) fc1.addItem(i);
    //
    //            reports = new ArrayList<>();
    //            reports.add(new Report(0,"My Report", findLocationById(4)));
    //            findReportById(0).setFlowchart(findFlowchartById(1));
    //            findReportById(0).setCreator(findUserById(0));
    //
    //            Log.i(this.toString(), "dummy data set");
    //        }
    //
    //        public static String peopleToJSON(List<Person> people) {
    //            JSONObject object = new JSONObject();
    //            for(Person p : people) {
    //                String json = p.toJSON();
    //                String sid = String.valueOf(p.getId());
    //                try {
    //                    object.put(sid, json);
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //            Log.i("JSON-List<Person>", object.toString());
    //            return object.toString();
    //        }
    //
    //        public static List<Person> peopleFromJSON(String json) {
    //            try {
    //                JSONObject object = new JSONObject(json);
    //                Iterator<String> iterator = object.keys();
    //                List list = new ArrayList<Person>();
    //                while(iterator.hasNext()) {
    //                    Person p = new Person(iterator.next());
    //                    list.add(p);
    //                }
    //                return list;
    //            } catch (JSONException e) {
    //                e.printStackTrace();
    //                return null;
    //            }
    //        }

}

package com.rener.sea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;


/**
 * Created by user on 4/8/15.
 */
public final class SEASchema extends SQLiteOpenHelper {

    private static String VALUE = "VALUEDUMMY";
    private static int DATABASE_VERSION = 1;
    // declaration of all keys for the DB
    public static final String
            DATABASE_NAME           = "seadb",
            TABLE_ADDRESS           = "address",
            ADDRESS_ID              = "address_id",
            ADDRESS_LINE1           = "address_line1",
            ADDRESS_CITY            = "city",
            ADDRESS_ZIPCODE         = "zipcode",
            ADDRESS_LINE2           = "address_line2",

    TABLE_APPOINTMENTS      = "appointments",
            APPOINTMENT_ID          = "appointment_id",
            APPOINTMENT_DATE        = "date",
            APPOINTMENT_TIME        = "time",
            APPOINTMENT_LOCATION_ID = "location_id",
            APPOINTMENT_REPORT_ID   = "report_id",
            APPOINTMENT_PURPOSE     = "purpose",

    TABLE_CATEGORY          = "category",
            CATEGORY_ID             = "category_id",
            CATEGORY_NAME           = "name",

    TABLE_DEVICES           = "devices",
            DEVICE_ID               = "device_id",
            DEVICE_NAME             = "name",
            DEVICE_ID_NUMBER        = "id_number",
            DEVICE_USER_ID          = "user_id",
            DEVICE_LATEST_SYNC      = "latest_sync",

    TABLE_FLOWCHART         = "flowchart",
            FLOWCHART_ID            =  "flowchart_id",
            FLOWCHART_FIRST_ID      =  "first_id",
            FLOWCHART_NAME          =  "name",
            FLOWCHART_END_ID        =  "end_id",
            FLOWCHART_CREATOR_ID    =  "creator_id",
            FLOWCHART_VERSION       =  "version",

    TABLE_ITEM              = "item",
            ITEM_ID                 = "item_id",
            ITEM_FLOWCHART_ID       = "flowchart_id",
            ITEM_LABEL              = "label",
            ITEM_POS_TOP            = "pos_top",
            ITEM_POS_LEFT           = "pos_left",
            ITEM_TYPE               = "type",

    TABLE_LOCATION          = "location",
            LOCATION_ID             = "location_id",
            LOCATION_NAME           = "name",
            LOCATION_ADDRESS_ID     = "address_id",
            LOCATION_OWNER_ID       = "owner_id",
            LOCATION_MANAGER_ID     = "manager_id",
            LOCATION_LICENSE        = "license",
            LOCATION_AGENT_ID       = "agent_id",

    TABLE_LOCATION_CATEGORY             = "location_category",
            LOCATION_CATEGORY_LOCATION_ID       = "location_id",
            LOCATION_CATEGORY_CATEGORY_ID       = "category_id",

    TABLE_OPTION            = "option",
            OPTION_ID               = "option_id",
            OPTION_PARENT_ID        = "parent_id",
            OPTION_NEXT_ID          = "next_id",
            OPTION_LABEL            = "label",

    TABLE_PATH              = "path",
            PATH_REPORT_ID          = "report_id",
            PATH_OPTION_ID          = "option_id",
            PATH_DATA               = "data",

    TABLE_PERSON            = "person",
            PERSON_ID               = "person_id",
            PERSON_LAST_NAME1       = "last_name1",
            PERSON_FIRST_NAME       = "first_name",
            PERSON_EMAIL            = "email",
            PERSON_SPEC_ID          = "spec_id",
            PERSON_LAST_NAME2       = "last_name2",
            PERSON_MIDDLE_INITIAL   = "middle_initial",
            PERSON_PHONE_NUMBER     = "phone_number",

    TABLE_REPORT            = "report",
            REPORT_ID               = "report_id",
            REPORT_CREATOR_ID       = "creator_id",
            REPORT_LOCATION_ID      = "location_id",
            REPORT_SUBJECT_ID       = "subject_id",
            REPORT_FLOWCHART_ID     = "flowchart_id",
            REPORT_NOTE             = "note",
            REPORT_DATE_FILED       = "date_filed",

    TABLE_SPECIALIZATION    = "specialization",
            SPECIALIZATION_ID       = "spec_id",
            SPECIALIZATION_NAME     = "name",

    TABLE_USERS             = "users",
            USER_ID                 = "user_id",
            USER__USERNAME          = "username",
            USER__PASSHASH          = "passhash",
            USER__PERSON_ID         = "person_id",
            USER__SALT              = "salt";

    private List<Person> people;
    private List<Location> locations;
    private List<Report> reports;
    private List<User> users;
    private List<Flowchart> flowcharts;
    private List<Item> items;
    private List<Option> options;

    public SEASchema(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ADDRESS  + "(" +
                ADDRESS_ID                          + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ADDRESS_LINE1                       + " TEXT," +
                ADDRESS_CITY                        + " TEXT," +
                ADDRESS_ZIPCODE                     + " INTEGER," +
                ADDRESS_LINE2                       + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_APPOINTMENTS + "(" +
                APPOINTMENT_ID                      + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                APPOINTMENT_DATE                    + " TEXT," +
                APPOINTMENT_TIME                    + " TEXT," +
                APPOINTMENT_LOCATION_ID             + " TEXT," +
                APPOINTMENT_REPORT_ID               + " TEXT," +
                APPOINTMENT_PURPOSE                 + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY     + "(" +
                CATEGORY_ID                         + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CATEGORY_NAME                       + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_DEVICES      + "(" +
                DEVICE_ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DEVICE_NAME                         + " TEXT," +
                DEVICE_ID_NUMBER                    + " TEXT," +
                DEVICE_USER_ID                      + " INTEGER," +
                DEVICE_LATEST_SYNC                  + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_FLOWCHART    + "(" +
                FLOWCHART_ID                        + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FLOWCHART_FIRST_ID                  + " INTEGER," +
                FLOWCHART_NAME                      + " TEXT," +
                FLOWCHART_END_ID                    + " INTEGER," +
                FLOWCHART_CREATOR_ID                + " INTEGER," +
                FLOWCHART_VERSION                   + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ITEM         + "(" +
                ITEM_ID                         + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ITEM_FLOWCHART_ID                   + " INTEGER," +
                ITEM_LABEL                          + " TEXT," +
                ITEM_POS_TOP                        + " REAL," +
                ITEM_POS_LEFT                       + " REAL," +
                ITEM_TYPE                           + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_LOCATION     + "(" +
                LOCATION_ID                         + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOCATION_NAME                       + " TEXT," +
                LOCATION_ADDRESS_ID                 + " INTEGER," +
                LOCATION_OWNER_ID                   + " INTEGER," +
                LOCATION_MANAGER_ID                 + " INTEGER," +
                LOCATION_LICENSE                    + " TEXT," +
                LOCATION_AGENT_ID       + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_LOCATION_CATEGORY + "(" +
                LOCATION_CATEGORY_LOCATION_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOCATION_CATEGORY_CATEGORY_ID       + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_OPTION       + "(" +
                OPTION_ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OPTION_PARENT_ID                    + " INTEGER," +
                OPTION_NEXT_ID                      + " INTEGER," +
                OPTION_LABEL                        + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_PATH         + "(" +
                PATH_REPORT_ID                      + " INTEGER," +
                PATH_OPTION_ID                      + " INTEGER," +
                PATH_DATA                           + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_PERSON       + "(" +
                PERSON_ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PERSON_LAST_NAME1                   + " TEXT," +
                PERSON_FIRST_NAME                   + " TEXT," +
                PERSON_EMAIL                        + " TEXT," +
                PERSON_SPEC_ID                      + " INTEGER," +
                PERSON_LAST_NAME2                   + " TEXT," +
                PERSON_MIDDLE_INITIAL               + " TEXT," +
                PERSON_PHONE_NUMBER                 + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_REPORT       + "(" +
                REPORT_ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                REPORT_CREATOR_ID                   + " INTEGER," +
                REPORT_LOCATION_ID                  + " INTEGER," +
                REPORT_SUBJECT_ID                   + " INTEGER," +
                REPORT_FLOWCHART_ID                 + " INTEGER," +
                REPORT_NOTE                         + " TEXT," +
                REPORT_DATE_FILED                   + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_SPECIALIZATION + "(" +
                SPECIALIZATION_ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SPECIALIZATION_NAME                 + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_USERS        + "(" +
                USER_ID                             + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER__USERNAME                      + " TEXT," +
                USER__PASSHASH                      + " TEXT," +
                USER__PERSON_ID                     + " INTEGER," +
                USER__SALT                          + " TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLOWCHART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIALIZATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        DATABASE_VERSION = newVersion;
        onCreate(db);
    }

    public long createAddress(Location loc){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ADDRESS_ID      , loc.getId());
        values.put(ADDRESS_LINE1   , loc.getAddressLine(1));
        values.put(ADDRESS_CITY    , loc.getCity());
        values.put(ADDRESS_ZIPCODE , loc.getZipCode());
        values.put(ADDRESS_LINE2   , loc.getAddressLine(2));

        long id = db.insert(TABLE_ADDRESS,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public void createLocationFull(Location loc){

        long addressID = createAddress(loc);
        loc.setAddressId(addressID);
        long locationID = createLocation(loc);


    }
    // APPOINTMENT missing
    public long createAppointments(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(APPOINTMENT_ID          , VALUE);
        values.put(APPOINTMENT_DATE        , VALUE);
        values.put(APPOINTMENT_TIME        , VALUE);
        values.put(APPOINTMENT_LOCATION_ID , VALUE);
        values.put(APPOINTMENT_REPORT_ID   , VALUE);
        values.put(APPOINTMENT_PURPOSE     , VALUE);

        long id = db.insert(TABLE_APPOINTMENTS,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public long createCategory(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CATEGORY_ID   , VALUE);
        values.put(CATEGORY_NAME , VALUE);

        long id = db.insert(TABLE_CATEGORY,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    // not to be implemented in device
    public long createDevices(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DEVICE_ID          , VALUE);
        values.put(DEVICE_NAME        , VALUE);
        values.put(DEVICE_ID_NUMBER   , VALUE);
        values.put(DEVICE_USER_ID     , VALUE);
        values.put(DEVICE_LATEST_SYNC , VALUE);

        long id = db.insert(TABLE_DEVICES,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    // for testing purposes
    public long createFlowchart(Flowchart flow){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FLOWCHART_ID         , flow.getId());
        values.put(FLOWCHART_FIRST_ID   , flow.getFirst().getId());
        values.put(FLOWCHART_NAME       , flow.getName());
        values.put(FLOWCHART_END_ID     , VALUE);// a flowchart has more than one possible end
        values.put(FLOWCHART_CREATOR_ID , flow.getCreator().getId());
        values.put(FLOWCHART_VERSION    , flow.getVersion());

        long id = db.insert(TABLE_FLOWCHART,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public long createItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_ID              , item.getId());
        values.put(ITEM_FLOWCHART_ID    , VALUE);
        values.put(ITEM_LABEL           , item.getLabel());
        values.put(ITEM_POS_TOP         , VALUE);// ???
        values.put(ITEM_POS_LEFT        , VALUE);// ???
        values.put(ITEM_TYPE            , item.getType());

        long id = db.insert(TABLE_ITEM,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public long createLocation(Location loc){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOCATION_ID                   , loc.getId());
        values.put(LOCATION_NAME                 , loc.getName());
        values.put(LOCATION_ADDRESS_ID           , loc.getAddressId());
        values.put(LOCATION_OWNER_ID             , loc.getOwner().getId());
        values.put(LOCATION_MANAGER_ID           , loc.getManager().getId());
        values.put(LOCATION_LICENSE              , VALUE);
        values.put(LOCATION_AGENT_ID             , loc.getAgent().getId());


        long id = db.insert(TABLE_LOCATION,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public long createLocation_category(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOCATION_CATEGORY_LOCATION_ID , VALUE);
        values.put(LOCATION_CATEGORY_CATEGORY_ID , VALUE);

        long id = db.insert(TABLE_LOCATION_CATEGORY,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public long createOption(Option option){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(OPTION_ID        ,   option.getId());
        //values.put(OPTION_PARENT_ID ,   option.getParent());
        values.put(OPTION_NEXT_ID   ,   option.getNext().getId());
        values.put(OPTION_LABEL     ,   option.getLabel());

        long id = db.insert(TABLE_OPTION,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
//    public long createPath(Path.Answer answer, long report){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(PATH_REPORT_ID , report);
//        values.put(PATH_OPTION_ID, answer.getSelected().getId());
//        values.put(PATH_DATA, answer.getData());
//
//        long id = db.insert(TABLE_PATH,null,values);
//        db.close();
//        return id;// if -1 error during insertion
//
//    }
    public long createPerson(Person person){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PERSON_ID             , person.getId());
        values.put(PERSON_LAST_NAME1     , person.getLastName1());
        values.put(PERSON_FIRST_NAME     , person.getFirstName());
        values.put(PERSON_EMAIL          , person.getEmail());
        values.put(PERSON_SPEC_ID        , VALUE);
        values.put(PERSON_LAST_NAME2     , person.getLastName2());
        values.put(PERSON_MIDDLE_INITIAL , person.getMiddleName());
        values.put(PERSON_PHONE_NUMBER   , person.getPhoneNumber());

        long id = db.insert(TABLE_PERSON,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public long createReport(Report report){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(REPORT_ID           , report.getId());
        values.put(REPORT_CREATOR_ID   , report.getCreator().getId());
        values.put(REPORT_LOCATION_ID  , report.getLocation().getId());
        values.put(REPORT_SUBJECT_ID   , report.getSubject().getId());
        values.put(REPORT_FLOWCHART_ID , report.getFlowchart().getId());
        values.put(REPORT_NOTE         , report.getNote());
        values.put(REPORT_DATE_FILED   , report.getDate().getTime());

        long id = db.insert(TABLE_REPORT,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public long createSpecialization(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SPECIALIZATION_ID   , VALUE);
        values.put(SPECIALIZATION_NAME , VALUE);

        long id = db.insert(TABLE_SPECIALIZATION,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    // for testing purposes
    public long createUsers(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ID         , user.getId());
        values.put(USER__USERNAME  , user.getUsername());
        values.put(USER__PASSHASH  , user.getPassword());
        values.put(USER__PERSON_ID , user.getPerson().getId());// redundant ???
        values.put(USER__SALT      , VALUE);

        long id = db.insert(TABLE_USERS,null,values);
        db.close();
        return id;// if -1 error during insertion

    }
    public Person findPersonById(long id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(TABLE_PERSON,new String[] {PERSON_ID,
                        PERSON_LAST_NAME1,
                        PERSON_FIRST_NAME,
                        PERSON_EMAIL,
                        PERSON_SPEC_ID,
                        PERSON_LAST_NAME2,
                        PERSON_MIDDLE_INITIAL,
                        PERSON_PHONE_NUMBER
                },
                PERSON_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
            Person person = new Person(cursor.getLong(0), cursor.getString(2), cursor.getString(1));
            person.setEmail(cursor.getString(3));
            person.setSpecializationID(cursor.getShort(4));
            person.setLastName2(cursor.getString(5));
            person.setMiddleName(cursor.getString(6));
            person.setPhoneNumber(cursor.getString(7));
            db.close();
            cursor.close();

            return person;
        }
        return null;
    }

    public Location findLocationById(long id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.query(TABLE_LOCATION,new String[] {LOCATION_ID,
                        LOCATION_NAME,
                        LOCATION_ADDRESS_ID,
                        LOCATION_OWNER_ID,
                        LOCATION_MANAGER_ID,
                        LOCATION_LICENSE,
                        LOCATION_AGENT_ID

                },
                LOCATION_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
            Location location = new Location(cursor.getLong(0),cursor.getString(1));
            location.setAddressId(cursor.getLong(2));
            location.setOwner(findPersonById(cursor.getLong(3)));
            location.setManager(findPersonById(cursor.getLong(4)));
            location.setLicense(cursor.getString(5));
            location.setAgent(findPersonById(cursor.getLong(6)));
            db.close();
            cursor.close();

            return location;
        }
        return null;
    }

//    public Item findItemById(long id){
//            SQLiteDatabase db = getReadableDatabase();
//            Cursor cursor= db.query(TABLE_ITEM,new String[] { ITEM_FLOWCHART_ID,
//                            ITEM_ID,
//                            ITEM_LABEL,
//                            ITEM_TYPE
//                    },
//                    LOCATION_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
//            if(cursor != null) {
//                cursor.moveToFirst();
//                Item item = new Item(cursor.getLong(0),cursor.getLong(1),cursor.getString(2),cursor.getString(3));
//                db.close();
//                cursor.close();
//
//                return item;
//            }
//            return null;
//
//    }
//    public List<Option> getOptions(long flowchartID){
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor= db.query(TABLE_OPTION,new String[] { OPTION_ID,
//                        OPTION_PARENT_ID,
//                        OPTION_NEXT_ID,
//                        OPTION_LABEL
//                },
//                LOCATION_ID + "=?", new String[] {String.valueOf(flowchartID)},null,null,null,null);
//        if(cursor != null) {
//            cursor.moveToFirst();
//            List<Option> options = new ArrayList<Option>();
//            options.add(new Option(cursor.getLong(0),));
//            db.close();
//            cursor.close();
//
//            return options;
//        }
//        return null;
//
//    }


    public class DummyData{
        private Person people;
        private Location locations;
        private Report reports;
        private User users;
        private Flowchart flowcharts;
        private List<Item> items;
        private List<Option> options;
        public void setDummyData() {

            people = new Person(0, "Nelson", "Reyes");
            people.setEmail("nelson.reyes@upr.edu");
            people.setPhoneNumber("787-403-1082");
            people.setEmail("nelson.reyes@upr.edu");
            people.setPhoneNumber("787-403-1082");
            users = new User(1, "nelson.reyes", "iamnelson", people);
            createPerson(people);
            createUsers(users);

            people = new Person(1, "Enrique", "Rodriguez");
            users = new User(2, "enrique.rodriguez2", "iamenrique", people);
            createPerson(people);
            createUsers(users);

            people = new Person(2, "Ricardo", "Fuentes");
            users = new User(3, "ricardo.fuentes", "iamricardo", people);
            createPerson(people);
            createUsers(users);

            people = new Person(3, "Ramón", "Saldaña");
            users = new User(4, "ramon.saldana", "iamramon", people);
            createPerson(people);
            createUsers(users);

            people = new Person(6, "Betzabe", "Rodriguez");
            users = new User(5, "betzabe.rodriguez", "iambetzabe", people);
            createPerson(people);
            createUsers(users);

            people = new Person(4, "Gustavo", "Fring");
            createPerson(people);
            createUsers(users);

            people = new Person(5, "Dennis", "Markowski");
            createPerson(people);
            createUsers(users);

            people = new Person(6, "Generic", "Person");
            users = new User(0, "", "", people);
            people.setEmail("generic.person@upr.edu");
            people.setPhoneNumber("555-555-5555");
            createPerson(people);
            createUsers(users);

            people = new Person(7, "Generic", "Agent");
            people.setEmail("generic.agent@upr.edu");
            people.setPhoneNumber("555-555-5555");
            createPerson(people);
            createUsers(users);

            locations = new Location(0, "Recinto Universitario de Mayagüez");
            locations = new Location(1, "Finca Alzamorra");
            locations = new Location(2, "Los Pollos Hermanos");
            locations = new Location(3, "Betzabe's Office");
            locations = new Location(4, "Generic Location");
            findLocationById(2).setOwner(findPersonById(4));
            findLocationById(2).setManager(findPersonById(5));
            findLocationById(3).setOwner(findPersonById(6));

//
//            Flowchart fc1 = new Flowchart(1, "Test Flowchart");
//
//            createItem(new Item(1, "Is the cow sick?", Item.BOOLEAN));
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
//
//            flowcharts.add(fc1);
//            for(Item i : items) fc1.addItem(i);
//
//            reports = new ArrayList<>();
//            reports.add(new Report(0,"My Report", findLocationById(4)));
//            findReportById(0).setFlowchart(findFlowchartById(1));
//            findReportById(0).setCreator(findUserById(0));
//
//            Log.i(this.toString(), "dummy data set");
        }


    }

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

//    public void setDummyData() {
//        people = new ArrayList<>();
//        people.add(new Person(0, "Nelson", "Reyes"));
//        people.add(new Person(1, "Enrique", "Rodriguez"));
//        people.add(new Person(2, "Ricardo", "Fuentes"));
//        people.add(new Person(3, "Ramón", "Saldaña"));
//        people.add(new Person(6, "Betzabe", "Rodriguez"));
//        people.add(new Person(4, "Gustavo", "Fring"));
//        people.add(new Person(5, "Dennis", "Markowski"));
//        people.add(new Person(6, "Generic", "Person"));
//        people.add(new Person(7, "Generic", "Agent"));
//        findPersonById(0).setEmail("nelson.reyes@upr.edu");
//        findPersonById(0).setPhoneNumber("787-403-1082");
//        findPersonById(6).setEmail("generic.person@upr.edu");
//        findPersonById(6).setPhoneNumber("555-555-5555");
//        findPersonById(7).setEmail("generic.agent@upr.edu");
//        findPersonById(7).setPhoneNumber("555-555-5555");
//
//        locations = new ArrayList<>();
//        locations.add(new Location(0, "Recinto Universitario de Mayagüez"));
//        locations.add(new Location(1, "Finca Alzamorra"));
//        locations.add(new Location(2, "Los Pollos Hermanos"));
//        locations.add(new Location(3, "Betzabe's Office"));
//        locations.add(new Location(4, "Generic Location"));
//        findLocationById(2).setOwner(findPersonById(4));
//        findLocationById(2).setManager(findPersonById(5));
//        findLocationById(3).setOwner(findPersonById(6));
//
//        users = new ArrayList<>();
//        users.add(new User(0, "", "", findPersonById(6)));
//        users.add(new User(1, "nelson.reyes", "iamnelson", findPersonById(0)));
//        users.add(new User(2, "enrique.rodriguez2", "iamenrique", findPersonById(1)));
//        users.add(new User(3, "ricardo.fuentes", "iamricardo", findPersonById(2)));
//        users.add(new User(4, "ramon.saldana", "iamramon", findPersonById(3)));
//        users.add(new User(5, "betzabe.rodriguez", "iambetzabe", findPersonById(6)));
//
//        items = new ArrayList<>();
//        items.add(new Item(1, "Is the cow sick?", Item.BOOLEAN));
//        items.add(new Item(2, "How would you categorize this problem?", Item.MULTIPLE_CHOICE));
//        items.add(new Item(3, "Record a description of the milk coloring, texture and smell",
//                Item.OPEN));
//        items.add(new Item(4, "Input amount of times cow eats a day", Item.CONDITIONAL));
//        items.add(new Item(5, "Recommendation 1", Item.RECOMMENDATION));
//        items.add(new Item(6, "Recommendation 2", Item.RECOMMENDATION));
//        items.add(new Item(7, "Recommendation 3", Item.RECOMMENDATION));
//        items.add(new Item(8, "Recommendation 4", Item.RECOMMENDATION));
//        items.add(new Item(9, "Recommendation 5", Item.RECOMMENDATION));
//        items.add(new Item(10, "End of flowchart test", Item.END));
//
//        options = new ArrayList<>();
//        options.add(new Option(1, findItemById(2),"Yes"));
//        options.add(new Option(2, findItemById(5),"No"));
//        findItemById(1).addOption(findOptionById(1));
//        findItemById(1).addOption(findOptionById(2));
//        options.add(new Option(3, findItemById(3),"Milk is discolored"));
//        options.add(new Option(4, findItemById(6),"Injured leg"));
//        options.add(new Option(5, findItemById(4),"Eating problems"));
//        findItemById(2).addOption(findOptionById(3));
//        findItemById(2).addOption(findOptionById(4));
//        findItemById(2).addOption(findOptionById(5));
//        options.add(new Option(6, findItemById(8),"USER INPUT"));
//        options.add(new Option(7, findItemById(9),"USER INPUT"));
//        findItemById(4).addOption(findOptionById(6));
//        findItemById(4).addOption(findOptionById(7));
//        options.add(new Option(8, findItemById(7), "USER INPUT"));
//        findItemById(3).addOption(findOptionById(8));
//
//        flowcharts = new ArrayList<>();
//        Flowchart fc1 = new Flowchart(1, "Test Flowchart");
//        flowcharts.add(fc1);
//        for(Item i : items) fc1.addItem(i);
//
//        reports = new ArrayList<>();
//        reports.add(new Report(0,"My Report", findLocationById(4)));
//        findReportById(0).setFlowchart(findFlowchartById(1));
//        findReportById(0).setCreator(findUserById(0));
//
//        Log.i(this.toString(), "dummy data set");
//    }

//    public class DBService extends Service {
//
//        private final IBinder mBinder = new DBBinder();
//        private List<Person> people;
//        private List<Location> locations;
//        private List<Report> reports;
//        private List<User> users;
//        private List<Flowchart> flowcharts;
//        private List<Item> items;
//        private List<Option> options;
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            Log.i("DBService", "started");
//            return super.onStartCommand(intent, flags, startId);
//        }
//
//        @Override
//        public void onCreate() {
//            super.onCreate();
//            setDummyData();
//            jsonTest();
//        }
//
//        @Override
//        public IBinder onBind(Intent intent) {
//            Log.i(this.toString(), "bound to " + intent.toString());
//            return mBinder;
//        }
//
//        @Override
//        public void onDestroy() {
//            Log.i(this.toString(), "destroyed");
//            super.onDestroy();
//        }
//
//        /**
//         * Class used for the client binder
//         */
//        public class DBBinder extends Binder {
//            DBService getService() {
//                return DBService.this;
//            }
//        }
//
//        /**
//         * Authenticates user credentials
//         * TODO: hashing
//         * @param username the username
//         * @param password the password
//         * @return true if credentials are OK
//         */
//        public boolean authLogin(String username, String password) {
//            boolean ok = false;
//            for(User u : users) {
//                if(u.getUsername().equals(username)) {
//                    if(u.getPassword().matches(password)) ok = true;
//                }
//            }
//            return ok;
//        }
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

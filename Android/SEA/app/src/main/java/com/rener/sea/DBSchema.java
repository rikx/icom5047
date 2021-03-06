package com.rener.sea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by user on 4/15/15.
 */
public class DBSchema {
    //
    public static final String ENABLE_FOREIGN_KEY = "PRAGMA foreign_keys = ON";
    public static final String DISABLE_FOREIGN_KEY = "PRAGMA foreign_keys = OFF";
    public static final String MODIFIED = "modified";
    public static final String STATUS = "status";
    public static final String MODIFIED_YES = "yes";
    public static final String MODIFIED_NO = "no";
    public static final String TABLE_ADDRESS = "address";
    public static final String ADDRESS_ID = "address_id";
    public static final String ADDRESS_LINE1 = "address_line1";
    public static final String ADDRESS_CITY = "city";
    public static final String ADDRESS_ZIPCODE = "zipcode";
    public static final String ADDRESS_LINE2 = "address_line2";
    public static final String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESS + "(" +
            ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ADDRESS_LINE1 + " TEXT," +
            ADDRESS_CITY + " TEXT," +
            ADDRESS_ZIPCODE + " TEXT," +
            ADDRESS_LINE2 + " TEXT," +
            STATUS + " INTEGER DEFAULT 1," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String APPOINTMENT_ID = "appointment_id";
    public static final String APPOINTMENT_DATE = "date";
    public static final String APPOINTMENT_TIME = "time";
    public static final String APPOINTMENT_REPORT_ID = "report_id";
    public static final String APPOINTMENT_PURPOSE = "purpose";
    public static final String APPOINTMENT_MAKER_ID = "maker_id";
    public static final String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_APPOINTMENTS + "(" +
            APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            APPOINTMENT_DATE + " TEXT," +
            APPOINTMENT_TIME + " TEXT," +
            APPOINTMENT_MAKER_ID + " INTEGER," +
            APPOINTMENT_REPORT_ID + " INTEGER," +
            APPOINTMENT_PURPOSE + " TEXT," +
            STATUS + " INTEGER DEFAULT 1," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_CATEGORY = "category";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "name";
    public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "(" +
            CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CATEGORY_NAME + " TEXT," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_DEVICES = "devices";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_NAME = "name";
    public static final String DEVICE_ID_NUMBER = "id_number";
    public static final String DEVICE_USER_ID = "user_id";
    public static final String DEVICE_LATEST_SYNC = "latest_sync";
    public static final String CREATE_DEVICES_TABLE = "CREATE TABLE " + TABLE_DEVICES + "(" +
            DEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_NAME + " TEXT," +
            DEVICE_ID_NUMBER + " TEXT," +
            DEVICE_USER_ID + " INTEGER," +
            DEVICE_LATEST_SYNC + " TEXT," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_FLOWCHART = "flowchart";
    public static final String FLOWCHART_ID = "flowchart_id";
    public static final String FLOWCHART_FIRST_ID = "first_id";
    public static final String FLOWCHART_NAME = "name";
    public static final String FLOWCHART_END_ID = "end_id";
    public static final String FLOWCHART_CREATOR_ID = "creator_id";
    public static final String FLOWCHART_VERSION = "version";
    public static final String CREATE_FLOWCHART_TABLE = "CREATE TABLE " + TABLE_FLOWCHART + "(" +
            FLOWCHART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FLOWCHART_FIRST_ID + " INTEGER," +
            FLOWCHART_NAME + " TEXT," +
            FLOWCHART_END_ID + " INTEGER," +
            FLOWCHART_CREATOR_ID + " INTEGER," +
            FLOWCHART_VERSION + " TEXT," +
            STATUS + " INTEGER DEFAULT 0," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_ITEM = "item";
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_FLOWCHART_ID = "flowchart_id";
    public static final String ITEM_LABEL = "label";
    public static final String ITEM_POS_TOP = "pos_top";
    public static final String ITEM_POS_LEFT = "pos_left";
    public static final String ITEM_TYPE = "type";
    public static final String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "(" +
            ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ITEM_FLOWCHART_ID + " INTEGER," +
            ITEM_LABEL + " TEXT," +
            ITEM_POS_TOP + " REAL," +
            ITEM_POS_LEFT + " REAL," +
            ITEM_TYPE + " TEXT," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_LOCATION = "location";
    public static final String LOCATION_ID = "location_id";
    public static final String LOCATION_NAME = "name";
    public static final String LOCATION_ADDRESS_ID = "address_id";
    public static final String LOCATION_OWNER_ID = "owner_id";
    public static final String LOCATION_MANAGER_ID = "manager_id";
    public static final String LOCATION_LICENSE = "license";
    public static final String LOCATION_AGENT_ID = "agent_id";
    public static final String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATION + "(" +
            LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            LOCATION_NAME + " TEXT," +
            LOCATION_ADDRESS_ID + " INTEGER," +
            LOCATION_OWNER_ID + " INTEGER," +
            LOCATION_MANAGER_ID + " INTEGER," +
            LOCATION_LICENSE + " TEXT UNIQUE," +
            LOCATION_AGENT_ID + " INTEGER," +
            STATUS + " INTEGER DEFAULT 0," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_LOCATION_CATEGORY = "location_category";
    public static final String LOCATION_CATEGORY_LOCATION_ID = "location_id";
    public static final String LOCATION_CATEGORY_CATEGORY_ID = "category_id";
    public static final String CREATE_LOCATION_CATEGORY_TABLE = "CREATE TABLE " + TABLE_LOCATION_CATEGORY + "(" +
            LOCATION_CATEGORY_LOCATION_ID + " INTEGER," +
            LOCATION_CATEGORY_CATEGORY_ID + " INTEGER," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "', " +
            "PRIMARY KEY (location_id, category_id) " + ")";
    public static final String TABLE_OPTION = "option";
    public static final String OPTION_ID = "option_id";
    public static final String OPTION_PARENT_ID = "parent_id";
    public static final String OPTION_NEXT_ID = "next_id";
    public static final String OPTION_LABEL = "label";
    public static final String CREATE_OPTION_TABLE = "CREATE TABLE " + TABLE_OPTION + "(" +
            OPTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            OPTION_PARENT_ID + " INTEGER," +
            OPTION_NEXT_ID + " INTEGER," +
            OPTION_LABEL + " TEXT," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_PERSON = "person";
    public static final String PERSON_ID = "person_id";
    public static final String PERSON_LAST_NAME1 = "last_name1";
    public static final String PERSON_FIRST_NAME = "first_name";
    public static final String PERSON_EMAIL = "email";
    //    public static final String PERSON_SPEC_ID = "spec_id";
    public static final String PERSON_LAST_NAME2 = "last_name2";
    public static final String PERSON_MIDDLE_INITIAL = "middle_initial";
    public static final String PERSON_PHONE_NUMBER = "phone_number";
    public static final String CREATE_PERSON_TABLE = "CREATE TABLE " + TABLE_PERSON + "(" +
            PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PERSON_LAST_NAME1 + " TEXT," +
            PERSON_FIRST_NAME + " TEXT," +
            PERSON_EMAIL + " TEXT," +
//            PERSON_SPEC_ID + " INTEGER," +
            PERSON_LAST_NAME2 + " TEXT," +
            PERSON_MIDDLE_INITIAL + " TEXT," +
            PERSON_PHONE_NUMBER + " TEXT," +
            STATUS + " INTEGER DEFAULT 1," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_REPORT = "report";
    public static final String REPORT_ID = "report_id";
    public static final String REPORT_CREATOR_ID = "creator_id";
    public static final String REPORT_LOCATION_ID = "location_id";
    //    public static final String REPORT_SUBJECT_ID = "subject_id";
    public static final String REPORT_FLOWCHART_ID = "flowchart_id";
    public static final String REPORT_NOTE = "note";
    public static final String REPORT_DATE_FILED = "date_filed";
    public static final String REPORT_NAME = "name";
    public static final String REPORT_STATUS = "status"; //0:started,1:done,2:deleted,3:tramper-start,4:tramper-done
    public static final String CREATE_REPORT_TABLE = "CREATE TABLE " + TABLE_REPORT + "(" +
            REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            REPORT_CREATOR_ID + " INTEGER," +
            REPORT_LOCATION_ID + " INTEGER," +
//            REPORT_SUBJECT_ID + " INTEGER," +
            REPORT_FLOWCHART_ID + " INTEGER," +
            REPORT_NOTE + " TEXT," +
            REPORT_NAME + " TEXT," +
            REPORT_DATE_FILED + " TEXT," +
            REPORT_STATUS + " INTEGER DEFAULT -1," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_PATH = "path";
    public static final String PATH_REPORT_ID = "report_id";
    public static final String PATH_OPTION_ID = "option_id";
    public static final String PATH_DATA = "data";
    public static final String PATH_SEQUENCE = "sequence";
    public static final String CREATE_PATH_TABLE = "CREATE TABLE " + TABLE_PATH + "(" +
            PATH_REPORT_ID + " INTEGER," +
            PATH_OPTION_ID + " INTEGER," +
            PATH_SEQUENCE + " INTEGER," +
            PATH_DATA + " TEXT," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "', " +
            "UNIQUE (" + PATH_REPORT_ID + ", " + PATH_OPTION_ID + ", " + PATH_SEQUENCE + "), " +
            "FOREIGN KEY(" + PATH_REPORT_ID + ") REFERENCES " + TABLE_REPORT + "(" + REPORT_ID + "))";
    public static final String TABLE_SPECIALIZATION = "specialization";
    public static final String SPECIALIZATION_ID = "spec_id";
    public static final String SPECIALIZATION_NAME = "name";
    public static final String CREATE_SPECIALIZATION_TABLE = "CREATE TABLE " + TABLE_SPECIALIZATION + "(" +
            SPECIALIZATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SPECIALIZATION_NAME + " TEXT UNIQUE," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_USERS = "users";
    public static final String USER_ID = "user_id";
    public static final String USER_USERNAME = "username";
    public static final String USER_PASSHASH = "passhash";
    public static final String USER_PERSON_ID = "person_id";
    public static final String USER_SALT = "salt";
    public static final String USER_TYPE = "type";
    public static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USER_USERNAME + " TEXT," +
            USER_PASSHASH + " TEXT," +
            USER_PERSON_ID + " INTEGER," +
            USER_SALT + " TEXT," +
            USER_TYPE + " TEXT," +
            STATUS + " INTEGER DEFAULT 1," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "')";
    public static final String TABLE_USERS_SPECIALIZATION = "users_specialization";
    public static final String USERS_SPECIALIZATION_USER_ID = "user_id";
    public static final String USERS_SPECIALIZATION_SPECIALIZATION_ID = "spec_id";
    public static final String CREATE_USERS_SPECIALIZATION_TABLE = "CREATE TABLE " + TABLE_USERS_SPECIALIZATION + "(" +
            USERS_SPECIALIZATION_USER_ID + " INTEGER," +
            USERS_SPECIALIZATION_SPECIALIZATION_ID + " INTEGER," +
            MODIFIED + " TEXT NOT NULL DEFAULT '" + MODIFIED_YES + "'," +
            "PRIMARY KEY (" + USERS_SPECIALIZATION_USER_ID + ", " + USERS_SPECIALIZATION_SPECIALIZATION_ID + "))";


    public static final DateFormat FORMATDATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat FORMATTIME = new SimpleDateFormat("HH:mm:ss");
    public static final DateFormat FORMATALL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String POST_DEVICE_ID = "id_number";
    public static final String POST_SYNC_TYPE = "sync_type"; // FULL, INC
    public static final String SYNC_FULL = "FULL";
    public static final String SYNC_INC = "INC";
    public static final String SYNC_STATUS = "sync_status";  // 1 = success, 0 = fail, -1 = error
    public static final int STATUS_ERROR = -1;
    public static final int STATUS_FAIL = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final String POST_USER_ID = "user_id";
    public static final String POST_USER_TYPE = "type";
    public static final String USER_ADMIN = "admin";
    public static final String USER_SPECIALIST = "specialist";
    public static final String USER_AGENT = "agent";
    public static final String POST_LOCAL_DATA = "data";
    // from server
    public static final String POST_SERVER_DATA_NEW = "new_data";// // return in JSON response object  {sync_status: 1, new_data: {db_data}}
    public static final String POST_SERVER_DATA_DELETED = "deleted_data";
    public static final String POST_SYNC_INF = "sync_info";
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_SUCCESS_NEW_USER = 2;
    public static final int LOGIN_FAIL = -200;
    public static final int SYNC_SUCCESS = 200;

    public static final String AUTH_USER = "username";
    public static final String AUTH_PASS = "password";
    public static final String AUTH_DEVICE = "android_id";
    public static final String AUTH_URL = "http://136.145.116.231:8080/mobile/test10.php";


    //    public static final String SYNC_URL = "http://136.145.116.231/mobile/test7.php"; // working
    public static final String SYNC_URL = "http://136.145.116.231:8080/mobile/test8.php"; // working

}

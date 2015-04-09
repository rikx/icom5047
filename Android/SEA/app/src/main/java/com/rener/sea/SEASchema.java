package com.rener.sea;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 4/8/15.
 */
public final class SEASchema extends SQLiteOpenHelper {


    private static int DATABASE_VERSION = 1;
    // declaration of all keys for the DB
    public static final String
            DATABASE_NAME           = "seadb",
            TABLE_ADDRESS 			= "address",
            ADDRESS_ID				= "address_id",
            ADDRESS_LINE1			= "address_line1",
            ADDRESS_CITY			= "city",
            ADDRESS_ZIPCODE			= "zipcode",
            ADDRESS_LINE2			= "address_line2",

            TABLE_APPOINTMENTS 		= "appointments",
            APPOINTMENT_ID			= "appointment_id",
            APPOINTMENT_DATE		= "date",
            APPOINTMENT_TIME		= "time",
            APPOINTMENT_LOCATION_ID	= "location_id",
            APPOINTMENT_REPORT_ID	= "report_id",
            APPOINTMENT_PURPOSE		= "purpose",

            TABLE_CATEGORY 			= "category",
            CATEGORY_ID			    = "category_id",
            CATEGORY_NAME		    = "name",

            TABLE_DEVICES 			= "devices",
            DEVICE_ID				= "device_id",
            DEVICE_NAME				= "name",
            DEVICE_ID_NUMBER		= "id_number",
            DEVICE_USER_ID			= "user_id",
            DEVICE_LATEST_SYNC		= "latest_sync",

            TABLE_FLOWCHART 		= "flowchart",
            FLOWCHART_ID            =  "flowchart_id",
            FLOWCHART_FIRST_ID      =  "first_id",
            FLOWCHART_NAME          =  "name",
            FLOWCHART_END_ID        =  "end_id",
            FLOWCHART_CREATOR_ID    =  "creator_id",
            FLOWCHART_VERSION       =  "version",

            TABLE_ITEM 				= "item",
            ITEM_ID       		    = "item_id",
            ITEM_FLOWCHART_ID       = "flowchart_id",
            ITEM_LABEL         		= "label",
            ITEM_POS_TOP       		= "pos_top",
            ITEM_POS_LEFT      		= "pos_left",
            ITEM_TYPE          		= "type",

            TABLE_LOCATION 			= "location",
            LOCATION_ID             = "location_id",
            LOCATION_NAME           = "name",
            LOCATION_ADDRESS_ID     = "address_id",
            LOCATION_OWNER_ID       = "owner_id",
            LOCATION_MANAGER_ID     = "manager_id",
            LOCATION_LICENSE        = "license",
            LOCATION_AGENT_ID       = "agent_id",

            TABLE_LOCATION_CATEGORY             = "location_category",
            LOCATION_CATEGORY_LOCATION_ID		= "location_id",
            LOCATION_CATEGORY_CATEGORY_ID		= "category_id",

            TABLE_OPTION 			= "option",
            OPTION_ID		    	= "option_id",
            OPTION_PARENT_ID 	    = "parent_id",
            OPTION_NEXT_ID	    	= "next_id",
            OPTION_LABEL	    	= "label",

            TABLE_PATH 				= "path",
            PATH_REPORT_ID			= "report_id",
            PATH_OPTION_ID			= "option_id",
            PATH_DATA				= "data",

            TABLE_PERSON 			= "person",
            PERSON_ID               = "person_id",
            PERSON_LAST_NAME1       = "last_name1",
            PERSON_FIRST_NAME       = "first_name",
            PERSON_EMAIL            = "email",
            PERSON_SPEC_ID          = "spec_id",
            PERSON_LAST_NAME2       = "last_name2",
            PERSON_MIDDLE_INITIAL   = "middle_initial",
            PERSON_PHONE_NUMBER     = "phone_number",

            TABLE_REPORT 			= "report",
            REPORT_ID				= "report_id",
            REPORT_CREATOR_ID		= "creator_id",
            REPORT_LOCATION_ID		= "location_id",
            REPORT_SUBJECT_ID		= "subject_id",
            REPORT_FLOWCHART_ID		= "flowchart_id",
            REPORT_NOTE				= "note",
            REPORT_DATE_FILED		= "date_filed",

            TABLE_SPECIALIZATION 	= "specialization",
            SPECIALIZATION_ID		= "spec_id",
            SPECIALIZATION_NAME		= "name",

            TABLE_USERS 			= "users",
            USER_ID					= "user_id",
            USER__USERNAME			= "username",
            USER__PASSHASH			= "passhash",
            USER__PERSON_ID			= "person_id",
            USER__SALT				= "salt";


    public SEASchema(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ADDRESS 	+ "(" +
                ADDRESS_ID							+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ADDRESS_LINE1						+ " TEXT," +
                ADDRESS_CITY						+ " TEXT," +
                ADDRESS_ZIPCODE						+ " INTEGER," +
                ADDRESS_LINE2						+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_APPOINTMENTS	+ "(" +
                APPOINTMENT_ID						+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                APPOINTMENT_DATE					+ " TEXT," +
                APPOINTMENT_TIME					+ " TEXT," +
                APPOINTMENT_LOCATION_ID				+ " TEXT," +
                APPOINTMENT_REPORT_ID				+ " TEXT," +
                APPOINTMENT_PURPOSE					+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY 	+ "(" +
                CATEGORY_ID			    			+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CATEGORY_NAME		    			+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_DEVICES 		+ "(" +
                DEVICE_ID							+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DEVICE_NAME							+ " TEXT," +
                DEVICE_ID_NUMBER					+ " TEXT," +
                DEVICE_USER_ID						+ " INTEGER," +
                DEVICE_LATEST_SYNC					+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_FLOWCHART 	+ "(" +
                FLOWCHART_ID            			+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FLOWCHART_FIRST_ID      			+ " INTEGER," +
                FLOWCHART_NAME          			+ " TEXT," +
                FLOWCHART_END_ID        			+ " INTEGER," +
                FLOWCHART_CREATOR_ID    			+ " INTEGER," +
                FLOWCHART_VERSION       			+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ITEM 		+ "(" +
                ITEM_ID       					+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ITEM_FLOWCHART_ID            		+ " INTEGER," +
                ITEM_LABEL         					+ " TEXT," +
                ITEM_POS_TOP       					+ " REAL," +
                ITEM_POS_LEFT      					+ " REAL," +
                ITEM_TYPE          					+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_LOCATION 	+ "(" +
                LOCATION_ID             			+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOCATION_NAME           			+ " TEXT," +
                LOCATION_ADDRESS_ID     			+ " INTEGER," +
                LOCATION_OWNER_ID       			+ " INTEGER," +
                LOCATION_MANAGER_ID     			+ " INTEGER," +
                LOCATION_LICENSE        			+ " TEXT," +
                LOCATION_AGENT_ID       + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_LOCATION_CATEGORY + "(" +
                LOCATION_CATEGORY_LOCATION_ID		+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LOCATION_CATEGORY_CATEGORY_ID		+ " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_OPTION 		+ "(" +
                OPTION_ID							+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OPTION_PARENT_ID					+ " INTEGER," +
                OPTION_NEXT_ID						+ " INTEGER," +
                OPTION_LABEL						+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_PATH 		+ "(" +
                PATH_REPORT_ID						+ " INTEGER," +
                PATH_OPTION_ID						+ " INTEGER," +
                PATH_DATA							+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_PERSON 		+ "(" +
                PERSON_ID               			+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PERSON_LAST_NAME1       			+ " TEXT," +
                PERSON_FIRST_NAME       			+ " TEXT," +
                PERSON_EMAIL            			+ " TEXT," +
                PERSON_SPEC_ID          			+ " INTEGER," +
                PERSON_LAST_NAME2       			+ " TEXT," +
                PERSON_MIDDLE_INITIAL   			+ " TEXT," +
                PERSON_PHONE_NUMBER     			+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_REPORT 		+ "(" +
                REPORT_ID							+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                REPORT_CREATOR_ID					+ " INTEGER," +
                REPORT_LOCATION_ID					+ " INTEGER," +
                REPORT_SUBJECT_ID					+ " INTEGER," +
                REPORT_FLOWCHART_ID					+ " INTEGER," +
                REPORT_NOTE							+ " TEXT," +
                REPORT_DATE_FILED					+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_SPECIALIZATION + "(" +
                SPECIALIZATION_ID					+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SPECIALIZATION_NAME					+ " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_USERS 		+ "(" +
                USER_ID								+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER__USERNAME						+ " TEXT," +
                USER__PASSHASH						+ " TEXT," +
                USER__PERSON_ID						+ " INTEGER," +
                USER__SALT							+ " TEXT)");


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


}

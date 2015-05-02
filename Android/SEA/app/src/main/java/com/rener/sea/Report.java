package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

/**
 * Represents a Report in the system
 * A report is a survey that has been answered
 * TODO: implement DBHelper into this class
 */
public class Report implements Comparable<Report> {

    public static final int NEW_REPORT_ID = -1;
    private long id;
    private Date date;
    private String type = "";
    private Path path;
	private Appointment appointment = null;
    private DBHelper dbHelper = null;

    public Report(long id, DBHelper db) {
        this.dbHelper = db;
        invoke(id);
    }

    public Report(long report_id, long creator_id, long location_id, long subject_id, long flowchart_id, String note, Date date, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(report_id)) { // can also verify if id == -1

        } else {
            this.id = create(creator_id, location_id, subject_id, flowchart_id, note, date);
        }
    }
    /**
     * Constructs a new Report with no ID and some default value
     * Used to represent a Report that has been created but hasn't been assigned a unique ID
     */
    public Report(DBHelper db, User creator) {
        this.dbHelper = db;
        this.id = create(creator.getId(),-1,-1,-1,"",new Date());
    }




    private long create(long creator_id, long location_id, long subject_id, long flowchart_id, String note, Date date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_CREATOR_ID, creator_id);
        values.put(DBSchema.REPORT_LOCATION_ID, location_id);
        values.put(DBSchema.REPORT_SUBJECT_ID, subject_id);
        values.put(DBSchema.REPORT_FLOWCHART_ID, flowchart_id);
        values.put(DBSchema.REPORT_NOTE, String.valueOf(note));
        values.put(DBSchema.REPORT_DATE_FILED, DBSchema.formatDate.format(date));
        long id = db.insert(DBSchema.TABLE_REPORT, null, values);
        db.close();
        return id;

    }

    private boolean exist(long report_id) {
        if (report_id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_ID},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(report_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    private boolean invoke(long user_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_ID},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(user_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                this.id = cursor.getLong(0);
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }




    public long getId() {
        return id;
    }

    public String getName() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = "";
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_NAME},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                name = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return name;
    }

    public long setName(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_NAME, name);
        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public User getCreator() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long creator = -1;
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_CREATOR_ID},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                creator = cursor.getLong(0);
            db.close();
            cursor.close();
        }
        return new User(creator,dbHelper);
    }

    public long setCreator(User creator) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_CREATOR_ID, creator.getId());
        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public Location getLocation() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long location = -1;
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_LOCATION_ID},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                location = cursor.getLong(0);
            db.close();
            cursor.close();
        }
        return new Location(location,dbHelper);
    }

    public long setLocation(Location location) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_LOCATION_ID, location.getId());
        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public Person getSubject() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long subject = -1;
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_SUBJECT_ID},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                subject = cursor.getLong(0);
            db.close();
            cursor.close();
        }
        return new Person(subject,dbHelper);
    }

    public long setSubject(Person subject) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_SUBJECT_ID, subject.getId());
        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public Flowchart getFlowchart() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long flowchart = -1;
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_FLOWCHART_ID},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                flowchart = cursor.getLong(0);
            db.close();
            cursor.close();
        }
        return new Flowchart(flowchart,dbHelper);
    }

    public long setFlowchart(Flowchart flowchart) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.i(this.toString(), "Set Flowchart ID :" + flowchart.getId());
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_FLOWCHART_ID, flowchart.getId());
        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public String getNotes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String notes = "";
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_NOTE},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                notes = cursor.getString(0);
            db.close();
            cursor.close();
        }
        return notes;
    }

    public long setNotes(String notes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_NOTE, notes);
        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
    }

    public Date getDate() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Date date = new Date(0);
        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_DATE_FILED},
                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                try {
                    date = DBSchema.formatDate.parse(cursor.getString(0));
                } catch (ParseException e) {
                    Log.e(this.toString(), "Time conversion error: " + e.toString());
                }

            }
            db.close();
            cursor.close();
        }

        return date;
    }

    public long setDate(Date date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.REPORT_DATE_FILED, String.valueOf(DBSchema.formatDate.format(date)));
        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }

    public String getDateString(String format, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(getDate());
    }
    //TODO: type dos not exist in the web db
    public String getType() {

//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String type = "";
//        Cursor cursor = db.query(DBSchema.TABLE_REPORT, new String[]{DBSchema.REPORT_TYPE},
//                DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//            cursor.moveToFirst();
//            if(!cursor.isNull(0))
//                type = cursor.getString(0);
//            db.close();
//            cursor.close();
//        }

        return type;
    }

    public long setType(String type) {

//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DBSchema.REPORT_TYPE, type);
//        long id = db.update(DBSchema.TABLE_REPORT, values, DBSchema.REPORT_ID + "=?", new String[]{String.valueOf(this.id)});
//        db.close();
//        return id;

        this.type = type;
        return -1;
    }
    // this method is maybe out of area
	public Appointment getAppointment() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long appointment = -1;
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_ID},
                DBSchema.APPOINTMENT_REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0))
                appointment = cursor.getLong(0);

            db.close();
            cursor.close();
        }
        return new Appointment(appointment,dbHelper);
	}
    // this method is maybe out of area
	public long setAppointment(Appointment appointment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.APPOINTMENT_REPORT_ID, String.valueOf(id));
        long id = db.update(DBSchema.TABLE_APPOINTMENTS, values, DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointment.getId())});
        db.close();
        return id;// if -1 error during update
	}

	public void addToPath(Option option, String data) {
        Item item = findOptionParent(option);
        path.addEntry(option, data);
    }

    public void addToPath(Option option) {
        Item item = findOptionParent(option);
        path.addEntry(option);
    }
//    public Stack<PathEntry> findPath(){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        Cursor cursor = db.query(DBSchema.TABLE_PATH, new String[]{DBSchema.PATH_OPTION_ID},
//                DBSchema.PATH_REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, DBSchema.PATH_OPTION_ID + " DESC", null);
//        Stack<PathEntry> path = new Stack<>();
//        if ((cursor != null) && (cursor.getCount() > 0)) {
//
//            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                if(!cursor.isNull(0))
//                    path.push(new PathEntry(this.id,cursor.getLong(0),dbHelper));
//
//            }
//
//            db.close();
//            cursor.close();
//
//        }
//        return path;
//    }
    public Path getPath() {
        Path path = new Path(id,dbHelper);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_PATH, new String[]{DBSchema.PATH_OPTION_ID},
                DBSchema.PATH_REPORT_ID + "=?", new String[]{String.valueOf(id)}, null, null, DBSchema.PATH_SEQUENCE + " ASC", null);
        if ((cursor != null) && (cursor.getCount() > 0)) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if(!cursor.isNull(0))
                    path.addEntry(new Option(cursor.getLong(0),dbHelper));
            }
        }
        return path;


    }
    // do not modify the db
    public void setPath(Path path) {
        this.path = path;
    }

    private Item findOptionParent(Option option) {
        Item item = null;
        for (Item i : getFlowchart().getItems()) {
            for (Option o : i.getOptions()) {
                if (o.getId() == option.getId()) item = i;
            }
        }
        return item;
    }

    public String toString() {
        return super.toString();
    }

    @Override
    public int compareTo(@NonNull Report r) {
        return getDate().compareTo(r.getDate());
    }
}

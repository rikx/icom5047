package com.rener.sea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Appointment {

    private long id = -1;
    private DBHelper dbHelper;
	private User creator; //TODO: integrate this to DB

    public Appointment(long id, DBHelper dbHelper) {
       this.dbHelper = dbHelper;
       invoke(id);

    }
    public Appointment( long id, String date, String time, long location_id, long report_id, String purpose, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        //verify if exit
        if (exist(id)) { // can also verify if id == -1

        } else {
            this.id = create(date, time, location_id, report_id, purpose);
        }

    }
    private long create(String date, String time, long creator_id, long report_id, String purpose){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.APPOINTMENT_DATE, date);
        values.put(DBSchema.APPOINTMENT_TIME, time);
        values.put(DBSchema.APPOINTMENT_MAKER_ID, creator_id);
        values.put(DBSchema.APPOINTMENT_REPORT_ID, report_id);
        values.put(DBSchema.APPOINTMENT_PURPOSE, purpose);
        long id = db.insert(DBSchema.TABLE_APPOINTMENTS, null, values);
        db.close();
        return id;
    }
    private boolean exist(long appointment_id) {
        if (appointment_id == -1) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_ID},
                DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointment_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }
    private boolean invoke(long appointment_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_ID},
                DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(appointment_id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)) {
                this.id = cursor.getLong(0);
            }
            db.close();
            cursor.close();
            return true;
        }
        return false;

    }

    public long getId() {
        return id;
    }

	public User getCreator() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long creator = -1;
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_MAKER_ID},
                DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
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
        values.put(DBSchema.APPOINTMENT_MAKER_ID, creator.getId());
        long id = db.update(DBSchema.TABLE_APPOINTMENTS, values, DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;
	}

	public Calendar getDate() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Date date = new Date(0);
        Calendar cal  = Calendar.getInstance();
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_DATE,DBSchema.APPOINTMENT_TIME},
                DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)&&!cursor.isNull(1)){
                try {


                    cal.setTime(DBSchema.FORMATALL.parse(cursor.getString(0)+" "+cursor.getString(0)));
//                    date = DBSchema.FORMATALL.parse(cursor.getString(0)+" "+cursor.getString(0));
                } catch (ParseException e) {
                    Log.e(this.toString(), "Time conversion error: " + e.toString());
                }

            }
            db.close();
            cursor.close();
        }

        return cal;
	}

	public long setDate(Calendar date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.APPOINTMENT_DATE, String.valueOf(DBSchema.FORMATDATE.format(date.getTime())));
        values.put(DBSchema.APPOINTMENT_TIME, String.valueOf(DBSchema.FORMATTIME.format(date.getTime())));
        long id = db.update(DBSchema.TABLE_APPOINTMENTS, values, DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
	}

	public String getPurpose() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String purpose = "";
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_PURPOSE},
                DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                purpose = cursor.getString(0);
            }
            db.close();
            cursor.close();
        }

        return purpose;
	}



    public long setPurpose(String purpose) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.APPOINTMENT_PURPOSE, String.valueOf(purpose));
        long id = db.update(DBSchema.TABLE_APPOINTMENTS, values, DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
	}

	public String getDateString(String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		return sdf.format(getDate());
	}

    public Report getReport() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long report = -1;
        Cursor cursor = db.query(DBSchema.TABLE_APPOINTMENTS, new String[]{DBSchema.APPOINTMENT_REPORT_ID},
                DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            if(!cursor.isNull(0)){
                report = cursor.getLong(0);
            }
            db.close();
            cursor.close();
        }

        return new Report(report,dbHelper);
    }

    public long setReport(Report report){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBSchema.APPOINTMENT_REPORT_ID, report.getId());
        long id = db.update(DBSchema.TABLE_APPOINTMENTS, values, DBSchema.APPOINTMENT_ID + "=?", new String[]{String.valueOf(this.id)});
        db.close();
        return id;// if -1 error during update
    }


}

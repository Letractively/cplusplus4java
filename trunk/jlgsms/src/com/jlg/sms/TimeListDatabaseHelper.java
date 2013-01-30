package com.jlg.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeListDatabaseHelper {

	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "timetracker.db";
	private static final String TABLE_NAME = "timerecords";

	private static final String TIMETRACKER_COLUMN_ID = "_id";
	private static final String TIMETRACKER_COLUMN_TIME = "time";
	private static final String TIMETRACKER_COLUMN_NOTES = "notes";

	private TimeTrackerOpenHelper openHelper;
	private SQLiteDatabase database;

	public TimeListDatabaseHelper(Context context) {
		openHelper = new TimeTrackerOpenHelper(context);
		database = openHelper.getWritableDatabase();
	}
	
	public void saveRecord(String time, String notes) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(TIMETRACKER_COLUMN_TIME, time);
		contentValues.put(TIMETRACKER_COLUMN_NOTES, notes);
		database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllTimeRecords() {
		return database.rawQuery(
				"SELECT * FROM " + TABLE_NAME +"  ORDER BY "+TIMETRACKER_COLUMN_ID+" DESC ", 
				null);
	}

	private class TimeTrackerOpenHelper extends SQLiteOpenHelper {

		public TimeTrackerOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL("CREATE TABLE " + TABLE_NAME + "("
					+ TIMETRACKER_COLUMN_ID + " INTEGER PRIMARY KEY, "
					+ TIMETRACKER_COLUMN_TIME + " TEXT, "
					+ TIMETRACKER_COLUMN_NOTES + " TEXT)");

		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "");
			onCreate(database);
		}

	}

}


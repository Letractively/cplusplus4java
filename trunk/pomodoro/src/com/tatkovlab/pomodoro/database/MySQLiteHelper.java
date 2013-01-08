package com.tatkovlab.pomodoro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper
{
  public static final String COLUMN_ABANDONED = "abandoned";
  public static final String COLUMN_DATE_ADDED = "date_added";
  public static final String COLUMN_DONE = "done";
  public static final String COLUMN_ESTIMATED = "estimated";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_LIST = "list";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_STATUS = "status";
  private static final String DATABASE_CREATE = "create table tasks(_id integer primary key autoincrement, name text not null, status integer not null, list integer not null, estimated integer not null, done integer not null, abandoned integer not null, date_added integer not null);";
  private static final String DATABASE_NAME = "pomodoro_timer.db";
  private static final int DATABASE_VERSION = 1;
  public static final String TABLE_TASKS = "tasks";

  public MySQLiteHelper(Context paramContext)
  {
    super(paramContext, "pomodoro_timer.db", null, 1);
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("create table tasks(_id integer primary key autoincrement, name text not null, status integer not null, list integer not null, estimated integer not null, done integer not null, abandoned integer not null, date_added integer not null);");
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + paramInt1 + " to " + paramInt2 + ", which will destroy all old data");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS tasks");
    onCreate(paramSQLiteDatabase);
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.database.MySQLiteHelper
 * JD-Core Version:    0.6.2
 */
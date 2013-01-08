package com.tatkovlab.pomodoro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class TasksDataSource
{
  private final String[] allColumns = { "_id", "name", "status", "list", "estimated", "done", "abandoned", "date_added" };
  private SQLiteDatabase database;
  private final MySQLiteHelper dbHelper;

  public TasksDataSource(Context paramContext)
  {
    this.dbHelper = new MySQLiteHelper(paramContext);
  }

  private Task cursorToTask(Cursor paramCursor)
  {
    Task localTask = new Task();
    localTask.setId(paramCursor.getLong(0));
    localTask.setName(paramCursor.getString(1));
    localTask.setStatus(Task.TASK_STATUS.numValueToEnum(paramCursor.getLong(2)));
    localTask.setList(Task.TASK_LIST.numValueToEnum(paramCursor.getLong(3)));
    localTask.setEstimated(paramCursor.getLong(4));
    localTask.setDone(paramCursor.getLong(5));
    localTask.setAbandoned(paramCursor.getLong(6));
    localTask.setDateAdded(paramCursor.getLong(7));
    return localTask;
  }

  private List<Task> loadTasksFromCursor(Cursor paramCursor)
  {
//    ArrayList localArrayList = new ArrayList();
//    paramCursor.moveToFirst();
//    while (true)
//    {
//      if (paramCursor.isAfterLast())
//      {
//        paramCursor.close();
//        return localArrayList;
//      }
//      localArrayList.add(cursorToTask(paramCursor));
//      paramCursor.moveToNext();
//    }
	  
	  
	  //use myself code
	  ArrayList re = new ArrayList();
	  while( paramCursor.moveToNext() ){
		  Task one=cursorToTask(paramCursor);
		  if(one!=null){
			  re.add(one);
		  }
	  }
	  
	  
	  return re;
  }

  public void close()
  {
    this.dbHelper.close();
  }

  public Task createTask(String paramString, Task.TASK_STATUS paramTASK_STATUS, Task.TASK_LIST paramTASK_LIST, long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("name", paramString);
    localContentValues.put("status", Long.valueOf(paramTASK_STATUS.getNumericValue()));
    localContentValues.put("list", Long.valueOf(paramTASK_LIST.getNumericValue()));
    localContentValues.put("estimated", Long.valueOf(paramLong1));
    localContentValues.put("done", Long.valueOf(paramLong2));
    localContentValues.put("abandoned", Long.valueOf(paramLong3));
    localContentValues.put("date_added", Long.valueOf(paramLong4));
    long l = this.database.insert("tasks", null, localContentValues);
    Cursor localCursor = this.database.query("tasks", this.allColumns, "_id = " + l, null, null, null, null);
    localCursor.moveToFirst();
    Task localTask = cursorToTask(localCursor);
    localCursor.close();
    return localTask;
  }

  public void deleteTask(Task paramTask)
  {
    long l = paramTask.getId();
    System.out.println("Task deleted with id: " + l);
    this.database.delete("tasks", "_id = " + l, null);
  }

  public Task getActiveTask()
  {
	Task re=null;
    Cursor localCursor = this.database.query("tasks", this.allColumns, "status = " + Task.TASK_STATUS.IN_PROGRESS.getNumericValue(), null, null, null, null);
//    boolean bool = localCursor.moveToFirst();
//    if (!bool);
//    while (true)
//    {
//      Task localTask = cursorToTask(localCursor);
//      localCursor.close();
//      return localTask;
//    }
    
    while( localCursor.moveToNext() ){
    	re=cursorToTask(localCursor);
    }
    
    return re;
  }

  public List<Task> getAllTasks()
  {
    return loadTasksFromCursor(this.database.query("tasks", this.allColumns, null, null, null, null, "date_added DESC"));
  }

  public List<Task> getIntervalTasks(long paramLong1, long paramLong2)
  {
    SQLiteDatabase localSQLiteDatabase = this.database;
    String[] arrayOfString1 = this.allColumns;
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = String.valueOf(paramLong1);
    return loadTasksFromCursor(localSQLiteDatabase.query("tasks", arrayOfString1, "_id < ?", arrayOfString2, null, null, "date_added DESC", String.valueOf(paramLong2)));
  }

  public List<Task> getTaskWithLowerOrEqualId(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.database;
    String[] arrayOfString1 = this.allColumns;
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = String.valueOf(paramLong - 1L);
    return loadTasksFromCursor(localSQLiteDatabase.query("tasks", arrayOfString1, "_id > ?", arrayOfString2, null, null, "date_added DESC"));
  }

  public List<Task> getTasks(long paramLong)
  {
    return loadTasksFromCursor(this.database.query("tasks", this.allColumns, null, null, null, null, "date_added DESC", String.valueOf(paramLong)));
  }

  public void open()
    throws SQLException
  {
    this.database = this.dbHelper.getWritableDatabase();
  }

  public void setTaskAsActiveTask(Task paramTask)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Long.valueOf(Task.TASK_STATUS.NEW.getNumericValue()));
    this.database.update("tasks", localContentValues, "status = " + Task.TASK_STATUS.IN_PROGRESS.getNumericValue(), null);
    localContentValues.put("status", Long.valueOf(Task.TASK_STATUS.IN_PROGRESS.getNumericValue()));
    this.database.update("tasks", localContentValues, "_id = " + paramTask.getId(), null);
  }

  public void updateTask(Task paramTask)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("name", paramTask.getName());
    localContentValues.put("estimated", Long.valueOf(paramTask.getEstimated()));
    localContentValues.put("done", Long.valueOf(paramTask.getDone()));
    localContentValues.put("abandoned", Long.valueOf(paramTask.getAbandoned()));
    localContentValues.put("status", Long.valueOf(paramTask.getStatus().numericValue));
    SQLiteDatabase localSQLiteDatabase = this.database;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramTask.getId());
    localSQLiteDatabase.update("tasks", localContentValues, "_id = ?", arrayOfString);
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.database.TasksDataSource
 * JD-Core Version:    0.6.2
 */
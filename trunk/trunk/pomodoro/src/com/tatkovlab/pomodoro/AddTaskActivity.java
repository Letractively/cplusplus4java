package com.tatkovlab.pomodoro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tatkovlab.pomodoro.database.Task;
import com.tatkovlab.pomodoro.database.TasksDataSource;

public class AddTaskActivity extends Activity
{
  private static final int DEFAULT_POMODOROS = 4;
  int expectedPodomoros = 4;
  TextView expectedPomodorosTextView = null;
  String taskName;
  EditText taskNameEditText;
  TasksDataSource tasksDataSource;

  private void saveTask()
  {
    String str = this.taskNameEditText.getText().toString();
    this.tasksDataSource.createTask(str, Task.TASK_STATUS.NEW, Task.TASK_LIST.TODAY, this.expectedPodomoros, 0L, 0L, System.currentTimeMillis());
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    getWindow().setSoftInputMode(4);
    this.tasksDataSource = new TasksDataSource(this);
    this.tasksDataSource.open();
    ((SeekBar)findViewById(2131296258)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        AddTaskActivity.this.expectedPodomoros = (paramAnonymousInt + 1);
        AddTaskActivity.this.expectedPomodorosTextView.setText(String.valueOf(AddTaskActivity.this.expectedPodomoros));
      }

      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
      }

      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
      }
    });
    ((Button)findViewById(2131296259)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AddTaskActivity.this.saveTask();
        AddTaskActivity.this.onBackPressed();
      }
    });
    this.taskNameEditText = ((EditText)findViewById(2131296256));
    this.expectedPomodorosTextView = ((TextView)findViewById(2131296257));
    this.expectedPomodorosTextView.setText(String.valueOf(this.expectedPodomoros));
  }

  protected void onPause()
  {
    this.tasksDataSource.close();
    super.onPause();
  }

  public void onResume()
  {
    this.tasksDataSource.open();
    super.onResume();
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.AddTaskActivity
 * JD-Core Version:    0.6.2
 */
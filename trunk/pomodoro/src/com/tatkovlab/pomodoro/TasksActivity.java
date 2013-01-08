package com.tatkovlab.pomodoro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.tatkovlab.pomodoro.database.Task;
import com.tatkovlab.pomodoro.database.TasksDataSource;
import com.tatkovlab.pomodoro.util.TypeFaceManager;
import java.util.List;

public class TasksActivity extends Activity
{
  protected static final int CONTEXTMENU_DELETEITEM = 0;
  protected static final int DEFAULT_TASK_NUMBER = 10;
  protected static final String LAST_LIST_ID_KEY = "last_list_id_key";
  TasksAdapter adapter;
  Button addTask;
  View footerView;
  LayoutInflater inflater;
  boolean isAllLoaded = false;
  long lastListId = -1L;
  Button loadMore;
  List<Task> tasks;
  TasksDataSource tasksDataSource;
  ListView tasksList;

  private void updateLoadMoreBtn()
  {
    if (!this.isAllLoaded)
    {
      this.isAllLoaded = true;
      this.tasksList.removeFooterView(this.footerView);
    }
  }

  public boolean onContextItemSelected(MenuItem paramMenuItem)
  {
    AdapterView.AdapterContextMenuInfo localAdapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)paramMenuItem.getMenuInfo();
    switch (paramMenuItem.getItemId())
    {
    default:
    case 0:
    }
    for (boolean bool = false; ; bool = true)
    {
      return bool;
//      Task localTask = (Task)this.tasksList.getAdapter().getItem(localAdapterContextMenuInfo.position);
//      this.tasksDataSource.deleteTask(localTask);
//      TasksAdapter localTasksAdapter = (TasksAdapter)((HeaderViewListAdapter)this.tasksList.getAdapter()).getWrappedAdapter();
//      localTasksAdapter.remove(localTask);
//      localTasksAdapter.notifyDataSetChanged();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903050);
    if ((paramBundle != null) && (paramBundle.getLong("last_list_id_key") != 0L))
      this.lastListId = paramBundle.getLong("last_list_id_key");
    this.inflater = LayoutInflater.from(this);
    this.tasksDataSource = new TasksDataSource(this);
    this.tasksList = ((ListView)findViewById(2131296282));
    Typeface localTypeface = TypeFaceManager.getHandDrawnTypeFace(this);
    View localView = this.inflater.inflate(2130903045, this.tasksList, false);
    this.tasksList.addHeaderView(localView);
    this.footerView = this.inflater.inflate(2130903044, this.tasksList, false);
    this.tasksList.addFooterView(this.footerView);
    this.tasksList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener()
    {
      public void onCreateContextMenu(ContextMenu paramAnonymousContextMenu, View paramAnonymousView, ContextMenu.ContextMenuInfo paramAnonymousContextMenuInfo)
      {
        paramAnonymousContextMenu.setHeaderTitle(TasksActivity.this.getString(2131099676));
        paramAnonymousContextMenu.add(0, 0, 0, 2131099677);
      }
    });
    this.tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        TasksActivity.this.tasksDataSource.setTaskAsActiveTask((Task)TasksActivity.this.tasks.get(paramAnonymousInt - 1));
        TasksActivity.this.onBackPressed();
      }
    });
    this.addTask = ((Button)findViewById(2131296266));
    this.addTask.setTypeface(localTypeface);
    this.addTask.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TasksActivity.this.startActivity(new Intent(TasksActivity.this, AddTaskActivity.class));
      }
    });
    this.loadMore = ((Button)findViewById(2131296265));
    this.loadMore.setTypeface(localTypeface);
    this.loadMore.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        List localList = TasksActivity.this.tasksDataSource.getIntervalTasks(TasksActivity.this.lastListId, 10L);
        if (localList.size() < 10)
          TasksActivity.this.updateLoadMoreBtn();
        if (localList.size() == 0);
        while (true)
        {
          return;
//          TasksActivity.this.adapter.addTasks(localList);
//          TasksActivity.this.lastListId = ((Task)localList.get(-1 + localList.size())).getId();
        }
      }
    });
  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  protected void onPause()
  {
    this.tasksDataSource.close();
    super.onPause();
  }

  public void onResume()
  {
    super.onResume();
    this.tasksDataSource.open();
    if (this.lastListId == -1L)
    {
      this.tasks = this.tasksDataSource.getTasks(10L);
      if (this.tasks.size() != 0)
        this.lastListId = ((Task)this.tasks.get(-1 + this.tasks.size())).getId();
    }
    while (true)
    {
      this.adapter = new TasksAdapter(this, this.tasks, this.tasksDataSource);
      this.tasksList.setAdapter(this.adapter);
      if (this.tasks.size() < 10)
        updateLoadMoreBtn();
      return;
//      this.tasks = this.tasksDataSource.getTaskWithLowerOrEqualId(this.lastListId);
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putLong("last_list_id_key", this.lastListId);
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.TasksActivity
 * JD-Core Version:    0.6.2
 */
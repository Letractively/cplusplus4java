package com.tatkovlab.pomodoro;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.tatkovlab.pomodoro.database.Task;
import com.tatkovlab.pomodoro.database.Task.TASK_STATUS;
import com.tatkovlab.pomodoro.database.TasksDataSource;
import com.tatkovlab.pomodoro.util.TypeFaceManager;
import java.util.List;

public class TasksAdapter extends BaseAdapter
{
  public static final int TOP_ROWS = 1;
  private final TasksDataSource dataSource;
  Typeface font;
  private final LayoutInflater inflater;
  private final Resources res;
  private final List<Task> tasks;

  public TasksAdapter(Context paramContext, List<Task> paramList, TasksDataSource paramTasksDataSource)
  {
    this.tasks = paramList;
    this.font = TypeFaceManager.getHandDrawnTypeFace(paramContext);
    this.inflater = LayoutInflater.from(paramContext);
    this.dataSource = paramTasksDataSource;
    this.res = paramContext.getResources();
  }

  public void addTasks(List<Task> paramList)
  {
    this.tasks.addAll(paramList);
    notifyDataSetChanged();
  }

  public int getCount()
  {
    return this.tasks.size();
  }

  public Object getItem(int paramInt)
  {
    return this.tasks.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return ((Task)this.tasks.get(paramInt)).getId();
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    TaskHolder localTaskHolder;
    if (paramView == null)
    {
      paramView = this.inflater.inflate(2130903043, paramViewGroup, false);
      localTaskHolder = new TaskHolder();
      localTaskHolder.name = ((TextView)paramView.findViewById(2131296263));
      localTaskHolder.isDone = ((CheckBox)paramView.findViewById(2131296262));
      localTaskHolder.doneExpected = ((TextView)paramView.findViewById(2131296264));
      localTaskHolder.name.setTypeface(this.font);
      localTaskHolder.doneExpected.setTypeface(this.font);
      localTaskHolder.isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
        {
          Task localTask = (Task)paramAnonymousCompoundButton.getTag();
          if (paramAnonymousBoolean)
            localTask.setStatus(Task.TASK_STATUS.FINISHED);
          while (true)
          {
            TasksAdapter.this.dataSource.updateTask(localTask);
            TasksAdapter.this.notifyDataSetChanged();
            return;
//            localTask.setStatus(Task.TASK_STATUS.NEW);
          }
        }
      });
      paramView.setTag(localTaskHolder);
      Task localTask = (Task)this.tasks.get(paramInt);
      localTaskHolder.name.setText(localTask.getName());
      localTaskHolder.doneExpected.setText(String.valueOf(localTask.getDone()) + "/" + String.valueOf(localTask.getEstimated()));
      localTaskHolder.isDone.setTag(localTask);
      if (localTask.getStatus() != Task.TASK_STATUS.FINISHED)
//        break label281;
      localTaskHolder.isDone.setChecked(true);
      localTaskHolder.name.setTextColor(this.res.getColor(2131034113));
      localTaskHolder.name.setBackgroundDrawable(this.res.getDrawable(2130837560));
      localTaskHolder.doneExpected.setTextColor(this.res.getColor(2131034113));
    }
    while (true)
    {
      return paramView;
//      localTaskHolder = (TaskHolder)paramView.getTag();
//      break;
//      label281: localTaskHolder.isDone.setChecked(false);
//      localTaskHolder.name.setTextColor(this.res.getColor(2131034112));
//      localTaskHolder.doneExpected.setTextColor(this.res.getColor(2131034112));
//      localTaskHolder.name.setBackgroundDrawable(null);
    }
  }

  public void remove(Task paramTask)
  {
    this.tasks.remove(paramTask);
  }

  public static class TaskHolder
  {
    TextView doneExpected;
    CheckBox isDone;
    TextView name;
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.TasksAdapter
 * JD-Core Version:    0.6.2
 */
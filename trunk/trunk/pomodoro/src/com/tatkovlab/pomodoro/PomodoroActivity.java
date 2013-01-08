package com.tatkovlab.pomodoro;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.tatkovlab.pomodoro.database.Task;
import com.tatkovlab.pomodoro.database.TasksDataSource;
import com.tatkovlab.pomodoro.util.CustomToast;
import com.tatkovlab.pomodoro.util.CustomWakeLock;
import com.tatkovlab.pomodoro.util.MyNotificationManager;
import com.tatkovlab.pomodoro.util.RingManager;
import com.tatkovlab.pomodoro.util.TypeFaceManager;

public class PomodoroActivity extends Activity
{
  public static final int LONG_BREAK_INTERVAL = 4;
  private int TIMER_COUNT = 0;
  Task activeTask = null;
  CompoundButton.OnCheckedChangeListener breakCheckListener = new CompoundButton.OnCheckedChangeListener()
  {
    public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean)
      {
        PomodoroActivity.this.doUnbindService();
        PomodoroActivity.this.setPomodoroButton(PomodoroActivity.this.startButton);
        PomodoroActivity.this.timeLine.setTimerState(TimeLineView.TIMER_STATE.POMODORO);
      }
    }
  };
  LocalService.TickCalback breakTickCallback = new LocalService.TickCalback()
  {
    public void onFinish()
    {
      PomodoroActivity.this.timeLine.setMargin(PomodoroActivity.this.timeLine.getTotalInterval());
      PomodoroActivity.this.doUnbindService();
      PomodoroActivity.this.notificationManager.showNotification(R.drawable.ic_launcher , PomodoroActivity.this.getText( R.string.pause_finished_title ), PomodoroActivity.this.getText( R.string.pause_finished_content ), 16);
      PomodoroActivity.this.ringManager.ring();
      PomodoroActivity.this.timeLine.setTimerState(TimeLineView.TIMER_STATE.POMODORO);
      PomodoroActivity.this.setPomodoroButton(PomodoroActivity.this.startButton);
    }

    public void onTick(int paramAnonymousInt)
    {
      PomodoroActivity.this.timeLine.setMargin(paramAnonymousInt);
    }
  };
  private LocalService mBoundService;
  private final ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      PomodoroActivity.this.mBoundService = ((LocalService.LocalBinder)paramAnonymousIBinder).getService();
      if (PomodoroActivity.this.waitingTimerTask != null)
        PomodoroActivity.this.mBoundService.startCounting(PomodoroActivity.this.waitingTimerTask);
      PomodoroActivity.this.waitingTimerTask = null;
    }

    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      PomodoroActivity.this.mBoundService = null;
    }
  };
  private boolean mIsBound;
  DisplayMetrics metrics;
  RelativeLayout noteArea;
  MyNotificationManager notificationManager;
  LinearLayout picturesHolder;
  LocalService.TickCalback podomoroTickCallback = new LocalService.TickCalback()
  {
    public void onFinish()
    {
      PomodoroActivity.this.timeLine.setMargin(PomodoroActivity.this.timeLine.getTotalInterval());
      PomodoroActivity.this.doUnbindService();
      PomodoroActivity.this.notificationManager.showNotification(R.drawable.ic_launcher, PomodoroActivity.this.getText( R.string.pause_finished_title ), PomodoroActivity.this.getText( R.string.pause_finished_content ), 16);
      PomodoroActivity.this.ringManager.ring();
      PomodoroActivity.this.showAfterPomodoroPopUp();
    }

    public void onTick(int paramAnonymousInt)
    {
      PomodoroActivity.this.timeLine.setMargin(paramAnonymousInt);
    }
  };
  CompoundButton.OnCheckedChangeListener pomodoroCheckListener = new CompoundButton.OnCheckedChangeListener()
  {
    public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean)
      {
        PomodoroTimerTask localPomodoroTimerTask = PomodoroTimerTask.buildPomodoroTask(PomodoroActivity.this.timeLine, PomodoroActivity.this.podomoroTickCallback, R.drawable.ic_launcher, PomodoroActivity.this.getText( R.string.pomodoro_running_title ), PomodoroActivity.this.getText( R.string.pomodoro_running_content ));
        PomodoroActivity.this.startPomodorTask(localPomodoroTimerTask);
      }
      while (true)
      {
        return;
        //PomodoroActivity.this.timeLine.setMargin(0);
        //PomodoroActivity.this.doUnbindService();
      }
    }
  };
  private PopupWindow pw;
  RingManager ringManager;
  ToggleButton startButton;
  TasksDataSource taskDataSource;
  TextView taskTextView;
  TimeLineView timeLine;
  private PomodoroTimerTask waitingTimerTask = null;
  CustomWakeLock wakeLock;

  private void askForBreakLengthPopUp()
  {
    try
    {
      View localView = ((LayoutInflater)getSystemService("layout_inflater")).inflate( R.layout.pause_selection_popup , (ViewGroup)findViewById( R.id.popUpRootElement ));
      this.pw = new PopupWindow(localView, (int)(220.0F * this.metrics.density), (int)(260.0F * this.metrics.density), true);
      this.pw.showAtLocation(localView, 17, 0, 0);
      Button localButton1 = (Button)localView.findViewById( R.id.shortBreak );
      Button localButton2 = (Button)localView.findViewById(R.id.longBreak);
      localButton1.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          PomodoroActivity.this.dismissPopUp();
          PomodoroActivity.this.initiateShortPauseTimer();
        }
      });
      localButton2.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          PomodoroActivity.this.dismissPopUp();
          PomodoroActivity.this.initiateLongPauseTimer();
        }
      });
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  private void dismissPopUp()
  {
    this.pw.dismiss();
  }

  private void initiateLongPauseTimer()
  {
    this.timeLine.setTimerState(TimeLineView.TIMER_STATE.LONG_BREAK);
    setBreakButton(this.startButton);
    startPomodorTask(PomodoroTimerTask.buildPomodoroTask(this.timeLine, this.breakTickCallback, R.drawable.ic_launcher, getText(R.string.pause_running_title), getText(R.string.pause_running_content)));
  }

  private void initiatePauseTimer()
  {
    this.TIMER_COUNT = (1 + this.TIMER_COUNT);
    if (this.TIMER_COUNT % 4 == 0)
      askForBreakLengthPopUp();
    while (true)
    {
      return;
      //initiateShortPauseTimer();
    }
  }

  private void initiateShortPauseTimer()
  {
    this.timeLine.setTimerState(TimeLineView.TIMER_STATE.SHORT_BREAK);
    setBreakButton(this.startButton);
    startPomodorTask(PomodoroTimerTask.buildPomodoroTask(this.timeLine, this.breakTickCallback, R.drawable.ic_launcher, getText(R.string.pause_running_title), getText(R.string.pause_running_content)));
  }

  private void setBreakButton(ToggleButton paramToggleButton)
  {
    paramToggleButton.setTextOff(getString( R.string.skip_break ));
    paramToggleButton.setTextOn(getString(R.string.skip_break));
    paramToggleButton.setOnCheckedChangeListener(this.breakCheckListener);
    paramToggleButton.setChecked(false);
  }

  private void setPomodoroButton(ToggleButton paramToggleButton)
  {
    paramToggleButton.setTextOff(getString(R.string.start_pomodoro));
    paramToggleButton.setTextOn(getString(R.string.abandon_pomodoro));
    paramToggleButton.setOnCheckedChangeListener(null);
    paramToggleButton.setChecked(false);
    paramToggleButton.setOnCheckedChangeListener(this.pomodoroCheckListener);
    paramToggleButton.invalidate();
  }

  private void showAfterPomodoroPopUp()
  {
    try
    {
      View localView = ((LayoutInflater)getSystemService("layout_inflater")).inflate(R.layout.finished_podomoro_popup, (ViewGroup)findViewById(R.id.popUpRootElement));
      this.pw = new PopupWindow(localView, (int)(220.0F * this.metrics.density), (int)(220.0F * this.metrics.density), true);
      this.pw.showAtLocation(localView, 17, 0, 0);
      ((Button)localView.findViewById(R.id.completedBtn)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          PomodoroActivity.this.dismissPopUp();
          ImageView localImageView = new ImageView(PomodoroActivity.this);
          localImageView.setImageResource(R.id.completedBtn);
          PomodoroActivity.this.picturesHolder.addView(localImageView);
          PomodoroActivity.this.initiatePauseTimer();
          if (PomodoroActivity.this.activeTask != null)
          {
            PomodoroActivity.this.activeTask.setDone(1L + PomodoroActivity.this.activeTask.getDone());
            PomodoroActivity.this.taskDataSource.updateTask(PomodoroActivity.this.activeTask);
          }
        }
      });
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  private void startPomodorTask(PomodoroTimerTask paramPomodoroTimerTask)
  {
    CustomToast.makeText(getApplicationContext(), paramPomodoroTimerTask.getMessage(), 0).show();
    this.wakeLock.acquire();
    this.ringManager.startTicking();
    this.notificationManager.cancel();
    if (this.mIsBound)
      this.mBoundService.startCounting(paramPomodoroTimerTask);
    while (true)
    {
      return;
      //doBindService();
      //this.waitingTimerTask = paramPomodoroTimerTask;
    }
  }

  void doBindService()
  {
    bindService(new Intent(this, LocalService.class), this.mConnection, 1);
    this.mIsBound = true;
  }

  void doUnbindService()
  {
    if (this.mIsBound)
    {
      unbindService(this.mConnection);
      this.mIsBound = false;
      this.wakeLock.acquire(15000L);
      this.wakeLock.release();
      this.ringManager.stopTicking();
    }
  }

  public void onBackPressed()
  {
    moveTaskToBack(true);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.main);
    this.ringManager = RingManager.getInstance(this);
    this.notificationManager = new MyNotificationManager(this, R.string.app_name);
    this.taskDataSource = new TasksDataSource(this);
    this.taskDataSource.open();
    this.wakeLock = new CustomWakeLock(this);
    ImageView localImageView = (ImageView)findViewById( R.id.timeline );
    LinearLayout localLinearLayout = (LinearLayout)findViewById( R.id.timelineBackground );
    Display localDisplay = getWindowManager().getDefaultDisplay();
    this.metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(this.metrics);
    this.timeLine = new TimeLineView(this, localImageView, localLinearLayout, this.metrics, localDisplay);
    ((Button)findViewById( R.id.settings )).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        PomodoroActivity.this.startActivity(new Intent(PomodoroActivity.this, SettingsActivity.class));
      }
    });
    this.startButton = ((ToggleButton)findViewById( R.id.start ));
    setPomodoroButton(this.startButton);
    this.taskTextView = ((TextView)findViewById( R.id.taskTextView ));
    this.taskTextView.setTypeface(TypeFaceManager.getHandDrawnTypeFace(this));
    this.picturesHolder = ((LinearLayout)findViewById( R.id.picturesHolder ));
    this.noteArea = ((RelativeLayout)findViewById( R.id.noteArea ));
    this.noteArea.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(PomodoroActivity.this, TasksActivity.class);
        localIntent.setFlags(131072);
        PomodoroActivity.this.startActivityIfNeeded(localIntent, -1);
      }
    });
  }

  protected void onDestroy()
  {
    super.onDestroy();
    doUnbindService();
    this.wakeLock.release();
    this.taskDataSource.close();
  }

  protected void onPause()
  {
    super.onPause();
    this.wakeLock.release();
  }

  public void onResume()
  {
    super.onResume();
    this.activeTask = this.taskDataSource.getActiveTask();
    int i;
    if (this.activeTask != null)
    {
      this.taskTextView.setText(this.activeTask.getName());
      this.picturesHolder.removeAllViews();
      i = 0;
      if (i < this.activeTask.getDone());
    }
    while (true)
    {
      if (this.mIsBound)
        this.wakeLock.acquire();
      return;
//      ImageView localImageView = new ImageView(this);
//      localImageView.setImageResource(2130837563);
//      this.picturesHolder.addView(localImageView);
//      i++;
//      break;
//      this.taskTextView.setText(getString(2131099672));
    }
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.PomodoroActivity
 * JD-Core Version:    0.6.2
 */
package com.tatkovlab.pomodoro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import com.tatkovlab.pomodoro.util.MyNotificationManager;

public class LocalService extends Service
{
  private final int NOTIFICATION = 2131099650;
  private final IBinder mBinder = new LocalBinder();
  private MyNotificationManager notificationManager;
  private CountDownTimer timer;

  public IBinder onBind(Intent paramIntent)
  {
    return this.mBinder;
  }

  public void onCreate()
  {
    this.notificationManager = new MyNotificationManager(this, 2131099652);
  }

  public void onDestroy()
  {
    this.notificationManager.cancel();
    this.timer.cancel();
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    Log.i("LocalService", "Received start id " + paramInt2 + ": " + paramIntent);
    return 1;
  }

  public void startCounting(PomodoroTimerTask paramPomodoroTimerTask)
  {
    startForeground(2131099650, this.notificationManager.createNotification(paramPomodoroTimerTask.getIcon(), paramPomodoroTimerTask.getTitle(), paramPomodoroTimerTask.getMessage(), 2));
    this.timer = new CountDownTimer(paramPomodoroTimerTask.getTimerTime(), paramPomodoroTimerTask.getTick())
    {
      public void onFinish()
      {
        //this.val$task.getCallback().onFinish();
    	
      }

      public void onTick(long paramAnonymousLong)
      {
        //int i = (int)((1.0F - (float)paramAnonymousLong / this.val$task.getTimerTime()) * this.val$task.getTotalInterval());
        //this.val$task.getCallback().onTick(i);
      }
    }.start();
  }

  public class LocalBinder extends Binder
  {
    public LocalBinder()
    {
    }

    public LocalService getService()
    {
      return LocalService.this;
    }
  }

  public static abstract interface TickCalback
  {
    public abstract void onFinish();

    public abstract void onTick(int paramInt);
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.LocalService
 * JD-Core Version:    0.6.2
 */
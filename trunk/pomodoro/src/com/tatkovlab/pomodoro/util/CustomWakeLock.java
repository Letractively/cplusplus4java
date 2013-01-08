package com.tatkovlab.pomodoro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;

public class CustomWakeLock
{
  public static final int KEEP_WAKED_AFTER_PODOMORO = 15000;
  public static final String WAKE_LOCK_TAG = "Pomodoro wake lock";
  private boolean isLocked = false;
  SharedPreferences prefs;
  PowerManager.WakeLock wakeLock;

  public CustomWakeLock(Context paramContext)
  {
    this.wakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(10, "Pomodoro wake lock");
    this.prefs = PreferenceManager.getDefaultSharedPreferences(paramContext);
  }

  public void acquire()
  {
    if (!checkSettings());
    while (true)
    {
      return;
//      if (!this.isLocked)
//      {
//        this.isLocked = true;
//        this.wakeLock.acquire();
//      }
    }
  }

  public void acquire(long paramLong)
  {
    if (!checkSettings());
    while (true)
    {
      return;
//      this.wakeLock.acquire(paramLong);
    }
  }

  public boolean checkSettings()
  {
    return this.prefs.getBoolean("keepScreenOnTag", false);
  }

  public void release()
  {
    if (this.isLocked)
    {
      this.isLocked = false;
      this.wakeLock.release();
    }
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.util.CustomWakeLock
 * JD-Core Version:    0.6.2
 */
package com.tatkovlab.pomodoro.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.tatkovlab.pomodoro.PomodoroActivity;

public class MyNotificationManager
{
  private final int NOTIFICATION;
  private final Context context;
  private final NotificationManager notificationManager;

  public MyNotificationManager(Context paramContext, int paramInt)
  {
    this.context = paramContext;
    this.NOTIFICATION = paramInt;
    this.notificationManager = ((NotificationManager)paramContext.getSystemService("notification"));
  }

  public void cancel()
  {
    this.notificationManager.cancel(this.NOTIFICATION);
  }

  public Notification createNotification(int paramInt1, CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt2)
  {
    Notification localNotification = new Notification(paramInt1, paramCharSequence2, System.currentTimeMillis());
    Intent localIntent = new Intent(this.context, PomodoroActivity.class);
    localIntent.setFlags(603979776);
    PendingIntent localPendingIntent = PendingIntent.getActivity(this.context, 0, localIntent, 0);
    localNotification.setLatestEventInfo(this.context, paramCharSequence1, paramCharSequence2, localPendingIntent);
    localNotification.flags = paramInt2;
    return localNotification;
  }

  public void showNotification(int paramInt1, CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt2)
  {
    Notification localNotification = createNotification(paramInt1, paramCharSequence1, paramCharSequence2, paramInt2);
    this.notificationManager.notify(this.NOTIFICATION, localNotification);
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.util.MyNotificationManager
 * JD-Core Version:    0.6.2
 */
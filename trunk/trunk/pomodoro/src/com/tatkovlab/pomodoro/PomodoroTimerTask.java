package com.tatkovlab.pomodoro;

public class PomodoroTimerTask
{
  private final LocalService.TickCalback callback;
  private final int icon;
  private final CharSequence message;
  private final int tick;
  private final int timerTime;
  private final CharSequence title;
  private final int totalInterval;

  public PomodoroTimerTask(int paramInt1, int paramInt2, int paramInt3, LocalService.TickCalback paramTickCalback, int paramInt4, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    this.timerTime = paramInt1;
    this.tick = paramInt2;
    this.totalInterval = paramInt3;
    this.callback = paramTickCalback;
    this.icon = paramInt4;
    this.title = paramCharSequence1;
    this.message = paramCharSequence2;
  }

  public static PomodoroTimerTask buildPomodoroTask(TimeLineView paramTimeLineView, LocalService.TickCalback paramTickCalback, int paramInt, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    return new PomodoroTimerTask(paramTimeLineView.getTimerTime(), paramTimeLineView.getTickInterval(), paramTimeLineView.getTotalInterval(), paramTickCalback, paramInt, paramCharSequence1, paramCharSequence2);
  }

  public LocalService.TickCalback getCallback()
  {
    return this.callback;
  }

  public int getIcon()
  {
    return this.icon;
  }

  public CharSequence getMessage()
  {
    return this.message;
  }

  public int getTick()
  {
    return this.tick;
  }

  public int getTimerTime()
  {
    return this.timerTime;
  }

  public CharSequence getTitle()
  {
    return this.title;
  }

  public int getTotalInterval()
  {
    return this.totalInterval;
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.PomodoroTimerTask
 * JD-Core Version:    0.6.2
 */
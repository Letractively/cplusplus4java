package com.tatkovlab.pomodoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TimeLineView
{
  private static final int MINUTE = 60000;
  private static final int SECOND = 1000;
  private static final int TICK_INTERVAL = 2000;
  public static final int TIMELINE_MARGIN = 10;
  public static final int TIMELINE_WIDTH = 672;
  private static final int TIMER_TIME = 1500000;
  private int CURRENT_LONG_BREAK_START_MARGIN;
  private int CURRENT_LONG_BREAK_TIME;
  private int CURRENT_LONG_BREAK_TIMER_INTERVAL;
  private int CURRENT_SHORT_BREAK_START_MARGIN;
  private int CURRENT_SHORT_BREAK_TIME;
  private int CURRENT_SHORT_BREAK_TIMER_INTERVAL;
  private final Context appContex;
  private TIMER_STATE current_state = TIMER_STATE.POMODORO;
  private final int finishMargin;
  SharedPreferences prefs;
  private final int startMargin;
  ImageView timeLine;
  private final LinearLayout timelineBackground;
  private final int totalInterval;

  public TimeLineView(Context paramContext, ImageView paramImageView, LinearLayout paramLinearLayout, DisplayMetrics paramDisplayMetrics, Display paramDisplay)
  {
    this.timeLine = paramImageView;
    this.timelineBackground = paramLinearLayout;
    this.prefs = PreferenceManager.getDefaultSharedPreferences(paramContext);
    this.appContex = paramContext.getApplicationContext();
    int i = paramDisplay.getWidth();
    this.startMargin = ((int)(-(672.0F * paramDisplayMetrics.density) + i / 2 + 10.0F * paramDisplayMetrics.density));
    this.finishMargin = ((int)(i / 2 - 10.0F * paramDisplayMetrics.density));
    this.totalInterval = (Math.abs(this.startMargin) + Math.abs(this.finishMargin));
    this.CURRENT_SHORT_BREAK_TIME = getShortBreakMinutes();
    this.CURRENT_LONG_BREAK_TIME = getLongBreakMinutes();
    this.CURRENT_SHORT_BREAK_TIMER_INTERVAL = calculateTimerInterval(this.CURRENT_SHORT_BREAK_TIME);
    this.CURRENT_SHORT_BREAK_START_MARGIN = calculateStartMargin(this.CURRENT_SHORT_BREAK_TIME);
    this.CURRENT_LONG_BREAK_TIMER_INTERVAL = calculateTimerInterval(this.CURRENT_LONG_BREAK_TIME);
    this.CURRENT_LONG_BREAK_START_MARGIN = calculateTimerInterval(this.CURRENT_LONG_BREAK_TIME);
    setMargin(0);
  }

  private int calculateAddition(int paramInt)
  {
    return (int)(this.totalInterval * ((1500000 - paramInt) / 1500000.0F));
  }

  private int calculateStartMargin(int paramInt)
  {
    return this.startMargin + calculateAddition(paramInt);
  }

  private int calculateTimerInterval(int paramInt)
  {
    int i = calculateAddition(paramInt);
    if (this.finishMargin - (this.totalInterval - i) > 0);
    for (int j = this.finishMargin - (this.finishMargin - (this.totalInterval - i)); ; j = (int)Math.abs(this.totalInterval * (1.0F - (1500000 - paramInt) / 1500000.0F)))
      return j;
  }

  private int getLongBreakMinutes()
  {
    return 60000 * Integer.parseInt(this.appContex.getResources().getStringArray(2131165187)[this.prefs.getInt("longBreakTag", 1)]);
  }

  private int getShortBreakMinutes()
  {
    return 60000 * Integer.parseInt(this.appContex.getResources().getStringArray(2131165185)[this.prefs.getInt("shortBreakTag", 0)]);
  }

  private int getStartMargin(TIMER_STATE paramTIMER_STATE)
  {
    int i=0;
    switch (paramTIMER_STATE.ordinal())
    {
    default:
      i = this.startMargin;
    case 1:
    case 2:
    }
    while (true)
    {
      return i;
//      i = this.CURRENT_SHORT_BREAK_START_MARGIN;
//      continue;
//      i = this.CURRENT_LONG_BREAK_START_MARGIN;
    }
  }

  private void initializeLongBreakTimer()
  {
    this.current_state = TIMER_STATE.LONG_BREAK;
    this.CURRENT_LONG_BREAK_TIME = getLongBreakMinutes();
    this.CURRENT_LONG_BREAK_TIMER_INTERVAL = calculateTimerInterval(this.CURRENT_LONG_BREAK_TIME);
    this.CURRENT_LONG_BREAK_START_MARGIN = calculateStartMargin(this.CURRENT_LONG_BREAK_TIME);
    setBackground(BACKGROUND_COLORS.GREEN);
    setMargin(0);
  }

  private void initializePomodoroTimer()
  {
    this.current_state = TIMER_STATE.POMODORO;
    setBackground(BACKGROUND_COLORS.RED);
    setMargin(0);
  }

  private void initializeShortBreakTimer()
  {
    this.current_state = TIMER_STATE.SHORT_BREAK;
    this.CURRENT_SHORT_BREAK_TIME = getShortBreakMinutes();
    this.CURRENT_SHORT_BREAK_TIMER_INTERVAL = calculateTimerInterval(this.CURRENT_SHORT_BREAK_TIME);
    this.CURRENT_SHORT_BREAK_START_MARGIN = calculateStartMargin(this.CURRENT_SHORT_BREAK_TIME);
    setBackground(BACKGROUND_COLORS.GREEN);
    setMargin(0);
  }

  private void setBackground(BACKGROUND_COLORS paramBACKGROUND_COLORS)
  {
    switch ( paramBACKGROUND_COLORS.ordinal() )
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      return;
//      this.timelineBackground.setBackgroundResource(2130837508);
//      continue;
//      this.timelineBackground.setBackgroundResource(2130837509);
    }
  }

  public int getTickInterval()
  {
    return 2000;
  }

  public int getTimerTime()
  {
    int i=0;
    switch ( this.current_state.ordinal() )
    {
    default:
      i = 1500000;
    case 1:
    case 2:
    }
    while (true)
    {
      return i;
//      i = this.CURRENT_SHORT_BREAK_TIME;
//      continue;
//      i = this.CURRENT_LONG_BREAK_TIME;
    }
  }

  public int getTotalInterval()
  {
    int i=0;
    switch ( this.current_state.ordinal())
    {
    default:
      i = this.totalInterval;
    case 1:
    case 2:
    }
    while (true)
    {
      return i;
//      i = this.CURRENT_SHORT_BREAK_TIMER_INTERVAL;
//      continue;
//      i = this.CURRENT_LONG_BREAK_TIMER_INTERVAL;
    }
  }

  public void setMargin(int paramInt)
  {
    int i = paramInt + getStartMargin(this.current_state);
    ViewGroup.MarginLayoutParams localMarginLayoutParams = new ViewGroup.MarginLayoutParams(this.timeLine.getLayoutParams());
    localMarginLayoutParams.setMargins(i, 0, 0, 0);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(localMarginLayoutParams);
    this.timeLine.setLayoutParams(localLayoutParams);
  }

  public void setTimerState(TIMER_STATE paramTIMER_STATE)
  {
    switch ( paramTIMER_STATE.ordinal() )
    {
    default:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return;
//      initializeShortBreakTimer();
//      continue;
//      initializeLongBreakTimer();
//      continue;
//      initializePomodoroTimer();
    }
  }

  private static enum BACKGROUND_COLORS
  {
	  GREEN(1),RED(0);
	  
	  final int numericValue;
	  private BACKGROUND_COLORS(int arg3)
	    {
	      this.numericValue = arg3;
	    }
	  
    static
    {
      BACKGROUND_COLORS[] arrayOfBACKGROUND_COLORS = new BACKGROUND_COLORS[2];
      arrayOfBACKGROUND_COLORS[0] = RED;
      arrayOfBACKGROUND_COLORS[1] = GREEN;
    }
  }

  public static enum TIMER_STATE
  {
	  LONG_BREAK(1),POMODORO(2),SHORT_BREAK(0);
	  final int numericValue;
	  private TIMER_STATE(int arg3)
	    {
	      this.numericValue = arg3;
	    }
	  
	  
    static
    {
      TIMER_STATE[] arrayOfTIMER_STATE = new TIMER_STATE[3];
      arrayOfTIMER_STATE[0] = SHORT_BREAK;
      arrayOfTIMER_STATE[1] = LONG_BREAK;
      arrayOfTIMER_STATE[2] = POMODORO;
    }
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.TimeLineView
 * JD-Core Version:    0.6.2
 */
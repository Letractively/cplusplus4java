package com.tatkovlab.pomodoro.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast
{
  public static Toast makeText(Context paramContext, int paramInt1, int paramInt2)
  {
    return makeText(paramContext, paramContext.getString(paramInt1), paramInt2);
  }

  public static Toast makeText(Context paramContext, CharSequence paramCharSequence, int paramInt)
  {
    View localView = LayoutInflater.from(paramContext).inflate(2130903051, null);
    ((TextView)localView.findViewById(2131296283)).setText(paramCharSequence);
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay().getMetrics(localDisplayMetrics);
    Toast localToast = new Toast(paramContext);
    localToast.setGravity(80, 0, (int)(60.0F * localDisplayMetrics.density));
    localToast.setDuration(1);
    localToast.setView(localView);
    return localToast;
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.util.CustomToast
 * JD-Core Version:    0.6.2
 */
package com.tatkovlab.pomodoro.util;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFaceManager
{
  private static Typeface font;

  public static Typeface getHandDrawnTypeFace(Context paramContext)
  {
    if (font == null)
      font = Typeface.createFromAsset(paramContext.getAssets(), "SeanHand.ttf");
    return font;
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.util.TypeFaceManager
 * JD-Core Version:    0.6.2
 */
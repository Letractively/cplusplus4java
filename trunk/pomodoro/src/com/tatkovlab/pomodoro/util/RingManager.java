package com.tatkovlab.pomodoro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Vibrator;
import android.preference.PreferenceManager;

public class RingManager
{
  private static final float MIN_VOLUME_FORTICKING = 0.03F;
  private static RingManager managerInstance = null;
  AudioManager audioManager;
  Context context;
  boolean isTicking = false;
  SharedPreferences prefs;
  MediaPlayer ringingPlayer;
  MediaPlayer tickingPlayer;
  Vibrator vibrator;

  protected RingManager(Context paramContext)
  {
    this.prefs = PreferenceManager.getDefaultSharedPreferences(paramContext);
    this.vibrator = ((Vibrator)paramContext.getSystemService("vibrator"));
    this.ringingPlayer = MediaPlayer.create(paramContext, 2130968578);
    this.tickingPlayer = MediaPlayer.create(paramContext, 2130968579);
    this.tickingPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener()
    {
      public void onSeekComplete(MediaPlayer paramAnonymousMediaPlayer)
      {
      }
    });
    this.context = paramContext;
    this.audioManager = ((AudioManager)paramContext.getSystemService("audio"));
  }

  public static RingManager getInstance(Context paramContext)
  {
    if (managerInstance == null)
      managerInstance = new RingManager(paramContext.getApplicationContext());
    return managerInstance;
  }

  private boolean isTickingOn(float paramFloat)
  {
    if (paramFloat < 0.03F);
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public void ring()
  {
    stopTicking();
    if (this.prefs.getBoolean("isVibrationTag", false))
    {
      Vibrator localVibrator = this.vibrator;
      long[] arrayOfLong = new long[4];
      arrayOfLong[1] = 200L;
      arrayOfLong[2] = 300L;
      arrayOfLong[3] = 200L;
      localVibrator.vibrate(arrayOfLong, -1);
    }
    float f = this.prefs.getInt("ringSoundTag", 50) / 100.0F;
    this.ringingPlayer.setScreenOnWhilePlaying(true);
    this.ringingPlayer.setVolume(f, f);
    this.ringingPlayer.start();
  }

  public void startTicking()
  {
    this.isTicking = true;
    float f = this.prefs.getInt("tickingTag", 50) / 100.0F;
    if (isTickingOn(f))
    {
      this.tickingPlayer.setVolume(f, f);
      this.tickingPlayer.setLooping(true);
      this.tickingPlayer.start();
    }
  }

  public void stopTicking()
  {
    this.isTicking = false;
    if (this.tickingPlayer.isPlaying())
      this.tickingPlayer.pause();
  }

  public void updateTickingVolume()
  {
    float f = this.prefs.getInt("tickingTag", 50) / 100.0F;
    if (!isTickingOn(f))
      if (this.tickingPlayer.isPlaying())
        this.tickingPlayer.pause();
    while (true)
    {
      return;
//      if ((!this.tickingPlayer.isPlaying()) && (this.isTicking))
//        startTicking();
//      else
//        this.tickingPlayer.setVolume(f, f);
    }
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.util.RingManager
 * JD-Core Version:    0.6.2
 */
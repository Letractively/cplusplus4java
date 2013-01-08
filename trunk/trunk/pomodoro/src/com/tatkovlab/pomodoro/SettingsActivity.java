package com.tatkovlab.pomodoro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import com.tatkovlab.pomodoro.util.RingManager;

public class SettingsActivity extends Activity
{
  public static final String KEEP_SCREEN_ON_TAG = "keepScreenOnTag";
  public static final String LONG_BREAK_TAG = "longBreakTag";
  public static final String RINGING_TAG = "ringSoundTag";
  public static final String SHORT_BREAK_TAG = "shortBreakTag";
  public static final String TICKING_TAG = "tickingTag";
  public static final String VIBRATION_TAG = "isVibrationTag";
  SharedPreferences prefs;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903048);
    this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
    int i = this.prefs.getInt("ringSoundTag", 50);
    SeekBar localSeekBar1 = (SeekBar)findViewById(2131296276);
    localSeekBar1.setProgress(i);
    SeekBar.OnSeekBarChangeListener local1 = new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
      }

      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
      }

      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        SharedPreferences.Editor localEditor = SettingsActivity.this.prefs.edit();
        localEditor.putInt("ringSoundTag", paramAnonymousSeekBar.getProgress());
        localEditor.commit();
      }
    };
    localSeekBar1.setOnSeekBarChangeListener(local1);
    int j = this.prefs.getInt("tickingTag", 50);
    SeekBar localSeekBar2 = (SeekBar)findViewById(2131296277);
    localSeekBar2.setProgress(j);
    SeekBar.OnSeekBarChangeListener local2 = new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
      }

      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
      }

      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        SharedPreferences.Editor localEditor = SettingsActivity.this.prefs.edit();
        localEditor.putInt("tickingTag", paramAnonymousSeekBar.getProgress());
        localEditor.commit();
        RingManager.getInstance(SettingsActivity.this).updateTickingVolume();
      }
    };
    localSeekBar2.setOnSeekBarChangeListener(local2);
    boolean bool1 = this.prefs.getBoolean("isVibrationTag", false);
    CheckBox localCheckBox1 = (CheckBox)findViewById(2131296278);
    localCheckBox1.setChecked(bool1);
    CompoundButton.OnCheckedChangeListener local3 = new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        SharedPreferences.Editor localEditor = SettingsActivity.this.prefs.edit();
        localEditor.putBoolean("isVibrationTag", paramAnonymousBoolean);
        localEditor.commit();
      }
    };
    localCheckBox1.setOnCheckedChangeListener(local3);
    boolean bool2 = this.prefs.getBoolean("keepScreenOnTag", false);
    CheckBox localCheckBox2 = (CheckBox)findViewById(2131296279);
    localCheckBox2.setChecked(bool2);
    CompoundButton.OnCheckedChangeListener local4 = new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        SharedPreferences.Editor localEditor = SettingsActivity.this.prefs.edit();
        localEditor.putBoolean("keepScreenOnTag", paramAnonymousBoolean);
        localEditor.commit();
      }
    };
    localCheckBox2.setOnCheckedChangeListener(local4);
    int k = this.prefs.getInt("shortBreakTag", 0);
    Spinner localSpinner1 = (Spinner)findViewById(2131296280);
    ArrayAdapter localArrayAdapter1 = ArrayAdapter.createFromResource(this, 2131165184, 2130903049);
    localArrayAdapter1.setDropDownViewResource(17367049);
    localSpinner1.setAdapter(localArrayAdapter1);
    localSpinner1.setSelection(k);
    AdapterView.OnItemSelectedListener local5 = new AdapterView.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        SharedPreferences.Editor localEditor = SettingsActivity.this.prefs.edit();
        localEditor.putInt("shortBreakTag", paramAnonymousInt);
        localEditor.commit();
      }

      public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView)
      {
      }
    };
    localSpinner1.setOnItemSelectedListener(local5);
    int m = this.prefs.getInt("longBreakTag", 1);
    Spinner localSpinner2 = (Spinner)findViewById(2131296281);
    ArrayAdapter localArrayAdapter2 = ArrayAdapter.createFromResource(this, 2131165186, 2130903049);
    localArrayAdapter2.setDropDownViewResource(17367049);
    localSpinner2.setAdapter(localArrayAdapter2);
    localSpinner2.setSelection(m);
    AdapterView.OnItemSelectedListener local6 = new AdapterView.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        SharedPreferences.Editor localEditor = SettingsActivity.this.prefs.edit();
        localEditor.putInt("longBreakTag", paramAnonymousInt);
        localEditor.commit();
      }

      public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView)
      {
      }
    };
    localSpinner2.setOnItemSelectedListener(local6);
  }
}

/* Location:           D:\android\decompile\apktool\dex2jar-0.0.9.12\classes_dex2jar.jar
 * Qualified Name:     com.tatkovlab.pomodoro.SettingsActivity
 * JD-Core Version:    0.6.2
 */
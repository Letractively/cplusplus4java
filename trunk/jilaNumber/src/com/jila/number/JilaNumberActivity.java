package com.jila.number;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class JilaNumberActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String packageCodePath=getPackageCodePath();
        Log.i("PACKT", "packageCodePath:"+packageCodePath);
        
        //getResources().openRawResourceFd(R)
        String filesDir= getFilesDir().getPath();
        Log.i("PACKT", "filesDir:"+filesDir);
    }
    
    
    
    
}
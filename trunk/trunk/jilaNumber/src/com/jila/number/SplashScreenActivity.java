package com.jila.number;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

public class SplashScreenActivity extends Activity{
	private long ms=0;
	private long splashTime=2000;
	private boolean splashActive=true;
	private boolean paused=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.splashscreen);
		
		try {
			//init AppInfo
			AssetManager am= this.getAssets();
			JilaAppInfo.getInstance();
			String str="{\"key1\": \"mail1\",\"key2\": \"房间看电视看对方\"}";
			
			JSONObject json = new JSONObject( new String(str.getBytes(),"gb2312") );
			Dylib dylib=new Dylib();
			
			dylib.initializeDylib( json.toString(),am );
			JilaAppInfo.getInstance().setDylib(dylib);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread mythread=new Thread(){
			public void run(){
				try{
					while( splashActive&&ms<splashTime ){
						if(!paused)ms=ms+100;
						sleep(100);
					}
				}catch(Exception e){}
				finally{
					Intent intent=new Intent( SplashScreenActivity.this,JilaNumberActivity.class  );
					startActivity(intent);
				}
			}
		};
		mythread.start();
		
	}
	
	
}

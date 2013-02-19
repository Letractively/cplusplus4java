package com.jila.number;

import android.app.Activity;
import android.content.Intent;
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
		//init AppInfo
		JilaAppInfo.getInstance();
		
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

package com.jila.number;

import org.json.JSONException;
import org.json.JSONObject;

public class JilaAppInfo {
	
	private void init(){
		this.setDylib( new Dylib() );
		
		
		try {
			String str="{\"mailHost\": \"mail1\",\"mailHostOverride\": \"mail2\"}";
			JSONObject json = new JSONObject( str );
			this.getDylib().initializeDylib( json.toString() );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
//////////////////////////////////////////////////////////////////////////
	volatile private static JilaAppInfo one;
	
	private JilaAppInfo(){
		init();		
	}
	
	public static JilaAppInfo getInstance(){
		if(one==null){
			synchronized( JilaAppInfo.class ){
				if( one==null )one=new JilaAppInfo();
			}
		}return one;
	}
	
////////////////////////////////////////////////////////////////////////	
	
	private Dylib dylib;
	public Dylib getDylib() {
		return dylib;
	}
	public void setDylib(Dylib dylib) {
		this.dylib = dylib;
	}
	
	
	
	
}

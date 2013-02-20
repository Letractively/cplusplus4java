package com.jila.number;

import android.content.res.AssetManager;

public class Dylib {
	
	
	static{
		System.loadLibrary("jilanumber");
	}
	
	
	
	
	public native String initializeDylib( String paramstr,AssetManager am );
    public native String finalizeDylib( String paramstr );
	
    
    
    
	
}

package com.jila.number;

public class Dylib {
	
	
	static{
		System.loadLibrary("jilanumber");
	}
	
	public native String initializeDylib( String paramstr );
    public native String finalizeDylib( String paramstr );
	
    
    
    
	
}

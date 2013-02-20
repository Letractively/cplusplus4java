package com.jila.number;


public class JilaAppInfo {
	
	private void init(){
		
		
		
	}
//////////////////////////////////////////////////////////////////////////
	volatile private static JilaAppInfo one;
	
	private JilaAppInfo(){
		init();		
	}
	
	public static JilaAppInfo getInstance(  ){
		if(one==null){
			synchronized( JilaAppInfo.class ){
				if( one==null ){					
					one=new JilaAppInfo();
				}
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

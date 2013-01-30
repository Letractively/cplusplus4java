package com.jlg.sms;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ResponserService extends Service {
	
	private TimeListDatabaseHelper databaseHelper;
	private Map<String,String> jlgMap;
	
	//The Action fired by the Android-System when a SMS was received.
	private static final String RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private static final String SENT_ACTION="SENT_SMS";
	private static final String DELIVERED_ACTION="DELIVERED_SMS";

	String requester;
	String reply="";  
	SharedPreferences myprefs;
		


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        myprefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        //init db tool
        databaseHelper = new TimeListDatabaseHelper(this);
        jlgMap=new HashMap<String,String>();
        jlgMap.put("106573189888222", "yes");
        jlgMap.put("106550730107301", "yes");
        jlgMap.put("106592260177271", "yes");
        //init sms register
	    registerReceiver(sentReceiver, new IntentFilter(SENT_ACTION));
	    registerReceiver(deliverReceiver, new IntentFilter(DELIVERED_ACTION));
       
	    IntentFilter receiverfilter = new IntentFilter(RECEIVED_ACTION);
	    receiverfilter.setPriority(2147483647);//max inteager   2147483647	    
        registerReceiver(receiver, receiverfilter);
        
        IntentFilter sendfilter = new IntentFilter(SENT_ACTION);
        registerReceiver(sender,sendfilter);
	}
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		unregisterReceiver(sender);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    private BroadcastReceiver sender = new BroadcastReceiver(){
    	@Override
    	public void onReceive(Context c, Intent i)
    	{
    		if(i.getAction().equals(SENT_ACTION))
    		{
    			if(getResultCode() != Activity.RESULT_OK)
    			{
    				String reciptent = i.getStringExtra("recipient");
    				requestReceived(reciptent);
    			}
    		}
    	}
    };
    BroadcastReceiver sentReceiver = new BroadcastReceiver(){
        @Override public void onReceive(Context c, Intent in) {
            switch(getResultCode()){

                case Activity.RESULT_OK:
                    //sent SMS message successfully;
                	smsSent();
                    break;
                default:
                    //sent SMS message failed
                	smsFailed();
                    break;
             }

         }

    };
    
    public void smsSent(){
    	Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT);
    }
    public void smsFailed(){
    	Toast.makeText(this, "SMS sent failed", Toast.LENGTH_SHORT);
    }
    public void smsDelivered(){
    	Toast.makeText(this, "SMS delivered", Toast.LENGTH_SHORT);
    }
   BroadcastReceiver deliverReceiver = new BroadcastReceiver(){

        @Override public void onReceive(Context c, Intent in) {

            //SMS delivered actions
        	smsDelivered();
        }    
    };
    public void requestReceived(String f)
    {
    	Log.v("ResponserService","In requestReceived");
    	requester=f;
    }

    BroadcastReceiver receiver = new BroadcastReceiver()
    {
    	@Override
    	public void onReceive(Context c, Intent in)
    	{
    		Log.v("ResponserService","On Receive");
    		reply="";
    		if(in.getAction().equals(RECEIVED_ACTION))
    		{
        		Log.v("ResponserService","On SMS RECEIVE");
        		
    			Bundle bundle = in.getExtras();
    			if(bundle!=null)
    			{
    				Object[] pdus = (Object[])bundle.get("pdus");
    				SmsMessage[] messages = new SmsMessage[pdus.length];
    				for(int i = 0; i<pdus.length; i++)
    				{
    		    		Log.v("ResponserService","FOUND MESSAGE");
    					messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
    				}
    				for(SmsMessage one: messages)
    				{
    					String ori_address=one.getOriginatingAddress();
    					String notes=one.getMessageBody();    					
    					requestReceived(one.getOriginatingAddress());
    					//just put into DB to show in listActivity
    					Log.v("KO:","mobile:"+ori_address+"@content:"+notes);
    					//before put into db,validate jlg
    					//if( jlgMap.containsKey(ori_address)  ){
    						databaseHelper.saveRecord(ori_address, notes);
    					//}    					    					
    				}
    				//respond();  //back door
    			}
    		}

    	}
    };


	
	

    public void respond()
    {
    	Log.v("ResponserService","Responing to "+requester);
        reply = myprefs.getString("reply", "Thank you for your message. I am busy now. I will call you later");
    	SmsManager sms = SmsManager.getDefault();
    	Intent sentIn = new Intent(SENT_ACTION);
    	PendingIntent sentPIn = PendingIntent.getBroadcast(this,0,sentIn,0);

    	Intent deliverIn = new Intent(DELIVERED_ACTION);
    	PendingIntent deliverPIn = PendingIntent.getBroadcast(this,0,deliverIn,0);
    	
        ArrayList<String> Msgs = sms.divideMessage(reply);
        ArrayList<PendingIntent> sentIns = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliverIns = new ArrayList<PendingIntent>();

        for(int i=0; i< Msgs.size(); i++){
        	sentIns.add(sentPIn);
            deliverIns.add(deliverPIn);
        }
      	
//        sms.sendMultipartTextMessage(requester, null, Msgs, sentIns, deliverIns);
//        WebView wb = new WebView(this);
        
    }


	

}

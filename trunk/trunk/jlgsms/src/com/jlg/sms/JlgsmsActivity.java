package com.jlg.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class JlgsmsActivity extends Activity {
	
	private TimeTrackerAdapter timeTrackerAdapter;
	private TimeListDatabaseHelper databaseHelper;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        //start service
        Intent intent=new Intent(JlgsmsActivity.this,ResponserService.class);
        startService(intent);
        //init main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        databaseHelper = new TimeListDatabaseHelper(this);
        ListView listView = (ListView) findViewById(R.id.times_list);
        timeTrackerAdapter = new TimeTrackerAdapter(this,databaseHelper.getAllTimeRecords());
        listView.setAdapter(timeTrackerAdapter);
    }
    
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	//refresh view from db
    	
    	timeTrackerAdapter.changeCursor(  databaseHelper.getAllTimeRecords() );
    	timeTrackerAdapter.notifyDataSetChanged();
    }
    
    public void onUpdateContactButtonClicked(View view){
    	 Button button=(Button)findViewById( R.id.update_contact_button );
    	//refresh view from db
    	 timeTrackerAdapter.changeCursor(  databaseHelper.getAllTimeRecords() );
    	 timeTrackerAdapter.notifyDataSetChanged();
    }
    
}
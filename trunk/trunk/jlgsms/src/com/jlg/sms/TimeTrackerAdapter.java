package com.jlg.sms;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TimeTrackerAdapter extends CursorAdapter {
	
	public TimeTrackerAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView timeTextView = (TextView) view.findViewById(R.id.time_view);
		timeTextView.setText(cursor.getString(1));
		TextView notesTextView = (TextView) view.findViewById(R.id.notes_view);
		notesTextView.setText(cursor.getString(2));
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.time_list_item, parent, false);
		return view;
	}

}


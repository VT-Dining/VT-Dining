package com.vtdining;

import android.os.Message;

import android.os.Handler;

import android.os.Looper;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VT_Dining extends Activity {
    /** Called when the activity is first created. */
    private VT_Dining m;
    private Day day;
    private LinearLayout locations;
    private TextView date;
    private Handler h;
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	m = this;
	date=(TextView)findViewById(R.id.date);
	locations = (LinearLayout) findViewById(R.id.locations);
	 h=new Handler() {
	    public void handleMessage(Message m) {
		setUp();
	    }
	};
	load();
    }

    public void load() {
	new Thread() {
	    public void run() {
		Looper.prepare();
		try {
		    day = new Day(5, 9, 2011);
		    h.sendEmptyMessage(0);
		} catch (Exception e) {
		    Toast.makeText(m, "35 " + e, Toast.LENGTH_LONG).show();
		}
	    }
	}.start();
    }

    public void setUp() {
	try {
	    for (Location l : day.getLocations()) {
		ViewGroup location = (ViewGroup) getLayoutInflater().inflate(
			R.layout.location, null);
		((TextView) location.getChildAt(0)).setText(l.getName());
		((TextView) location.getChildAt(1)).setText(l.getTimes());
		locations.addView(location);
		location.invalidate();
	    }
	} catch (Exception e) {
	    Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
	}
    }
}
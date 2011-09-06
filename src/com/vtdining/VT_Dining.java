package com.vtdining;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VT_Dining extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	LinearLayout locations = (LinearLayout) findViewById(R.id.locations);
	try {
	    Day day=new Day(5,9,2011);

	    for(Location l:day.getLocations())
	    {
		LinearLayout location = (LinearLayout)getLayoutInflater()
		    .inflate(R.layout.location, null);
	    ((TextView)location.getChildAt(0)).setText(l.getName());
	    locations.addView(location);
	    }
	}catch(Exception e) {}

    }
}
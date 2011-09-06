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

	for (int i = 0; i < 10; i++) {
	    LinearLayout location = (LinearLayout)getLayoutInflater()
		    .inflate(R.layout.location, null);
	    ((TextView)location.getChildAt(0)).setText("asdf "+i);
	    locations.addView(location);
	}

    }
}
package com.vtdining;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.ImageButton;
=======
import android.widget.ImageView;
>>>>>>> 771ac97955757c73b453477be7b00e5aef5e2925
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VT_Dining extends Activity {
    /** Called when the activity is first created. */
<<<<<<< HEAD
    private Day date;
=======
    private Day day;
>>>>>>> 771ac97955757c73b453477be7b00e5aef5e2925
    private LinearLayout locations;
    private TextView dateDisplay;
    private ImageButton back, calendar, forward;
    private Handler h;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);
<<<<<<< HEAD
	dateDisplay = (TextView) findViewById(R.id.date);
	back = (ImageButton) findViewById(R.id.back);
	calendar = (ImageButton) findViewById(R.id.calendar);
	forward = (ImageButton) findViewById(R.id.next);
=======
	date=(TextView)findViewById(R.id.date);
>>>>>>> 771ac97955757c73b453477be7b00e5aef5e2925
	locations = (LinearLayout) findViewById(R.id.locations);
	h = new Handler() {
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
		    h.sendEmptyMessage(0);
		} catch (Exception e) {
		}
	    }
	}.start();
    }

    public void setUp() {
	try {

	    dateDisplay.setText();
	    for (Location l : date.getLocations()) {
		ViewGroup location = (ViewGroup) getLayoutInflater().inflate(
			R.layout.location, null);
		((TextView) location.getChildAt(0)).setText(l.getName()); //make less retarded
		((TextView) location.getChildAt(1)).setText(l.getTimes());
		((ImageView)location.getChildAt(2)).setImageResource(R.drawable.red);
		locations.addView(location);
	    }
	} catch (Exception e) {
	    Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
	}
    }

}
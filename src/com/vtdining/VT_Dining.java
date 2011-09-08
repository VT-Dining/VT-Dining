package com.vtdining;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormatSymbols;

public class VT_Dining extends Activity {
    /** Called when the activity is first created. */
    private Days date;
    private LinearLayout locations;
    private TextView dateDisplay;
    private Handler h;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	date = new Days();
	dateDisplay = (TextView) findViewById(R.id.date);
	locations = (LinearLayout) findViewById(R.id.locations);
	h = new Handler() {
	    public void handleMessage(Message m) {
		if (m.what == 1)
		    printEx((Exception) m.obj);
		else
		    setUp();
	    }
	};
	load();
    }

    public void tText(String text) {
	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void load() {
	if(date.requiresLoading())
	dateDisplay.setText("Loading...");
	new Thread() {
	    public void run() {
		Looper.prepare();
		try {
		    date.load();
		    h.sendEmptyMessage(0);
		} catch (Exception e) {
		    Message m = new Message();
		    m.obj = e;
		    m.what = 1;
		    h.sendMessage(m);
		}
	    }
	}.start();
    }

    public void setUp() {
	try {
	    locations.removeAllViews();
	    dateDisplay.setText(getMonth(date.getDay().getMonth()) + " "
		    + date.getDay().getDay() + ", " + date.getDay().getYear());
	    for (Location l : date.getLocations()) {
		ViewGroup location = (ViewGroup) getLayoutInflater().inflate(
			R.layout.location, null);
		((TextView) location.getChildAt(0)).setText(l.getName());
		TextView time = (TextView) location.getChildAt(1);
		time.setText(l.getTimes());
		time.setHeight(0);
		((ImageView) location.getChildAt(2))
			.setImageResource(getColorID(l));
		locations.addView(location);
	    }
	} catch (Exception e) {
	    printEx(e);
	}

    }

    private String getMonth(int month) {
	return ""+new DateFormatSymbols().getMonths()[month-1];
    }

    public void expandItem(View v) {
	TextView time = (TextView) ((ViewGroup) v).getChildAt(1);
	if (time.getHeight() == 0)
	    time.setHeight(time.getLineHeight() * time.getLineCount() + 5);
	else
	    time.setHeight(0);
    }

    public void incDay(View v) {
	date.incDay();
	load();
    }

    public void decDay(View v) {
	date.decDay();
	load();
    }

    private int getColorID(Location l) {
	if (date.open(l)) {
	    if (date.hurry(l))
		return R.drawable.amber;
	    else
		return R.drawable.green;
	} else {
	    if (date.openSoon(l))
		return R.drawable.dark;
	    else
		return R.drawable.red;
	}
    }

    public void printEx(Exception e) {
	final TextView t = new TextView(this);
	e.printStackTrace(new PrintStream(new OutputStream() {
	    public void write(int i) throws IOException {
		t.append("" + (char) i);
	    }
	}));
	locations.addView(t);
    }

}
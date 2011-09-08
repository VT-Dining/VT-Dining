package com.vtdining;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormatSymbols;
import java.util.Calendar;

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
	loadCachedDays();
	date.setNow();
	dateDisplay = (TextView) findViewById(R.id.date);
	locations = (LinearLayout) findViewById(R.id.locations);
	h = new Handler() {
	    public void handleMessage(Message m) {
		    setUp();
	    }
	};
	date.refresh();
	load();
	refresh(locations);
    }

    public void refresh(View v) {
	date.refresh();
	load();
    }

    public void pickDate(View v) {
	showDialog(0);

    }

    protected Dialog onCreateDialog(int id) {
	return new DatePickerDialog(this,
		new DatePickerDialog.OnDateSetListener() {
		    public void onDateSet(DatePicker d, int year, int month,
			    int day) {
			date.date.set(Calendar.YEAR, year);
			date.date.set(Calendar.MONTH, month);
			date.date.set(Calendar.DATE, day);
			date.decDay();
			incDay(locations);
		    }
		}, date.date.get(Calendar.YEAR), date.date.get(Calendar.MONTH),
		date.date.get(Calendar.DATE));

    }

    public void loadCachedDays() {
	ObjectInputStream in = null;
	try {
	    in = new ObjectInputStream(openFileInput("cachedDays"));
	    date = (Days) in.readObject();
	    in.close();
	} catch (Exception e) {
	    if (in != null)
		try {
		    in.close();
		} catch (IOException e1) {
		}
		date=new Days();
	}
    }

    public void onStop() {
	ObjectOutputStream out = null;
	try {
	    out = new ObjectOutputStream(openFileOutput("cachedDays",
		    MODE_PRIVATE));
	    out.writeObject(date);
	    out.flush();
	    out.close();
	} catch (Exception e) {
	    try {
		if (out != null)
		    out.close();
	    } catch (IOException e1) {
	    }
	}
	super.onStop();
    }

    public void load() {
	if (date.requiresLoading()) {
	    dateDisplay.setText("Loading...");
	}
	new Thread() {
	    public void run() {
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
		String times = l.getTimes();
		String name = l.getName();
		if (times.contains("no operation hours")) {
		    times = "";
		    name = "No Locations Open Today";
		}
		((TextView) location.getChildAt(0)).setText(name);
		TextView time = (TextView) location.getChildAt(1);
		time.setText(times);
		time.setHeight(0);
		((ImageView) location.getChildAt(2))
			.setImageResource(getColorID(l));
		locations.addView(location);
	    }
	} catch (Exception e) {
	}

    }

    private String getMonth(int month) {
	return "" + new DateFormatSymbols().getMonths()[month - 1];
    }

    public void expandItem(View v) {
	TextView time = (TextView) ((ViewGroup) v).getChildAt(1);
	if (time.getHeight() == 0)
	    time.setHeight(time.getLineHeight() * time.getLineCount() + 15);
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
}
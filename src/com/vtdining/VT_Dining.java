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
import java.util.Observable;
import java.util.Observer;

// -------------------------------------------------------------------------
/**
 * VT Dining main activity
 *
 * @author John
 * @version September 8, 2011
 */
public class VT_Dining extends Activity {
    /** Called when the activity is first created. */
    private Handler h;
    private Days date;
    private LinearLayout locations;
    private TextView dateDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	loadCachedDays();
	h = new Handler() {
	    public void handleMessage(Message m) {
		setUp();
	    }
	};
	date.setNow();
	dateDisplay = (TextView) findViewById(R.id.date);
	locations = (LinearLayout) findViewById(R.id.locations);
	date.startUp();
	date.load();
	date.addObserver(new DaysObserver());
	refresh(locations);
    }

    // ----------------------------------------------------------
    /**
     * Marks that the current date needs a refresh, then load()s
     *
     * @param v
     */
    public void refresh(View v) {
	loading();
	date.refresh();
	date.load();
    }

    /**
     * Shows the date picker dialog
     *
     * @param v
     *            unused, can be null
     */
    public void pickDate(View v) {
	showDialog(0);
    }

    /**
     * Refresh the GUI and startup a refresh thread
     */
    public void onResume() {
	loading();
	date.load();
	date.startUp();
	super.onResume();
    }

    /**
     * Kill the update thread
     */
    public void onPause() {
	date.stop();
	super.onPause();
    }

    /**
     * Open up a date picker
     */
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

    /**
     * Loads cached data if available, otherwise creates new database.
     */
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
		    // suppress
		}
	    date = new Days();
	}
    }

    /**
     * Writes database.
     */
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
		// suppress
	    }
	}
	super.onStop();
    }

    /**
     * Changes dateDisplay text to "loading"
     */
    public void loading() {
	if(date.requiresLoading())
	dateDisplay.setText("Loading...");
    }

    /**
     * Builds display based on loaded data.
     */
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
	    // suppress
	}

    }

    private String getMonth(int month) {
	return "" + new DateFormatSymbols().getMonths()[month - 1];
    }

    /**
     * Expands passed view.
     *
     * @param v
     *            View to be expanded
     */
    public void expandItem(View v) {
	TextView time = (TextView) ((ViewGroup) v).getChildAt(1);
	if (time.getHeight() == 0)
	    time.setHeight(time.getLineHeight() * time.getLineCount() + 15);
	else
	    time.setHeight(0);
    }

    /**
     * Increments the date, loads data for that date
     *
     * @param v
     *            unused, can be null
     */
    public void incDay(View v) {
	loading();
	date.incDay();
    }

    /**
     * Decrements the date, loads data for that date
     *
     * @param v
     *            unused, can be null
     */
    public void decDay(View v) {
	loading();
	date.decDay();
    }

    /**
     * Return resource ID based on the state of the location.
     */
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

    private class DaysObserver implements Observer {

	public void update(Observable unused, Object o) {
	    h.sendEmptyMessage(0);
	}
    }
}
package com.vtdining;

import java.util.Observable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

/**
 * // -------------------------------------------------------------------------
 * /** Holds day objects. Maintains cached days, and loads new days as
 * necessary. Only allows selected day to be viewed
 *
 * @author john
 * @version September 6, 2011
 */
public class Days extends Observable implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<String, Day> days = new HashMap<String, Day>();
    private boolean running = true;
    /**
     * The current day.
     */
    public Calendar date;

    // ----------------------------------------------------------
    /**
     * Create a new Days object.
     */
    public Days() {
	date = Calendar.getInstance();
    }

    /**
     * Starts up a thread that notifies observers every minute.
     */
    public void startUp() {
	running = true;
	new Thread() { // refresh list every minute.
	    public void run() {
		while (running) {
		    setChanged();
		    notifyObservers();
		    try {
			Thread.sleep(60000);
		    } catch (Exception e) {
		    }
		}
	    }
	}.start();
    }

    // ----------------------------------------------------------
    /**
     * Sets the calendar to the current date
     */
    public void setNow() {
	Calendar date2 = Calendar.getInstance();
	date.set(Calendar.YEAR, date2.get(Calendar.YEAR));
	date.set(Calendar.DATE, date2.get(Calendar.DATE));
	date.set(Calendar.MONTH, date2.get(Calendar.MONTH));
    }

    // ----------------------------------------------------------
    /**
     * Returns the Day object associated with the current date
     *
     * @return Day object associated with the current date
     */
    public Day getDay() {
	return days.get(date.toString());
    }

    /**
     * Marks the current date as needing reloading
     */
    public void refresh() {
	days.remove(date.toString());
    }

    // ----------------------------------------------------------
    /**
     * Returns a Vector of Location objects associated with the current date
     *
     * @return Vector of Location objects associated with the current date
     */
    public Vector<Location> getLocations() {
	return days.get(date.toString()).getLocations();
    }

    // ----------------------------------------------------------
    /**
     * Checks if the location is open at the current time
     *
     * @param l
     *            Location to check
     * @return whether or not passed location is open
     */
    public boolean open(Location l) {
	return l.open(getTime());
    }

    /**
     * Increments the current date
     */
    public void incDay() {
	date.add(Calendar.DAY_OF_YEAR, 1);
	load();
    }

    /**
     * Decrements the current date
     */
    public void decDay() {
	date.add(Calendar.DAY_OF_YEAR, -1);
	load();
    }

    /**
     * Checks whether or not the passed location is close to closing.
     *
     * @param l
     *            Location to check
     * @return whether or not passed location is close to closing
     */
    public boolean hurry(Location l) {
	return l.hurry(getTime(), 30);
    }

    /**
     * Checks whether or not the passed location is close to opening
     *
     * @param l
     *            Location to check
     * @return whether or not passed location is close to opening
     */
    public boolean openSoon(Location l) {
	return l.openSoon(getTime(), 30);
    }

    private int getTime() {
	int t = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	if (t == 0)
	    t = 24;
	t *= 60;
	t += Calendar.getInstance().get(Calendar.MINUTE);
	return t;
    }

    /**
     * This method will update the times, then notify observers of changes.
     *
     */
    public void load() {
	new Thread() {
	    public void run() {
		if (days.size() == 0 || !days.containsKey(date.toString()))
		    try {
			days.put(date.toString(), new Day(date
				.get(Calendar.DATE),
				date.get(Calendar.MONTH) + 1, date
					.get(Calendar.YEAR)));
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		setChanged();
		notifyObservers();
	    }
	}.start();
    }

    /**
     * Stops the updating thread.
     */
    public void stop() {
	running = false;
    }
    /**
     * Determines whether or not the current date requires loading
     * @return whether or not the current date requires loading
     */
    public boolean requiresLoading() {
	return !days.containsKey(date.toString());
    }

}

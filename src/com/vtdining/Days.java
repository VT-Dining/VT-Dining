package com.vtdining;

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
 * @version Sep 6, 2011
 */
public class Days implements Serializable {
    private static final long serialVersionUID = 1L;
    public HashMap<String, Day> days = new HashMap<String, Day>();
    public Calendar date;

    public Days() {

    }
    public void setNow() {
	Calendar date2 = Calendar.getInstance();
	date.set(Calendar.YEAR, date2.get(Calendar.YEAR));
	date.set(Calendar.DATE, date2.get(Calendar.DATE));
	date.set(Calendar.MONTH, date2.get(Calendar.MONTH));
    }

    public Day getDay() {
	return days.get(date.toString());
    }
    public void refresh() {
	days.remove(date.toString());
    }

    public Vector<Location> getLocations() {
	return days.get(date.toString()).getLocations();
    }

    public boolean open(Location l) {
	return l.open(getTime());
    }

    public void incDay() {
	date.add(Calendar.DAY_OF_YEAR, 1);
    }

    public void decDay() {
	date.add(Calendar.DAY_OF_YEAR,-1);
    }

    public boolean hurry(Location l) {
	return l.hurry(getTime(), 30);
    }

    public boolean openSoon(Location l) {
	return l.openSoon(getTime(), 30);
    }

    public int getTime() {
	int t = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	if(t==0)
	    t=24;
	t*= 60;
	t += Calendar.getInstance().get(Calendar.MINUTE);
	return t;
    }

    /**
     * CALL THIS METHOD FROM A SEPARATE THREAD (if in GUI) This method may load
     * data from a network if data is not available locally.
     *
     * The date data is requested for
     */
    public void load() throws Exception {
	if (days.size()==0||!days.containsKey(date.toString()))
	    days.put(date.toString(), new Day(date.get(Calendar.DATE), date
		    .get(Calendar.MONTH) + 1, date.get(Calendar.YEAR)));
    }

    public boolean requiresLoading() {
	return !days.containsKey(date.toString());
    }

}

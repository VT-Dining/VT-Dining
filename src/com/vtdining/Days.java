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
 * @version September 6, 2011
 */
public class Days
    implements Serializable
{
    private static final long    serialVersionUID = 1L;
    private HashMap<String, Day> days             = new HashMap<String, Day>();
    /**
     * The current day.
     */
    public Calendar              date;


    // ----------------------------------------------------------
    /**
     * Create a new Days object.
     */
    public Days()
    {
        date = Calendar.getInstance();
    }


    // ----------------------------------------------------------
    /**
     * Sets the calendar to the current date
     */
    public void setNow()
    {
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
    public Day getDay()
    {
        return days.get(date.toString());
    }


    /**
     * Marks the current date as needing reloading
     */
    public void refresh()
    {
        days.remove(date.toString());
    }


    // ----------------------------------------------------------
    /**
     * Returns a Vector of Location objects associated with the current date
     *
     * @return Vector of Location objects associated with the current date
     */
    public Vector<Location> getLocations()
    {
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
    public boolean open(Location l)
    {
        return l.open(getTime());
    }


    /**
     * Increments the current date
     */
    public void incDay()
    {
        date.add(Calendar.DAY_OF_YEAR, 1);
    }


    /**
     * Decrements the current date
     */
    public void decDay()
    {
        date.add(Calendar.DAY_OF_YEAR, -1);
    }


    /**
     * Checks whether or not the passed location is close to closing.
     *
     * @param l
     *            Location to check
     * @return whether or not passed location is close to closing
     */
    public boolean hurry(Location l)
    {
        return l.hurry(getTime(), 30);
    }


    /**
     * Checks whether or not the passed location is close to opening
     *
     * @param l
     *            Location to check
     * @return whether or not passed location is close to opening
     */
    public boolean openSoon(Location l)
    {
        return l.openSoon(getTime(), 30);
    }


    private int getTime()
    {
        int t = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (t == 0)
            t = 24;
        t *= 60;
        t += Calendar.getInstance().get(Calendar.MINUTE);
        return t;
    }


    /**
     * CALL THIS METHOD FROM A SEPARATE THREAD (if in GUI) This method will try
     * to load data from a network if data is not available locally.
     *
     * @exception Exception occurs if data fails to download
     */
    public void load()
        throws Exception
    {
        if (days.size() == 0 || !days.containsKey(date.toString()))
            days.put(
                date.toString(),
                new Day(
                    date.get(Calendar.DATE),
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.YEAR)));
    }


    // ----------------------------------------------------------
    /**
     * Checks if the database lacks an entry at the current date
     * @return whether or not the database needs to load data
     */
    public boolean requiresLoading()
    {
        return !days.containsKey(date.toString());
    }

}

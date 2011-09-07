package com.vtdining;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Observable;

/**
 * // -------------------------------------------------------------------------
/**
 *  Holds day objects. Maintains cached days, and loads new days as necessary.
 *  Only allows selected day to be viewed
 *
 *  @author john
 *  @version Sep 6, 2011
 */
public class Days extends Observable{
    private HashMap<Calendar,Day> days=new HashMap<Calendar,Day>();
    private Calendar date;
    public Days() {
	date=Calendar.getInstance();

    }
    public Day getDay(Calendar cal) {
	return days.get(cal);
    }

}

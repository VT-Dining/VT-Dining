package com.vtdining;

import java.io.Serializable;
import java.util.Vector;

public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    private String location;
    private Vector<String> hours = new Vector<String>();
    private Vector<Range> ranges = new Vector<Range>();
    private Vector <String> reducedHours=new Vector<String>();//Just the times.
    public Location(String location, Vector<String> hours) {
	this.hours = hours;
	this.location = location;
	for (String s : hours) {
	    int index1 = s.indexOf(":") - 2;
	    int index2 = s.indexOf("-");
	    int index3 = s.lastIndexOf(":") + 6;
	    reducedHours.add(s.substring(index1));
	    String time1 = s.substring(index1, index2);
	    String time2 = s.substring(index2 + 1, index3);
	    time1 = time1.replace(" ", "");
	    time2 = time2.replace(" ", "");
	    String hour1 = time1.substring(0, time1.indexOf(":"));
	    String hour2 = time2.substring(0, time2.indexOf(":"));
	    String minute1 = time1.substring(time1.indexOf(":") + 1);
	    String minute2 = time2.substring(time2.indexOf(":") + 1);
	    minute1 = minute1.substring(0, 2);
	    minute2 = minute2.substring(0, 2);
	    int h1 = Integer.parseInt(hour1) * 60
		    + (time1.contains("pm") ? 12 * 60 : 0);
	    int h2 = Integer.parseInt(hour2) * 60
		    + (time2.contains("pm") ? 12 * 60 : 0);
	    if (h1 == 720)
		h1 = 0;
	    if (h2 == 720)
		h2 = 0;
	    int t1 = h1 + Integer.parseInt(minute1);
	    int t2 = h2 + Integer.parseInt(minute2);
	    if (t2 < t1)
		t2 += 24 * 60;
	    System.out.println(location + "   " + t1 + " - " + t2);
	    ranges.add(new Range(t1, t2));
	}
    }

    public boolean open(int t) {
	for (Range r : ranges)
	    if (r.within(t))
		return true;
	return false;
    }

    public String getName() {
	return location;
    }

    public String getTimes() {
	String times = "";
	for (String s : hours)
	    times += s + "\n";
	return times;
    }

    public Vector<String> getHours() {
	return hours;
    }

    public boolean hurry(int t, int thresh) {
	for (Range r : ranges)
	    if (r.t2-t < thresh && r.within(t))
		return true;
	return false;
    }

    public String toString() {
	String open = "";
	for (String s : hours)
	    open += "\n    " + s;
	return location + open;
    }

    public boolean openSoon(int t, int thresh) {
	if(open(t))
	    return false;
	for (Range r : ranges)
	    if (Math.abs(t-r.t1) < thresh)
		return true;
	return false;
    }
}

package com.vtdining;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class Day{
    private int day,month,year;
    private Vector<Location> locations=new Vector<Location>();
    public Day(int day, int month, int year) throws Exception{
	    String data = getData(day,month,year);
	    this.day=getDay(data);
	    this.month=getMonth(data);
	    this.year=getYear(data);
	    locations=getLocations(data);
    }
    public int getDay() {return day;}
    public int getMonth() {return month;}
    public int getYear() {return year;}
    public Vector<Location> getLocations(){return locations;}
    private static String getData(int day, int month, int year) throws Exception {
	String data = "d_day=" + day + "&d_month=" + month + "&d_year=" + year
		+ "&view=View+Date";
	URL url = new URL(
		"https://secure.hosting.vt.edu/www.dining.vt.edu/hours/index.php?d=t");
	URLConnection urlc = url.openConnection();
	urlc.setDoOutput(true);
	OutputStream out = urlc.getOutputStream();
	out.write(data.getBytes());
	out.flush();
	InputStream i = urlc.getInputStream();
	byte[] b = new byte[10000];
	i.read(b);
	i.close();
	out.close();
	return new String(b);
    }

    private Vector<Location> getLocations(String s) {
	Vector<Location> hours = new Vector<Location>();
	String sub = s.substring(s.indexOf("<ul>") + 4, s.indexOf("<table>"));
	String location;
	for (;;) {
	    try {
		location = sub.substring(sub.indexOf("<li>") + 4, sub
			.indexOf("<ul>"));
	    } catch (Exception e) {
		break;
	    }

	    location = location.replaceAll("^[ \t]+", "");
	    location = location.replaceAll("[ \t]+$", "");
	    String open = sub.substring(sub.indexOf("<ul>") + 4, sub
		    .indexOf("</ul>"));
	    //SPECIAL CASE COMPENSATING FOR ERROR IN VT DATABASE
	    if(location.contains("DXpress"))
		open=open.replace("pm","am");

	    for (char i = 0; i < 20; i++)
		open = open.replace(i, ' ');
	    open = open.replace("   ", " ");
	    open = open.replaceAll("^[ \t]+", "");
	    open = open.replaceAll("[ \t]+$", "");
	    sub = sub.substring(sub.indexOf("</ul>") + 9);
	    Vector<String> openHours = new Vector<String>();

	    for (;;) {
		try {
		    openHours.add(open.substring(open.indexOf("<li>") + 4, open
			    .indexOf("</li>")));
		    open = open.substring(open.indexOf("</li>") + 5);
		} catch (Exception e) {
		    break;
		}
	    }
	    location=location.replace("amp;","");
	    hours.add(new Location(location, openHours));
	}
	return hours;
    }

    /**
     * Parses HTML data from dining.vt.edu to determine the selected month
     *
     * @param data
     *            HTML data from dining.vt.edu
     */
    private int getMonth(String data) throws Exception{
	    String sub = data.substring(data.indexOf("t_v_d_month"), data
		    .indexOf("t_v_d_day"));
	    sub = sub.substring(sub.indexOf("\"selected\" value=\""));
	    return Integer.parseInt(sub.substring(18, sub.indexOf("\">")));
    }

    /**
     * Parses HTML data from dining.vt.edu to determine the selected day
     *
     * @param data
     *            HTML data from dining.vt.edu
     */
    private int getDay(String data) throws Exception{
	    String sub = data.substring(data.indexOf("t_v_d_day"), data
		    .indexOf("t_v_d_year"));
	    sub = sub.substring(sub.indexOf("\"selected\" value=\""));
	    return Integer.parseInt(sub.substring(18, sub.indexOf("\">")));
    }

    /**
     * Parses HTML data from dining.vt.edu to determine the selected year
     *
     * @param data
     *            HTML data from dining.vt.edu
     */
    public static int getYear(String data) throws Exception{
	    String sub = data.substring(data.indexOf("t_v_d_year"), data
		    .indexOf("<input type="));
	    sub = sub.substring(sub.indexOf("\"selected\" value=\""));
	    return Integer.parseInt(sub.substring(18, sub.indexOf("\">")));
    }
}
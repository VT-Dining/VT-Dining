package com.vtdining;

import java.io.Serializable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

/**
 * // -------------------------------------------------------------------------
 * /** Represents a single day, containing all dining locations and hours.
 *
 * @author John
 * @version September 8, 2011
 */
public class Day
    implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int               day, month, year;
    private Vector<Location>  locations        = new Vector<Location>();


    // ----------------------------------------------------------
    /**
     * Create a new Day object, loading data from online database.
     *
     * @param day
     * @param month
     * @param year
     * @throws Exception
     */
    public Day(int day, int month, int year)
        throws Exception
    {
        load(day, month, year);
    }



    // ----------------------------------------------------------
    /**
     * Loads and parses data from dining.vt
     * @param d day
     * @param m month
     * @param y year
     * @throws Exception
     */
    public void load(int d, int m, int y)
        throws Exception
    {
        String data = getData(d, m, y);
        this.day = getDay(data);
        this.month = getMonth(data);
        this.year = getYear(data);
        locations = getLocations(data);
    }


    // ----------------------------------------------------------
    /**
     * Returns the parsed day, or -1 if the requested date was not available
     * @return day
     */
    public int getDay()
    {
        return day;
    }

    // ----------------------------------------------------------
    /**
     * Returns the parsed month, or -1 if the requested date was not available
     * @return month
     */
    public int getMonth()
    {
        return month;
    }

    // ----------------------------------------------------------
    /**
     * Returns the parsed year, or -1 if the requested date was not available
     * @return year
     */
    public int getYear()
    {
        return year;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list of all available locations on the loaded date
     * @return locations
     */
    public Vector<Location> getLocations()
    {
        return locations;
    }


    private static String getData(int day, int month, int year)
        throws Exception
    {
        String data =
            "d_day=" + day + "&d_month=" + month + "&d_year=" + year
                + "&view=View+Date";
        URL url =
            new URL(
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


    private Vector<Location> getLocations(String s)
    {
        Vector<Location> hours = new Vector<Location>();
        String sub = s.substring(s.indexOf("<ul>") + 4, s.indexOf("<table>"));
        String location;
        for (;;)
        {
            try
            {
                location =
                    sub.substring(sub.indexOf("<li>") + 4, sub.indexOf("<ul>"));
            }
            catch (Exception e)
            {
                break;
            }

            location = location.replaceAll("^[ \t]+", "");
            location = location.replaceAll("[ \t]+$", "");
            String open =
                sub.substring(sub.indexOf("<ul>") + 4, sub.indexOf("</ul>"));
            // SPECIAL CASE COMPENSATING FOR ERROR IN VT DATABASE
            if (location.contains("DXpress"))
                open = open.replace("pm", "am");

            for (char i = 0; i < 20; i++)
                open = open.replace(i, ' ');
            open = open.replace("   ", " ");
            open = open.replaceAll("^[ \t]+", "");
            open = open.replaceAll("[ \t]+$", "");
            sub = sub.substring(sub.indexOf("</ul>") + 9);
            Vector<String> openHours = new Vector<String>();

            for (;;)
            {
                try
                {
                    openHours.add(open.substring(
                        open.indexOf("<li>") + 4,
                        open.indexOf("</li>")));
                    open = open.substring(open.indexOf("</li>") + 5);
                }
                catch (Exception e)
                {
                    break;
                }
            }
            location = location.replace("amp;", "");
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
    private int getMonth(String data)
        throws Exception
    {
        String sub =
            data.substring(
                data.indexOf("t_v_d_month"),
                data.indexOf("t_v_d_day"));
        sub = sub.substring(sub.indexOf("\"selected\" value=\""));
        return Integer.parseInt(sub.substring(18, sub.indexOf("\">")));
    }


    /**
     * Parses HTML data from dining.vt.edu to determine the selected day
     *
     * @param data
     *            HTML data from dining.vt.edu
     */
    private int getDay(String data)
        throws Exception
    {
        String sub =
            data.substring(
                data.indexOf("t_v_d_day"),
                data.indexOf("t_v_d_year"));
        sub = sub.substring(sub.indexOf("\"selected\" value=\""));
        return Integer.parseInt(sub.substring(18, sub.indexOf("\">")));
    }


    /**
     * Parses HTML data from dining.vt.edu to determine the selected year
     *
     * @param data
     *            HTML data from dining.vt.edu
     * @return parses the year from loaded HTML data
     * @throws Exception if year not found.
     */
    public static int getYear(String data)
        throws Exception
    {
        String sub =
            data.substring(
                data.indexOf("t_v_d_year"),
                data.indexOf("<input type="));
        sub = sub.substring(sub.indexOf("\"selected\" value=\""));
        return Integer.parseInt(sub.substring(18, sub.indexOf("\">")));
    }
}

package com.vtdining;

import java.io.Serializable;

// -------------------------------------------------------------------------
/**
 *  A range of two times.
 *
 *  @author John
 *  @version September 8, 2011
 */
public class Range implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * first time
     */
    public int t1;
    /**
     * second time
     */
    public int t2;
    // ----------------------------------------------------------
    /**
     * Create a new Range object.
     * @param t1 first time
     * @param t2 second time
     */
    public Range(int t1,int t2) {
	this.t1=t1;
	this.t2=t2;
    }
    // ----------------------------------------------------------
    /**
     * Checks if passed time is within this range
     * @param t time to check
     * @return whether or not passed time is within range
     */
    public boolean within(int t) {
	return t>t1 && t<t2;
    }
}

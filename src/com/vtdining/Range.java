package com.vtdining;

import java.io.Serializable;

public class Range implements Serializable{
    private static final long serialVersionUID = 1L;
    public int t1, t2;
    public Range(int t1,int t2) {
	this.t1=t1;
	this.t2=t2;
    }
    public boolean within(int t) {
	return t>t1 && t<t2;
    }
}

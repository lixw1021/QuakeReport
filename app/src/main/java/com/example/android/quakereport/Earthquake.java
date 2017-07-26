package com.example.android.quakereport;

import java.net.URL;

/**
 * Created by xianwei li on 7/21/2017.
 */

public class Earthquake {
    private Double mag;
    private String location;
    private Long time;
    private String url;
    public Earthquake(double mag, String location, long time, String url) {
        this.mag = mag;
        this.location =location;
        this.time = time;
        this.url = url;
    }

    public double getMag() {
        return mag;
    }

    public String getLocation() {
        return location;
    }

    public long getTimeInMilliseconds(){
        return time;
    }

    public String getUrl() {
        return url;
    }
}

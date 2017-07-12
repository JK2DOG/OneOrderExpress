package com.zc.express.bean;

/**
 * Created by ZC on 2017/7/11.
 */

public class    Location {

    /**
     * latitude : 31.919187
     * longitude : 117.369341
     */

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

package com.zc.express.bean;

/**
 * Created by ZC on 2017/7/12.
 */

public class WaitOrder {

    /**
     * userId : 24
     * orderId : 3b92e0d7-e557-413c-9f17-4f9b1fae35ad
     * location : {"latitude":10.97,"longitude":78.47}
     * stationId : 4
     * pickupContactId : 235
     */

    private int userId;
    private String orderId;
    private Location location;
    private int stationId;
    private int pickupContactId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getPickupContactId() {
        return pickupContactId;
    }

    public void setPickupContactId(int pickupContactId) {
        this.pickupContactId = pickupContactId;
    }

}

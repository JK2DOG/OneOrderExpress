package com.zc.express.bean;

/**
 * Created by ZC on 2017/7/24.
 */

public class PushOrder {


    /**
     * userId : 24
     * orderId : b9c0c013-7972-4462-af67-2b42d1101873
     * location : {"latitude":42.440716540480445,"longitude":-71.02282647981403}
     * stationId : 4
     * pickupContactId : 243
     */

    private int userId;
    private String orderId;
    private LocationBean location;
    private int stationId;
    private int pickupContactId;

    public PushOrder(String orderId){
        this.orderId=orderId;
    }

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

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
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

    public static class LocationBean {
        /**
         * latitude : 42.440716540480445
         * longitude : -71.02282647981403
         */

        private double latitude;
        private double longitude;

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
}

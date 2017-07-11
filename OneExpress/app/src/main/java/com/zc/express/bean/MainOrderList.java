package com.zc.express.bean;

/**
 * Created by ZC on 2017/7/11.
 */

public class MainOrderList {

    /**
     * order_id : 01aef6b2-dcbb-4ef8-8f89-13eba60339cf
     * order_date : 2017-04-11T01:57:00.000+0000
     * accept_decision : true
     * accept_result : true
     * user_id : 24
     * station_id : 4
     * final_price : 108.0
     * currency : CNY
     * order_status : canceled
     */

    private String order_id;
    private String order_date;
    private boolean accept_decision;
    private boolean accept_result;
    private int user_id;
    private int station_id;
    private double final_price;
    private String currency;
    private String order_status;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public boolean isAccept_decision() {
        return accept_decision;
    }

    public void setAccept_decision(boolean accept_decision) {
        this.accept_decision = accept_decision;
    }

    public boolean isAccept_result() {
        return accept_result;
    }

    public void setAccept_result(boolean accept_result) {
        this.accept_result = accept_result;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public double getFinal_price() {
        return final_price;
    }

    public void setFinal_price(double final_price) {
        this.final_price = final_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}

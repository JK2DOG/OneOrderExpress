package com.zc.express.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZC on 2017/6/28.
 */

public class Order implements Parcelable {


    /**
     * id : f6832ff5-94f1-49e1-99f8-5801d4e4df08
     * user_id : 24
     * create_time : 2017-06-23T04:21:49.000+0000
     * departure : 上海市
     * destination : United States West
     * commodity_item_name : 普货
     * status : created
     * error_message : null
     * estimate_price : 66.2
     * final_price : 66.2
     * is_paid : false
     * payment_method : null
     * price_unit : CNY
     * latitude : 42.44053926050518
     * longitude : -71.02291913606228
     * sender : {"address_id":243,"user_id":24,"name":null,"contact_person":"Bin","street_name_number":"Skdkdkdkdkdkdk","apt_suite_number":null,"street_en":"Rjfjdjdkdkdkdkdkd","apt_en":null,"city_town":"上海市","state_province":"上海市","zip_postal_code":"3100000","country":"中国","contact_phone":"153848483838","mobile_phone":null,"email":null,"is_sender":true,"is_active":true,"create_date":"2017-05-16T23:39:26.000+0000","company":"Ingenico","city_en":"Shanghai City","province_en":"Shanghai City","country_en":"China"}
     * need_pickup : false
     * pickup_contact : {"address_id":243,"user_id":24,"name":null,"contact_person":"Bin","street_name_number":"Skdkdkdkdkdkdk","apt_suite_number":null,"street_en":"Rjfjdjdkdkdkdkdkd","apt_en":null,"city_town":"上海市","state_province":"上海市","zip_postal_code":"3100000","country":"中国","contact_phone":"153848483838","mobile_phone":null,"email":null,"is_sender":true,"is_active":true,"create_date":"2017-05-16T23:39:26.000+0000","company":"Ingenico","city_en":"Shanghai City","province_en":"Shanghai City","country_en":"China"}
     * transport_company : null
     * transport_tracking : null
     * comment : null
     * eship_service_id : 19
     * eship_service_name : Fedex-Test
     * station_id : 13
     * carrier : Fedex
     * active : true
     * packages : [{"id":902,"order_id":"f6832ff5-94f1-49e1-99f8-5801d4e4df08","description":"doc","height":10,"width":10,"length":10,"dimension_unit":"CM","weight":0.5,"weight_unit":"KG","custom_number":null,"value":100,"currency_unit":"CNY","recipient":{"address_id":242,"user_id":24,"name":null,"contact_person":"Bin lang","street_name_number":null,"apt_suite_number":null,"street_en":"101 federal st","apt_en":null,"city_town":"Boston","state_province":"MA","zip_postal_code":"02210","country":"美国东部","contact_phone":"6174700894","mobile_phone":null,"email":null,"is_sender":false,"is_active":true,"create_date":"2017-05-14T23:35:19.000+0000","company":"Ingenico"},"status":"created","tracking_num":null,"active":true,"doc":false}]
     */

    private String id;
    private int user_id;
    private String create_time;
    private String departure;//起点
    private String destination;//终点
    private String commodity_item_name;
    private String status;
    private String error_message;
    private double estimate_price;//估价
    private double final_price;//最终价格
    private boolean is_paid;
    private String payment_method;
    private String price_unit;
    private double latitude;
    private double longitude;
    private Person sender;
    private boolean need_pickup;
    private Person pickup_contact;
    private String transport_company;
    private String transport_tracking;
    private String comment;
    private int eship_service_id;
    private String eship_service_name;
    private int station_id;
    private String carrier;
    private boolean active;
    private List<Package> packages;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCommodity_item_name() {
        return commodity_item_name;
    }

    public void setCommodity_item_name(String commodity_item_name) {
        this.commodity_item_name = commodity_item_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public double getEstimate_price() {
        return estimate_price;
    }

    public void setEstimate_price(double estimate_price) {
        this.estimate_price = estimate_price;
    }

    public double getFinal_price() {
        return final_price;
    }

    public void setFinal_price(double final_price) {
        this.final_price = final_price;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
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

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public boolean isNeed_pickup() {
        return need_pickup;
    }

    public void setNeed_pickup(boolean need_pickup) {
        this.need_pickup = need_pickup;
    }

    public Person getPickup_contact() {
        return pickup_contact;
    }

    public void setPickup_contact(Person pickup_contact) {
        this.pickup_contact = pickup_contact;
    }

    public String getTransport_company() {
        return transport_company;
    }

    public void setTransport_company(String transport_company) {
        this.transport_company = transport_company;
    }

    public String getTransport_tracking() {
        return transport_tracking;
    }

    public void setTransport_tracking(String transport_tracking) {
        this.transport_tracking = transport_tracking;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getEship_service_id() {
        return eship_service_id;
    }

    public void setEship_service_id(int eship_service_id) {
        this.eship_service_id = eship_service_id;
    }

    public String getEship_service_name() {
        return eship_service_name;
    }

    public void setEship_service_name(String eship_service_name) {
        this.eship_service_name = eship_service_name;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.user_id);
        dest.writeString(this.create_time);
        dest.writeString(this.departure);
        dest.writeString(this.destination);
        dest.writeString(this.commodity_item_name);
        dest.writeString(this.status);
        dest.writeString(this.error_message);
        dest.writeDouble(this.estimate_price);
        dest.writeDouble(this.final_price);
        dest.writeByte(this.is_paid ? (byte) 1 : (byte) 0);
        dest.writeString(this.payment_method);
        dest.writeString(this.price_unit);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeParcelable(this.sender, flags);
        dest.writeByte(this.need_pickup ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.pickup_contact, flags);
        dest.writeString(this.transport_company);
        dest.writeString(this.transport_tracking);
        dest.writeString(this.comment);
        dest.writeInt(this.eship_service_id);
        dest.writeString(this.eship_service_name);
        dest.writeInt(this.station_id);
        dest.writeString(this.carrier);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeList(this.packages);
    }

    public Order() {
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.user_id = in.readInt();
        this.create_time = in.readString();
        this.departure = in.readString();
        this.destination = in.readString();
        this.commodity_item_name = in.readString();
        this.status = in.readString();
        this.error_message = in.readString();
        this.estimate_price = in.readDouble();
        this.final_price = in.readDouble();
        this.is_paid = in.readByte() != 0;
        this.payment_method = in.readString();
        this.price_unit = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.sender = in.readParcelable(Person.class.getClassLoader());
        this.need_pickup = in.readByte() != 0;
        this.pickup_contact = in.readParcelable(Person.class.getClassLoader());
        this.transport_company = in.readString();
        this.transport_tracking = in.readString();
        this.comment = in.readString();
        this.eship_service_id = in.readInt();
        this.eship_service_name = in.readString();
        this.station_id = in.readInt();
        this.carrier = in.readString();
        this.active = in.readByte() != 0;
        this.packages = new ArrayList<Package>();
        in.readList(this.packages, Package.class.getClassLoader());
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}

package com.zc.express.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZC on 2017/6/28.
 */

public class Package implements Parcelable {

    private int id;
    private String order_id;
    private String description;
    private int height;
    private int width;
    private int length;
    private String dimension_unit;
    private double weight;
    private String weight_unit;
    private String custom_number;
    private int value;
    private String currency_unit;
    private Person recipient;
    private String status;
    private String tracking_num;
    private boolean active;
    private boolean doc;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getDimension_unit() {
        return dimension_unit;
    }

    public void setDimension_unit(String dimension_unit) {
        this.dimension_unit = dimension_unit;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getCustom_number() {
        return custom_number;
    }

    public void setCustom_number(String custom_number) {
        this.custom_number = custom_number;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrency_unit() {
        return currency_unit;
    }

    public void setCurrency_unit(String currency_unit) {
        this.currency_unit = currency_unit;
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTracking_num() {
        return tracking_num;
    }

    public void setTracking_num(String tracking_num) {
        this.tracking_num = tracking_num;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDoc() {
        return doc;
    }

    public void setDoc(boolean doc) {
        this.doc = doc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.order_id);
        dest.writeString(this.description);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeInt(this.length);
        dest.writeString(this.dimension_unit);
        dest.writeDouble(this.weight);
        dest.writeString(this.weight_unit);
        dest.writeString(this.custom_number);
        dest.writeInt(this.value);
        dest.writeString(this.currency_unit);
        dest.writeParcelable(this.recipient, flags);
        dest.writeString(this.status);
        dest.writeString(this.tracking_num);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeByte(this.doc ? (byte) 1 : (byte) 0);
    }

    public Package() {
    }

    protected Package(Parcel in) {
        this.id = in.readInt();
        this.order_id = in.readString();
        this.description = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
        this.length = in.readInt();
        this.dimension_unit = in.readString();
        this.weight = in.readDouble();
        this.weight_unit = in.readString();
        this.custom_number = in.readString();
        this.value = in.readInt();
        this.currency_unit = in.readString();
        this.recipient = in.readParcelable(Person.class.getClassLoader());
        this.status = in.readString();
        this.tracking_num = in.readString();
        this.active = in.readByte() != 0;
        this.doc = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Package> CREATOR = new Parcelable.Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel source) {
            return new Package(source);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
}

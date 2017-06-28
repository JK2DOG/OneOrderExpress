package com.zc.express.bean;

/**
 * Created by ZC on 2017/6/28.
 */

public class Package {

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
}

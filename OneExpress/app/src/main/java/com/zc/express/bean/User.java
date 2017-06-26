package com.zc.express.bean;

/**
 * Created by ZC on 2017/6/23.
 */

public class User {

    /**
     * user_name : string
     * email : string
     * password : string
     * phone : string
     * register_date : 2017-06-23T01:47:38.754Z
     * real_name : string
     * company : string
     * vip : true
     * is_business : true
     * phone_verified : true
     * phone_activated : true
     * id : 0
     */

    private String user_name;
    private String email;
    private String password;
    private String phone;
    private String register_date;
    private String real_name;
    private String company;
    private boolean vip;
    private boolean is_business;
    private boolean phone_verified;
    private boolean phone_activated;
    private int id;


    public User() {
    }

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public boolean isIs_business() {
        return is_business;
    }

    public void setIs_business(boolean is_business) {
        this.is_business = is_business;
    }

    public boolean isPhone_verified() {
        return phone_verified;
    }

    public void setPhone_verified(boolean phone_verified) {
        this.phone_verified = phone_verified;
    }

    public boolean isPhone_activated() {
        return phone_activated;
    }

    public void setPhone_activated(boolean phone_activated) {
        this.phone_activated = phone_activated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package com.zc.express.bean;

/**
 * 验证信息
 */
public class Auth {
    String username;
    String password;

    public Auth() {
    }

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

package com.zc.express.bean;

/**
 * Created by ZC on 2017/6/26.
 */

public class ApiResponse {


    /**
     * code : 0
     * message : string
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

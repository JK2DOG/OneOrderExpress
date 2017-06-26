package com.zc.express.api;

/**
 * Created by ZC on 2017/6/26.
 */

public class ResultException extends Throwable {
    private int code;
    private String msg;

    public  ResultException(int code,String msg){
        this.code=code;
        this.msg=msg;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

package com.zc.express.api;

import rx.functions.Func1;

/**
 * Created by ZC on 2017/6/26.
 */

public class ApiResponseRxFunc1<T extends R, R> implements Func1<T, R> {

    String mMessage;

    /**
     * 当description为空时，设置的信息
     * @param message 字符串
     */
    public ApiResponseRxFunc1(String message) {
        mMessage = message;
    }


    @Override
    public R call(T t) {

        return t;
    }
}

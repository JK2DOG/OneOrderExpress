package com.zc.express.api;

/**
 * Created by Administrator on 2016/12/15 0015.
 */

public class NoDataException extends RuntimeException {
    public NoDataException() {
        super();
    }

    public NoDataException(String detailMessage) {
        super(detailMessage);
    }
}

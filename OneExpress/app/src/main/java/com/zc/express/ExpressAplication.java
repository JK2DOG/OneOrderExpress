package com.zc.express;

import android.app.Application;

import com.zc.express.api.ExpressApiProvider;
import com.zc.express.data.network.OkHttp;

/**
 * Created by ZC on 2017/6/23.
 */

public class ExpressAplication extends Application {

    private static ExpressComponent mExpressComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttp.createCache(getCacheDir());
        ExpressApiProvider.init(OkHttp.client(this));
        _initInjector();
    }

    private void _initInjector() {
        mExpressComponent = DaggerExpressComponent.builder()
                .expressModule(new ExpressModule(this))
                .build();
    }


    public static ExpressComponent getExpressComponent() {
        return mExpressComponent;
    }
}

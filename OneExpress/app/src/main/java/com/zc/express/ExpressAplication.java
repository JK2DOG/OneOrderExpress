package com.zc.express;

import android.app.Application;

import com.zc.express.api.ExpressApiProvider;
import com.zc.express.data.network.OkHttp;
import com.zc.express.utils.ToastUtils;

/**
 * Created by ZC on 2017/6/23.
 */

public class ExpressAplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ExpressModule.init(this);
        OkHttp.createCache(getCacheDir());
        ExpressApiProvider.init(OkHttp.client(this));
        ToastUtils.init(this);
    }



}

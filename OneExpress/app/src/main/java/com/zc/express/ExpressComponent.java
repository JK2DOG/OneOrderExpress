package com.zc.express;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.zc.express.api.ExpressApi;
import com.zc.express.bean.User;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by ZC on 2017/6/23.
 */
@Singleton
@Component(modules = ExpressModule.class)
public interface ExpressComponent {

    Context getContext();

    OkHttpClient getOkHttpClient();

    Picasso getPicasso();

    ExpressApi getExpressApi();

    User getUser();


}

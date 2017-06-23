package com.zc.express.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 *
 * API单例
 */
public class ExpressApiProvider {
    private static ExpressApi mInstance;

    /**
     * 需要OkHttpClient进行初始化
     * @param okHttpClient  客户端所有网络通讯共享的OkHttpClient
     */
    public static void init(OkHttpClient okHttpClient){
        if (null == mInstance) {
            synchronized (ExpressApiProvider.class) {
                if (null == mInstance) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(ExpressApiConfig.SERVER_ADDR + "/")
                            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                            .build();

                    mInstance = retrofit.create(ExpressApi.class);
                }
            }
        }
    }

    /**
     * 获取API实例
     * @return ExpressApi
     */
    public static ExpressApi api() {
        return mInstance;
    }

    private ExpressApiProvider() {
    }
}

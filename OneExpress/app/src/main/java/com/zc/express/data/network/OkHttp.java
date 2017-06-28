package com.zc.express.data.network;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp 单例
 */
public class OkHttp {
    static private final String DEFAULT_CACHE_DIR = "OkHttpCache";
    static private final int DEFAULT_CACHE_SIZE = 8 * 1024 * 1024;
    private static OkHttpClient mInstance;
    private static Cache mCache;
    private static String name = Base64.encodeToString("blang01 ".getBytes(), Base64.DEFAULT).trim();
    private static String pwd = Base64.encodeToString("123456".getBytes(), Base64.DEFAULT).trim();

    //app端你试试blang01
//123456
    public static OkHttpClient client(Context appContext) {
        if (null == mInstance) {
            synchronized (OkHttp.class) {
                if (null == mInstance) {
//                    mInstance = getDefaultBuilder().sslSocketFactory(SslContextFactory.getSslSocket(appContext)).build();
                    mInstance = getDefaultBuilder().addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
//                                    .addHeader("Authorization", "YmxhbmcwMToxMjM0NTY=")
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Accept", "application/json")
                                    .build();
                            return chain.proceed(request);
                        }
                    }).build();
                }
            }
        }
        return mInstance;
    }

    private OkHttp() {
    }

    static public void createCache(File appCacheDir) {
        synchronized (OkHttp.class) {
            if (null == mCache) {
                File cacheDir = new File(appCacheDir, DEFAULT_CACHE_DIR);
                mCache = new Cache(cacheDir, DEFAULT_CACHE_SIZE);
            }
        }
    }

    static public void create(OkHttpClient.Builder builder) {
        synchronized (OkHttp.class) {
            mInstance = builder.build();
        }
    }

    static public OkHttpClient.Builder getDefaultBuilder() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("zc", "OkHttp====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).cache(mCache).addInterceptor(loggingInterceptor);
    }
}

package com.zc.express.data.network;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * OkHttp 单例
 */
public class OkHttp {
    static private final String DEFAULT_CACHE_DIR = "OkHttpCache";
    static private final int DEFAULT_CACHE_SIZE = 8 * 1024 * 1024;
    private static OkHttpClient mInstance;
    private static Cache mCache;

    public static OkHttpClient client(Context appContext) {
        if (null == mInstance) {
            synchronized (OkHttp.class) {
                if (null == mInstance) {
//                    mInstance = getDefaultBuilder().sslSocketFactory(SslContextFactory.getSslSocket(appContext)).build();
                    mInstance = getDefaultBuilder().build();
                }
            }
        }
        return mInstance;
    }

    private OkHttp() {
    }

    static public void createCache(File appCacheDir){
        synchronized (OkHttp.class){
            if (null == mCache){
                File cacheDir = new File(appCacheDir, DEFAULT_CACHE_DIR);
                mCache = new Cache(cacheDir, DEFAULT_CACHE_SIZE);
            }
        }
    }

    static public void create(OkHttpClient.Builder builder){
        synchronized (OkHttp.class){
            mInstance = builder.build();
        }
    }

    static public OkHttpClient.Builder getDefaultBuilder(){
        return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).cache(mCache);
    }
}

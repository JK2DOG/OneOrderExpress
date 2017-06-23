package com.zc.express.data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 *  * picasso + okhttp3
 */
public class PicassoProvider {
//    static private Picasso singleton;

//    static public Picasso get(Context context){
//        if (singleton == null) {
//            synchronized (PicassoProvider.class) {
//                if (singleton == null) {
//                    singleton = builder(context, OkHttp.client()).build();
//                }
//            }
//        }
//        return singleton;
//    }

    static public Picasso.Builder builder(Context context, OkHttpClient okHttpClient){
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient));//.indicatorsEnabled(true)
    }
}

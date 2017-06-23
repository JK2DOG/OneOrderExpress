//package com.zc.express.data.network;
//
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//import rx.schedulers.Schedulers;
//
///**
// * Retrofit2 + gson + okhttp3
// */
//public class RetrofitProvider {
//    static private ConcurrentMap<String, Retrofit> retrofitMap = new ConcurrentHashMap<>();
//
//    static public Retrofit get(String baseUrl) {
//        if (null == retrofitMap.get(baseUrl)) {
//            synchronized (RetrofitProvider.class) {
//                if (null == retrofitMap.get(baseUrl)) {
//                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
//                            .addConverterFactory(GsonConverterFactory.create()).client(OkHttp.client())
//                            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
//                            .build();
//                    retrofitMap.put(baseUrl, retrofit);
//                }
//            }
//        }
//        return retrofitMap.get(baseUrl);
//    }
//}

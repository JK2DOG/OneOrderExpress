package com.zc.express.api;

import com.zc.express.bean.QueryOrder;
import com.zc.express.bean.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZC on 2017/6/23.
 */

public interface ExpressApi {

    @POST("user/check")
    Observable<ResponseBody> login(@Body User user);//登录


    @GET("user")
    Observable<ResponseBody> queryUser(@Query("id") int id);//查询用户

    @POST("user")
    Observable<ResponseBody> update(@Body User user);//更新用户信息can only be used to update password phone, realname, and company


    @POST("/order/user/orders")
    Observable<ResponseBody> queryOrder(@Header("Authorization") String auth, @Body QueryOrder queryOrder);

    @GET("lbs/pendingOrders/{pickerId}")
    Observable<Response<ResponseBody>> getWaitOrder(@Header("Authorization") String auth, @Path("pickerId") String uid);

    @GET("order/picker/summary")
    Observable<ResponseBody> getSuccessOrder(@Header("Authorization") String auth, @Query("pickerid") String uid, @Query("start") String stime, @Query("end") String etime);//查询已完成订单

    /**
     * 文件内容，参数名file
     *
     * @param filePart
     * @return
     */
    @Multipart
    @POST("user/photo")
    Observable<ResponseBody> uploadFile(@Part MultipartBody.Part filePart);

}

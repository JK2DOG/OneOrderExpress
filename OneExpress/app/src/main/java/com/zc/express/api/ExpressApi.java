package com.zc.express.api;

import com.zc.express.bean.ApiResponse;
import com.zc.express.bean.User;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZC on 2017/6/23.
 */

public interface ExpressApi {

    @POST("user/check")
    Observable<User> login(@Body User user);//登录


    @GET("user")
    Observable<User> queryUser(@Query("id") int id);//查询用户

    @POST("user")
    Observable<User> update(@Body User user);//更新用户信息can only be used to update password phone, realname, and company

    /**
     * 文件内容，参数名file
     * @param filePart
     * @return
     */
    @Multipart
    @POST("user/photo")
    Observable<ApiResponse> uploadFile(@Part MultipartBody.Part filePart);

}

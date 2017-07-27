package com.zc.express.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.zc.express.api.ExpressApi;
import com.zc.express.api.UserReadableException;
import com.zc.express.bean.Auth;
import com.zc.express.bean.Location;
import com.zc.express.bean.Order;
import com.zc.express.bean.PushOrder;
import com.zc.express.bean.SetPushId;
import com.zc.express.bean.User;
import com.zc.express.data.memory.ObjectProvider;
import com.zc.express.data.preference.ObjectPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ZC on 2017/6/23.
 */
@Module
public class UserModel {

    private Context mContext;
    private ExpressApi mExpressApi;

    @Inject
    public UserModel(Context context, ExpressApi expressApi) {
        mContext = context;
        mExpressApi = expressApi;
    }

    /**
     * 登录
     */
    public Observable<ResponseBody> login(User user) {
        return mExpressApi.login(user).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 注册
     */
    public Observable<Response<ResponseBody>> register(User user) {
        return mExpressApi.register(user).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 更新用户信息
     * can only be used to update password phone, realname, and company
     */
    public Observable<ResponseBody> update(User user) {
        return mExpressApi.update(user).observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 检查登录状态，用于更新user信息
     *
     * @param context
     * @return
     */
    public Observable<ResponseBody> checkLogin(Context context) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        return login(new User(auth.getUsername(), auth.getPassword()));
    }

    //设置PUSH_ID
    public Observable<Response<ResponseBody>> setPushId(Context context, String rid) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.setPushId(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), getUser().getId() + "", new SetPushId(rid)).observeOn(AndroidSchedulers.mainThread());
    }

    //设置坐标
    public Observable<Response<ResponseBody>> setLocation(Context context, double lat, double lnt) {

        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.setLocation(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), getUser().getId() + "", new Location(lat, lnt)).observeOn(AndroidSchedulers.mainThread());
    }


//
//    public Observable<ResponseBody> queryOrder(Context context) {
//        Auth auth = ObjectPreference.getObject(context, Auth.class);
//        if (null == auth) {
//            return Observable.error(new UserReadableException(""));
//        }
//        String authStrng = auth.getUsername() + ":" + auth.getPassword();
//        return mExpressApi.queryOrder(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), new QueryOrder(getUser().getId(), null, null)).observeOn(AndroidSchedulers.mainThread());
//    }

    //待完成的订单
    public Observable<ResponseBody> getWaitOrder(Context context) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.getWaitOrder(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), getUser().getId() + "").observeOn(AndroidSchedulers.mainThread());
    }

    //已完成的订单
    public Observable<ResponseBody> getSuccessOrder(Context context) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.getSuccessOrder(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), getUser().getId() + "", "1970-01-01", "2017-07-05").observeOn(AndroidSchedulers.mainThread());
    }


    //订单详情
    public Observable<Response<ResponseBody>> getOrderDetails(Context context, String oid) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.getOrderDetails(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), oid).observeOn(AndroidSchedulers.mainThread());
    }

    //订单详情
    public Observable<Response<ResponseBody>> recheckOrder(Context context, Order entity) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.recheckOrder(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), entity).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 头像上传
     *
     * @param path path
     * @return 头像链接
     */
    public Observable<Response<ResponseBody>> updateAvatar(Context context, String path) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        // 转换成jpg上传文件服务器
       File file=new File(path);

        return mExpressApi.uploadFile(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), MultipartBody.Part.createFormData("file", "avatar.jpg", RequestBody.create(MediaType.parse("image/jpeg"), file))).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 抢单
     */
    public Observable<Response<ResponseBody>> robOrder(Context context, String oid) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.robOrder(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(),"text/plain","true", oid, getUser().getId() + "").observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 保存登录状态
     *
     * @param user
     */
    public void saveUser(User user) {
        ObjectProvider.sharedInstance().set(user);
        ObjectPreference.saveObject(mContext, user);
    }

    public void saveConfirmOrder(Order order) {
        ObjectPreference.saveObject(mContext, order);
        ObjectProvider.sharedInstance().set(order);
    }

    public Order getConfirmOrder() {
        Order entity = ObjectProvider.sharedInstance().get(Order.class);
        if (null == entity) {
            entity = ObjectPreference.getObject(mContext, Order.class);
            if (null != entity)
                ObjectProvider.sharedInstance().set(entity);
        }
        return entity;
    }

    public void savePushOrder(String oid) {
        ObjectPreference.saveObject(mContext, new PushOrder(oid));
    }

    public PushOrder getPushOrder() {
        PushOrder entity = ObjectProvider.sharedInstance().get(PushOrder.class);
        return entity;
    }

    private User getUser() {
        User user = ObjectProvider.sharedInstance().get(User.class);
        if (null == user) {
            user = ObjectPreference.getObject(mContext, User.class);
            if (null != user)
                ObjectProvider.sharedInstance().set(user);
        }
        return user;
    }

    /**
     * 是否已登录
     *
     * @return
     */
    public boolean isLogin() {
        Auth auth = ObjectPreference.getObject(mContext, Auth.class);
        return null != getUser() && auth != null;
    }


    /**
     * 退出登录
     */
    public void logout() {
        ObjectProvider.sharedInstance().remove(User.class);
        ObjectPreference.clearObject(mContext, User.class);
        ObjectPreference.clearObject(mContext, Order.class);
        ObjectProvider.sharedInstance().remove(Order.class);
        ObjectPreference.clearObject(mContext, PushOrder.class);

    }

    /**
     * 获取验证码
     */
    public void getVerificationCode(String phone) {

    }


//    /**
//     * 注册
//     * @param name
//     * @param phone
//     * @param code
//     * @param password
//     * @return
//     */
//    public Observable<User> register(String name, String phone, String code, String password){
//        return mDoctorApi.register(phone, password, password, 2).map(new Func1<ApiResponse<User>, User>() {
//            @Override
//            public User call(ApiResponse<User> userApiResponse) {
//                User user = userApiResponse.getData();
//                if (null != user)
//                    saveUser(user);
//                return user;
//            }
//        }).observeOn(AndroidSchedulers.mainThread());
//
//    }
}

package com.zc.express.model;

import android.content.Context;
import android.util.Base64;

import com.zc.express.api.ExpressApi;
import com.zc.express.api.UserReadableException;
import com.zc.express.bean.Auth;
import com.zc.express.bean.QueryOrder;
import com.zc.express.bean.User;
import com.zc.express.data.memory.ObjectProvider;
import com.zc.express.data.preference.ObjectPreference;

import javax.inject.Inject;

import dagger.Module;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ZC on 2017/6/23.
 */
@Module
public class UserModel {

    private Context mContext;
    private ExpressApi mExpressApi;

    @Inject
    public UserModel(Context context, ExpressApi expressApi){
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
     * 更新用户信息
     * can only be used to update password phone, realname, and company
     */
    public Observable<ResponseBody> update(User user) {
        return mExpressApi.update(user).observeOn(AndroidSchedulers.mainThread());

    }



    /**
     * 检查登录状态，用于更新user信息
     * @param context
     * @return
     */
    public Observable<ResponseBody> checkLogin(Context context){
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth){
            return Observable.error(new UserReadableException(""));
        }
        return login(new User(auth.getUsername(), auth.getPassword()));
    }


    public  Observable<ResponseBody> queryOrder(Context context){
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth){
            return Observable.error(new UserReadableException(""));
        }
        String authStrng=auth.getUsername()+":"+auth.getPassword();
        return  mExpressApi.queryOrder(Base64.encodeToString(authStrng.getBytes(),Base64.DEFAULT).trim(),new QueryOrder(getUser().getId(),null,null)).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseBody> getWaitOrder(Context context) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.getWaitOrder(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(), getUser().getId() + "").observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResponseBody> getSuccessOrder(Context context) {
        Auth auth = ObjectPreference.getObject(context, Auth.class);
        if (null == auth) {
            return Observable.error(new UserReadableException(""));
        }
        String authStrng = auth.getUsername() + ":" + auth.getPassword();
        return mExpressApi.getSuccessOrder(Base64.encodeToString(authStrng.getBytes(), Base64.DEFAULT).trim(),getUser().getId() + "","1970-01-01","2017-07-05").observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 保存登录状态
     * @param user
     */
    public void saveUser(User user){
        ObjectProvider.sharedInstance().set(user);
        ObjectPreference.saveObject(mContext, user);
    }

    private User getUser(){
        User user = ObjectProvider.sharedInstance().get(User.class);
        if (null == user){
            user = ObjectPreference.getObject(mContext, User.class);
            if (null != user)
                ObjectProvider.sharedInstance().set(user);
        }
        return user;
    }

    /**
     * 是否已登录
     * @return
     */
    public boolean isLogin(){
        Auth auth = ObjectPreference.getObject(mContext, Auth.class);
        return null != getUser() && auth != null;
    }

    /**
     * 退出登录
     */
    public void logout(){
        ObjectProvider.sharedInstance().remove(User.class);
        ObjectPreference.clearObject(mContext, User.class);
    }

    /**
     * 获取验证码
     */
    public void getVerificationCode(String phone){

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

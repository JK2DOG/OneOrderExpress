package com.zc.express;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.zc.express.api.ExpressApi;
import com.zc.express.api.ExpressApiProvider;
import com.zc.express.bean.User;
import com.zc.express.data.memory.ObjectProvider;
import com.zc.express.data.network.OkHttp;
import com.zc.express.data.network.PicassoProvider;
import com.zc.express.data.preference.ObjectPreference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * 全局依赖注入
 */
@Module
public class ExpressModule {

    private Context mAppContext;
    private Picasso mPicasso;

    public ExpressModule(Context context) {
        mAppContext = context;
    }

    @Provides
    @Singleton
    public Context getAppContext() {
        return mAppContext;
    }

    @Singleton
    @Provides
    public OkHttpClient getOkHttpClient() {
        return OkHttp.client(mAppContext);
    }

    @Singleton
    @Provides
    public Picasso getPicasso() {
        if (null == mPicasso)
            mPicasso = PicassoProvider.builder(getAppContext(), getOkHttpClient()).build();

        return mPicasso;
    }

    @Singleton
    @Provides
    public ExpressApi getDoctorApi() {
        return ExpressApiProvider.api();
    }

    @Singleton
    @Provides
    public User getUser() {
        User user = ObjectProvider.sharedInstance().get(User.class);
        if (null == user) {
            user = ObjectPreference.getObject(getAppContext(), User.class);
            if (null != user)
                ObjectProvider.sharedInstance().set(user);
        }
        return user;
    }
}

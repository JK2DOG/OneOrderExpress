package com.zc.express.view.activity.user;

import com.zc.express.ExpressModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ZC on 2017/6/27.
 */
@Singleton
@Component(modules = ExpressModule.class)
public interface UserComponent {
    void inject(UserInfoActivity activity);

    void inject(EditUserInfoActivity activity);
}

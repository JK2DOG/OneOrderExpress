package com.zc.express.view.activity.login;

import com.zc.express.ExpressModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ZC on 2017/6/26.
 */
@Singleton
@Component(modules = ExpressModule.class)
public interface LoginComponent {
    void inject(LoginActivity object);
    void inject(RegisterActivity activity);

}

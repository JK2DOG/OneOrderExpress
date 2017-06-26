package com.zc.express.view.activity.login;

import com.zc.express.ExpressComponent;
import com.zc.express.model.UserModel;
import com.zc.express.scopes.UserScope;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ZC on 2017/6/26.
 */
@UserScope
@Component(modules = UserModel.class,dependencies = ExpressComponent.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}

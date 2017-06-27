package com.zc.express.view.activity.user;

import com.zc.express.ExpressComponent;
import com.zc.express.ExpressModule;

import dagger.Component;

/**
 * Created by ZC on 2017/6/27.
 */
@Component(modules = ExpressModule.class, dependencies = ExpressComponent.class)
public interface UserComponent {
    void inject(UserInfoActivity activity);

    void inject(EditUserInfoActivity activity);
}

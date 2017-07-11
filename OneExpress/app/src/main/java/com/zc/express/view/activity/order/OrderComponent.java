package com.zc.express.view.activity.order;

import com.zc.express.ExpressModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ZC on 2017/7/11.
 */
@Singleton
@Component(modules = ExpressModule.class)
public interface OrderComponent {
    void inject(OrderDetailsActivity activity);
}

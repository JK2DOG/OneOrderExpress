package com.zc.express.view.activity.home;

import com.zc.express.ExpressModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = ExpressModule.class)
public interface MainmenuComponent {
    void inject(MainActivity mainActivity);
}

package com.zc.express;

import android.app.Application;
import android.app.Notification;

import com.tencent.bugly.crashreport.CrashReport;
import com.zc.express.api.ExpressApiProvider;
import com.zc.express.data.network.OkHttp;
import com.zc.express.utils.ToastUtils;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by ZC on 2017/6/23.
 */

public class ExpressAplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(),"92b933de11", false);
        ExpressModule.init(this);
        OkHttp.createCache(getCacheDir());
        ExpressApiProvider.init(OkHttp.client(this));
        ToastUtils.init(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        setStyleBasic();
    }



    private void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.mipmap.app_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }

}

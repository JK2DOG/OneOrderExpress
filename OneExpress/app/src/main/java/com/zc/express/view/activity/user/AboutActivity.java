package com.zc.express.view.activity.user;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.view.activity.BaseActivity;

import butterknife.BindView;

/**
 * Created by ZC on 2017/6/29.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_version)
    TextView mVersionTv;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText("关于");
        String version = getVersion();
        mVersionTv.setText("版本号:" + " V" + version);
    }


    private String getVersion() {

        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getString(R.string.about_version);
        }
    }
}

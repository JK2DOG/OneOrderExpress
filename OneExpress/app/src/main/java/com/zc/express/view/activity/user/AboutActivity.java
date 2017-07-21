package com.zc.express.view.activity.user;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

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

    @OnClick({R.id.tv_phone, R.id.tv_feedback})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:
                Intent intentPhone = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "057158114447");
                intentPhone.setData(data);
                startActivity(intentPhone);
                break;
            case R.id.tv_feedback:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "contact@gladexpress.com", null));
                startActivity(Intent.createChooser(intent, "Send Email to contact@gladexpress.com"));
//                Intent data = new Intent(Intent.ACTION_SENDTO);
//                data.setData(Uri.parse("mailto:contact@gladexpress.com"));
//                startActivity(data);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

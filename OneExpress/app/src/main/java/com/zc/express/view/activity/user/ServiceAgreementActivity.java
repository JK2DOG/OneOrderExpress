package com.zc.express.view.activity.user;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.widget.BrowserLayout;

import butterknife.BindView;

/**
 * Created by ZC on 2017/7/21.
 */

public class ServiceAgreementActivity extends BaseActivity {

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.common_web_browser_layout)
    BrowserLayout mBrowserLayout;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_service;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText("服务条款");
        mBrowserLayout.loadUrl("file:///android_asset/service_agreement.html");
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

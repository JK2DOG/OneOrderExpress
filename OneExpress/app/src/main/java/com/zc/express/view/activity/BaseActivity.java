package com.zc.express.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zc.express.ExpressAplication;
import com.zc.express.ExpressComponent;
import com.zc.express.view.widget.EmptyLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ZC on 2017/6/23.
 */

public abstract class BaseActivity extends AppCompatActivity {


    /**
     * 把 EmptyLayout 放在基类统一处理，@Nullable 表明 View 可以为 null，详细可看 ButterKnife
     */
//    @Nullable
//    @BindView(R.id.empty_layout)
//    protected EmptyLayout mEmptyLayout;

    private ProgressDialog mProgress;
    private Unbinder unbinder;


    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    private void initProgress() {
        mProgress = new ProgressDialog(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        unbinder = ButterKnife.bind(this);
        initInjector();
        initViews();
        initProgress();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void showLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
        }
    }

    public void hideLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }

    public void showNetError(final EmptyLayout.OnRetryListener onRetryListener) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
            mEmptyLayout.setRetryListener(onRetryListener);
        }
    }


    /**
     * 获取 ExpressComponent
     *
     * @return ExpressComponent
     */
    protected ExpressComponent getAppComponent() {
        return ExpressAplication.getExpressComponent();
//        return ((AndroidApplication) getApplication()).getAppComponent();
    }


    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }


    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showProgress(String msg) {
        dissmissProgress();
        if (mProgress != null) {
            mProgress.setMessage(msg);
            mProgress.setCancelable(true);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.show();
        }
    }

    public void showProgress() {
        dissmissProgress();
        if (mProgress != null) {
            mProgress.setMessage("努力加载中...");
            mProgress.setCancelable(true);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.show();
        }
    }

    public void dissmissProgress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

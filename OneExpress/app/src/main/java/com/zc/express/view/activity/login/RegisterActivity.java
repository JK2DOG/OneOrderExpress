package com.zc.express.view.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.Auth;
import com.zc.express.bean.User;
import com.zc.express.data.preference.ObjectPreference;
import com.zc.express.model.LocationMgr;
import com.zc.express.model.UserModel;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.activity.home.MainActivity;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by ZC on 2017/7/12.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @BindView(R.id.et_name)
    EditText mNameEt;//用户名

    @BindView(R.id.et_email)
    EditText mEmailEt;//邮箱

    @BindView(R.id.et_password)
    EditText mPwdEt;//密码

    @BindView(R.id.et_password2)
    EditText mPwdEt2;//确认密码


    private double mLat = 0;
    private double mLnt = 0;

    @Inject
    UserModel mUserModel;
    @Inject
    RxSubscriptionCollection mSubscriptionCollection;


    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initInjector() {
        DaggerLoginComponent.create().inject(this);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText("注册");
        LocationMgr.getMyLocation(RegisterActivity.this, mOnLocationListener);
    }


    @OnClick(R.id.btn_register)
    void onClick() {
        final String name = mNameEt.getText().toString().trim();
        String email = mEmailEt.getText().toString().trim();
        final String pwd = mPwdEt.getText().toString().trim();
        String pwd2 = mPwdEt2.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast("用户名不能为空！");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showToast("邮箱不能为空！");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast("密码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(pwd2)) {
            ToastUtils.showToast("确认密码不能为空！");
            return;
        }
        if (!pwd.equals(pwd2)) {
            ToastUtils.showToast("两次输入密码不一致！");
            return;
        }
        showProgress();
        mSubscriptionCollection.add(mUserModel.register(new User(name, pwd, email)).subscribe(new Action1<Response<ResponseBody>>() {
            @Override
            public void call(Response<ResponseBody> response) {
                dissmissProgress();
                if (response.isSuccessful()) {
                    String data = null;
                    try {
                        data = response.body().string();
                        Log.e("retrofit", data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    User mUser = JsonUtils.convertEntity(data, User.class);
                    if (null != mUser) {
                        mUserModel.saveUser(mUser);
                        ObjectPreference.saveObject(RegisterActivity.this, new Auth(name, pwd));
                        Context context = RegisterActivity.this;
                        context.startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                    setPushId();
                    setLocation();
                } else {
                    ToastUtils.showToast("ERROR"+response.code());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                dissmissProgress();
                ToastUtils.showToast(e.getMessage());
                Log.e("zc", "Throwable:" + e.getMessage());
            }
        }));
    }


    private void setLocation() {
        mSubscriptionCollection.add(mUserModel.setLocation(RegisterActivity.this, mLat, mLnt).subscribe(new Action1<Response<ResponseBody>>() {
            @Override
            public void call(Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("retrofit", response.message());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                Log.e("zc", "Throwable:" + e.getMessage());
            }
        }));
    }

    private void setPushId() {
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        mSubscriptionCollection.add(mUserModel.setPushId(RegisterActivity.this, rid).subscribe(new Action1<Response<ResponseBody>>() {
            @Override
            public void call(Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showToast("PUSH_ID设置成功！");
                    Log.e("retrofit", response.message());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                ToastUtils.showToast("PUSH_ID设置失败！" + e.getMessage());
                Log.e("zc", "Throwable:" + e.getMessage());
            }
        }));
    }


    private LocationMgr.onLocationListener mOnLocationListener = new LocationMgr.onLocationListener() {
        @Override
        public void onLocationChanged(int code, double lat1, double long1, String location) {
            if (0 == code) {
                mLat = lat1;
                mLnt = long1;
                Log.e("ZC--------------", "lat1:" + lat1 + "long1:" + long1 + "地址:" + location);
            } else {
                mLat = 0;
                mLnt = 0;
                Log.e("ZC00000000000000", "失败");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptionCollection.cancelAll();
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

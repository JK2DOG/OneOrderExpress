package com.zc.express.view.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.Auth;
import com.zc.express.bean.User;
import com.zc.express.data.preference.ObjectPreference;
import com.zc.express.model.UserModel;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.activity.home.MainActivity;
import com.zc.express.view.activity.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 编辑用户信息
 * Created by ZC on 2017/6/26.
 */

public class EditUserInfoActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @BindView(R.id.et_name)
    EditText mNameEt;

    @BindView(R.id.et_phone)
    EditText mPhoneEt;

    @BindView(R.id.et_company)
    EditText mCompanyEt;

    @Inject
    User mUser;
    @Inject
    UserModel mUserModel;

    Subscription mSubscription;


    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_edit_user;
    }

    @Override
    protected void initInjector() {
        DaggerUserComponent.create().inject(this);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText("编辑资料");
        mNameEt.setText(mUser.getReal_name());
        mPhoneEt.setText(mUser.getPhone());
        mCompanyEt.setText(mUser.getCompany());

    }

    @OnClick(R.id.tv_save)void onClick(){
        final String name = mNameEt.getText().toString().trim();
        if (name.isEmpty()) {
            mNameEt.setError("请填写完整！");
            return;
        }

        final String phone = mPhoneEt.getText().toString().trim();
        if (phone.isEmpty()) {
            mPhoneEt.setError("请填写完整！");
            return;
        }
        final String comppany = mCompanyEt.getText().toString().trim();
        if (comppany.isEmpty()) {
            mCompanyEt.setError("请填写完整！");
            return;
        }
        mUser.setCompany(comppany);
        mUser.setReal_name(name);
        mUser.setPhone(phone);
        mSubscription=mUserModel.update(mUser).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                Log.e("zc", "ResponseBody" );
                try {
                    String data = responseBody.string();
                    JSONObject jsonObject = new JSONObject(data);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {//请求成功
                        User mUser = JsonUtils.convertEntity(data, User.class);
                        if (null != mUser) {
                            mUserModel.saveUser(mUser);
                            finish();
                        }
                    } else {//请求失败
                        String errorMsg = jsonObject.optString("message");
                        Log.e("zc", "ErrorMsg:" + errorMsg);
                        ToastUtils.showToast("修改失败！"+errorMsg);
                    }
                    Log.e("retrofit", responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                Log.e("zc", "Throwable:" + e.getMessage());
                ToastUtils.showToast("修改失败！"+e.getMessage());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSubscription)
            mSubscription.unsubscribe();
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

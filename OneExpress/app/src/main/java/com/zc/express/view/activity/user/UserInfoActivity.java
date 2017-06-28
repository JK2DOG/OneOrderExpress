package com.zc.express.view.activity.user;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.User;
import com.zc.express.model.UserModel;
import com.zc.express.view.activity.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户信息
 * Created by ZC on 2017/6/26.
 */

public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @BindView(R.id.tv_name)
    TextView mNameTv;//昵称

    @BindView(R.id.tv_email)
    TextView mEmailTv;//邮箱

    @BindView(R.id.tv_phone)
    TextView mPhoneTv;//电话

    @BindView(R.id.tv_company)
    TextView mCompanyTv;//昵称


    @Inject
    User mUser;
    @Inject
    UserModel mUserModel;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_user;
    }


    @Override
    protected void initInjector() {
        DaggerUserComponent.create().inject(this);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText("个人中心");
        mNameTv.setText(mUser.getUser_name());
        mEmailTv.setText(mUser.getEmail());
        mPhoneTv.setText(mUser.getPhone());
        mCompanyTv.setText(mUser.getCompany());
    }


    @OnClick(R.id.tv_exit)
    void onExit() {
        AlertDialog dialog = new AlertDialog.Builder(this).setCancelable(false)
                .setTitle(getString(R.string.logout_tip_title))
                .setMessage(getString(R.string.logout_tip_content))

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserModel.logout();
                        finish();
                        dialog.dismiss();
                    }
                })
                .show();
//        dialog.setCanceledOnTouchOutside(false);
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

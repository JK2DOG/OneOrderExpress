package com.zc.express.view.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zc.express.R;
import com.zc.express.bean.User;
import com.zc.express.model.UserModel;
import com.zc.express.utils.Constant;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.widget.PicassoCircleTransform;
import com.zc.express.view.widget.RoundAngleImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.dreamheart.imagecrop.ImageCropActivity;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * 用户信息
 * Created by ZC on 2017/6/26.
 */

public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题


    @BindView(R.id.tv_realname)
    TextView mRealTime;//实名


    @BindView(R.id.tv_name)
    TextView mNameTv;//昵称

    @BindView(R.id.tv_email)
    TextView mEmailTv;//邮箱

    @BindView(R.id.tv_phone)
    TextView mPhoneTv;//电话

    @BindView(R.id.tv_company)
    TextView mCompanyTv;//昵称

    @BindView(R.id.iv_head)
    RoundAngleImageView mHeadIv;//头像

    @Inject
    RxSubscriptionCollection mSubscriptionCollection;

    @Inject
    User mUser;
    @Inject
    UserModel mUserModel;
    @Inject
    Picasso mPicasso;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPicasso.load(Constant.HEAD_URL + mUser.getId() + ".png") .memoryPolicy(MemoryPolicy.NO_CACHE).error(R.mipmap.app_logo).transform(new PicassoCircleTransform()).into(mHeadIv);
        mNameTv.setText(mUser.getUser_name());
        mRealTime.setText(mUser.getReal_name());
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

    @OnClick(R.id.iv_head)
    void head() {
        ImageCropActivity.start(this, new ImageCropActivity.BmpReceiver() {
            @Override
            public void onBitmap(Bitmap bitmap) {
                final String path = MediaStore.Images.Media.insertImage(UserInfoActivity.this.getContentResolver(), bitmap, "avatar", null);
                mSubscriptionCollection.add(mUserModel.updateAvatar(UserInfoActivity.this, bitmap).subscribe(new Action1<Response<ResponseBody>>() {
                    @Override
                    public void call(Response<ResponseBody> response) {

                        if (response.code() == 200) {
                            mPicasso.load(path).error(R.mipmap.app_logo).transform(new PicassoCircleTransform()).into(mHeadIv);
                            ToastUtils.showToast("设置成功！");
                        } else {
                            ToastUtils.showToast("设置失败！");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.showToast("设置失败！");
                    }
                }));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
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
            case R.id.item_edit:
                startActivity(new Intent(this, EditUserInfoActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

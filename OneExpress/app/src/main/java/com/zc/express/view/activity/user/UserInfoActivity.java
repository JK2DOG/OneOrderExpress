package com.zc.express.view.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.squareup.picasso.MemoryPolicy;
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

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.functions.Action1;

import static android.R.attr.path;

/**
 * 用户信息
 * Created by ZC on 2017/6/26.
 */

public class UserInfoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {


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

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_user;
    }


    @Override
    protected void initInjector() {
        DaggerUserComponent.create().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
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
        mPicasso.load(Constant.HEAD_URL + mUser.getId() + ".png").memoryPolicy(MemoryPolicy.NO_CACHE).error(R.mipmap.app_logo).transform(new PicassoCircleTransform()).into(mHeadIv);
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
        ActionSheetDialogNoTitle();
    }

    /**
     * 拍照和相册上传头像
     */
    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"相机", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                Uri imageUri = Uri.fromFile(file);
                configCompress(getTakePhoto());
                configTakePhotoOption(getTakePhoto());
                switch (position) {
                    case 0://拍照
                        getTakePhoto().onPickFromCaptureWithCrop(imageUri,getCropOptions());
                        break;
                    case 1://相册
                        getTakePhoto().onPickFromGalleryWithCrop(imageUri,getCropOptions());
                        break;
                }
                dialog.dismiss();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        final String path= result.getImage().getCompressPath();
        Log.e("zc","takeSuccess：" + result.getImage().getCompressPath());
        mSubscriptionCollection.add(mUserModel.updateAvatar(UserInfoActivity.this, path).subscribe(new Action1<Response<ResponseBody>>() {
            @Override
            public void call(Response<ResponseBody> response) {

                if (response.code() == 200) {
                    mPicasso.load(Constant.HEAD_URL + mUser.getId() + ".png").memoryPolicy(MemoryPolicy.NO_CACHE).error(R.mipmap.app_logo).transform(new PicassoCircleTransform()).into(mHeadIv);
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

    @Override
    public void takeFail(TResult result, String msg) {
        Log.e("zc", "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.e("zc", getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
    }

    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(102400)
                .setMaxPixel(800)
                .enableReserveRaw(false)
                .create();

        takePhoto.onEnableCompress(config, false);

    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);//使用库选取图片
        builder.setCorrectImage(true);//纠正旋转角度
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(400).setAspectY(400);
        builder.setWithOwnCrop(false);
        return builder.create();
    }
}

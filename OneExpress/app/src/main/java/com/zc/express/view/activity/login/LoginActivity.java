package com.zc.express.view.activity.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.zc.express.R;
import com.zc.express.bean.Auth;
import com.zc.express.bean.User;
import com.zc.express.data.preference.ObjectPreference;
import com.zc.express.model.UserModel;
import com.zc.express.utils.SimpleTextWatcher;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.home.MainActivity;
import com.zc.express.view.activity.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ZC on 2017/6/23.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.logo)
    ImageView mLogoIv;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.et_mobile)
    EditText mMobileEt;
    @BindView(R.id.et_password)
    EditText mPasswordEt;
    @BindView(R.id.iv_clean_phone)
    ImageView mCleanMobileIv;
    @BindView(R.id.clean_password)
    ImageView mCleanPasswordIv;
    @BindView(R.id.iv_show_pwd)
    ImageView mShowPwdIv;
    @BindView(R.id.service)
    View mServiceView;
    @BindView(R.id.content)
    View mContentView;


    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private float scale = 0.6f; //logo缩放比例

    Subscription mLoginSubscription;
    @Inject
    UserModel mUserModel;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        DaggerLoginComponent.builder().expressComponent(getAppComponent()).build().inject(this);
    }

    @Override
    protected void initViews() {
        if (isFullScreen(this)) {
            AndroidBug5497Workaround.assistActivity(this);
        }
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
        mMobileEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanMobileIv.getVisibility() == View.GONE) {
                    mCleanMobileIv.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanMobileIv.setVisibility(View.GONE);
                }
            }
        });
        mPasswordEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanPasswordIv.getVisibility() == View.GONE) {
                    mCleanPasswordIv.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPasswordIv.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    ToastUtils.showToast(R.string.please_input_limit_pwd);
                    s.delete(temp.length() - 1, temp.length());
                    mPasswordEt.setSelection(s.length());
                }
            }
        });
        /**
         * 禁止键盘弹起的时候可以滚动
         */
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mScrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e("wenzhihao", "up------>" + (oldBottom - bottom));
                    int dist = mContentView.getBottom() - bottom;
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContentView, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        zoomIn(mLogoIv, dist);
                    }
                    mServiceView.setVisibility(View.INVISIBLE);

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
                    if ((mContentView.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContentView, "translationY", mContentView.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                        zoomOut(mLogoIv);
                    }
                    mServiceView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @OnClick({R.id.iv_clean_phone, R.id.clean_password, R.id.iv_show_pwd, R.id.btn_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clean_phone:
                mMobileEt.setText("");
                break;
            case R.id.clean_password:
                mPasswordEt.setText("");
                break;
            case R.id.iv_show_pwd:
                if (mPasswordEt.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mShowPwdIv.setImageResource(R.mipmap.icon_eye_visible);
                } else {
                    mPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mShowPwdIv.setImageResource(R.mipmap.icon_eye_invisible);
                }
                String pwd = mPasswordEt.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    mPasswordEt.setSelection(pwd.length());
                break;
            case R.id.btn_login://登录
                onRootLayoutClick(v);
                login();
                break;
        }
    }


    /**
     * 登录
     */
    private void login() {
        final String username = mMobileEt.getText().toString();
        if (username.isEmpty()) {
            mMobileEt.setError(getString(R.string.user_name_hint));
            return;
        }

        final String password = mPasswordEt.getText().toString();
        if (password.isEmpty()) {
            mPasswordEt.setError(getString(R.string.password_hint));
            return;
        }

        mLoginSubscription =mUserModel.login(new User(username,password)).subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                if (null != user)
                    mUserModel.saveUser(user);
                ObjectPreference.saveObject(LoginActivity.this, new Auth(username, password));

                Context context = LoginActivity.this;
                context.startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ToastUtils.showToast("登录失败！");
            }
        });
    }


    @OnClick(R.id.root)
    public void onRootLayoutClick(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 缩小
     *
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    /**
     * f放大
     *
     * @param view
     */
    public void zoomOut(final View view) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();
    }

    public boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }
}

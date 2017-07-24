package com.zc.express.view.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zc.express.R;
import com.zc.express.bean.ItemBean;
import com.zc.express.bean.Order;
import com.zc.express.bean.PushOrder;
import com.zc.express.bean.User;
import com.zc.express.model.UserModel;
import com.zc.express.utils.Constant;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.activity.login.LoginActivity;
import com.zc.express.view.activity.order.OrderConfirmActivity;
import com.zc.express.view.activity.order.OrderDetailsActivity;
import com.zc.express.view.activity.user.AboutActivity;
import com.zc.express.view.activity.user.ServiceAgreementActivity;
import com.zc.express.view.activity.user.UserInfoActivity;
import com.zc.express.view.fragment.CompleteFragment;
import com.zc.express.view.fragment.WaitFragment;
import com.zc.express.view.widget.DragLayout;
import com.zc.express.view.widget.PicassoCircleTransform;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.MultiActionsNotificationBuilder;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class MainActivity extends BaseActivity {
    @BindView(R.id.dl)
    DragLayout dl;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;//用户主页头像
    @BindView(R.id.iv_head)
    ImageView mHeadIv;//用户左边头像
    @BindView(R.id.tv_name)
    TextView mNameTv;//昵称

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation mAhBottomNavigation;

    private List<Fragment> fragments = new ArrayList<>();
    // user logged into another device
    public boolean isConflict = false;
    public static boolean isForeground = false;
    private boolean isConflictDialogShow;
    private AlertDialog.Builder conflictBuilder;
    private AlertDialog.Builder confirmBuilder;


    private QuickAdapter<ItemBean> quickAdapter;

    private int currentTabIndex;
    @Inject
    UserModel mUserModel;
    @Inject
    Picasso mPicasso;
    @Inject
    RxSubscriptionCollection mSubscriptionCollection;
    private String mCancleUid;
    private String mHaveOid;
    private String mConfirmOid;

    private Order mConfirmOrder;
    private PushOrder mPushOrder;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        DaggerMainmenuComponent.create().inject(this);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        if (isConflictDialogShow) {
            return;
        }
        // 未登录则重新登录
        if (!mUserModel.isLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        mConfirmOrder=mUserModel.getConfirmOrder();
        if (mConfirmOrder!=null){
            ToastUtils.showToast("您有一条未完成订单！");
            Intent intent = new Intent(MainActivity.this, OrderConfirmActivity.class);
            startActivity(intent);
            return;
        }
        mPushOrder=mUserModel.getPushOrder();
        if (mPushOrder!=null){
            ToastUtils.showToast("您有一条指派订单：" + mConfirmOid);
            showConfirmDialog(mConfirmOid);
            return;
        }
        // 检查登录状态
        mSubscriptionCollection.add(mUserModel.checkLogin(this).subscribe(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("zc", "Throwable:" + e.getMessage());
                ToastUtils.showToast("登录失败！" + e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                Log.e("zc0", "ResponseBody");
                try {
                    String data = responseBody.string();
                    JSONObject jsonObject = new JSONObject(data);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {//请求成功
                        User mUser = JsonUtils.convertEntity(data, User.class);
                        if (null != mUser) {
                            mUserModel.saveUser(mUser);
                            mNameTv.setText("名字：" + mUser.getUser_name());
                            mPicasso.load(Constant.HEAD_URL + mUser.getId() + ".png").memoryPolicy(MemoryPolicy.NO_CACHE).error(R.mipmap.app_logo).transform(new PicassoCircleTransform()).into(mHeadIv);
                            mPicasso.load(Constant.HEAD_URL + mUser.getId() + ".png").memoryPolicy(MemoryPolicy.NO_CACHE).error(R.mipmap.app_logo).transform(new PicassoCircleTransform()).into(ivIcon);
                        }
                    } else {//请求失败
                        String errorMsg = jsonObject.optString("message");
                        Log.e("zc", "ErrorMsg:" + errorMsg);
                        ToastUtils.showToast("登录失败！" + errorMsg);
                    }
                    Log.e("retrofit", responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make sure activity will not in background if user is logged into another device or removed
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            mUserModel.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPushIntent(intent);
    }

    //接手pushIntent
    private void getPushIntent(Intent it) {
        Bundle mBundle = it.getExtras();
        if (mBundle != null) {
            if (mBundle.getBoolean(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
                showConflictDialog();
            }
            mCancleUid = mBundle.getString(Constant.ACCOUNT_CANCEl);
            if (!TextUtils.isEmpty(mCancleUid)) {//取消
                ToastUtils.showToast(mCancleUid + "取消了下单！");
            }
            mHaveOid = mBundle.getString(Constant.HAVE_ORDER);
            if (!TextUtils.isEmpty(mHaveOid)) {//刷新待收取订单列表
                ToastUtils.showToast("有新的订单来了：" + mHaveOid);
                EventBus.getDefault().post(new PushOrder(mHaveOid));
            }
            mConfirmOid = mBundle.getString(Constant.CONFIRM_ORDER);
            if (!TextUtils.isEmpty(mConfirmOid)) {//指派订单
                ToastUtils.showToast("您有一条指派订单：" + mConfirmOid);
                mUserModel.savePushOrder(mConfirmOid);
                showConfirmDialog(mConfirmOid);
            }
        }
    }


    @Override
    protected void initViews() {
        JPushInterface.init(getApplicationContext());
//        registerMessageReceiver();  // used for receive msg
//        setStatusBar();
        initDragLayout();

        lv.setAdapter(quickAdapter = new QuickAdapter<ItemBean>(this, R.layout.item_left_layout, ItemDataUtils.getItemBeans()) {
            @Override
            protected void convert(BaseAdapterHelper helper, ItemBean item) {
                helper.setImageResource(R.id.item_img, item.getImg())
                        .setText(R.id.item_tv, item.getTitle());
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, ServiceAgreementActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                }
//                Toast.makeText(MainActivity.this, "Click Item " + position, Toast.LENGTH_SHORT).show();
            }
        });

        fragments.add(new WaitFragment());
        fragments.add(new CompleteFragment());

        showFragment(fragments.get(0));
        initBottomNav();
        getPushIntent(getIntent());
    }

    //当有指派订单时弹框
    private void showConfirmDialog(final String pid) {
        String st = "紧急任务";
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (confirmBuilder == null)
                    confirmBuilder = new AlertDialog.Builder(MainActivity.this);
                confirmBuilder.setTitle(st);
                confirmBuilder.setMessage("您有一条指派订单任务，请查看");
                confirmBuilder.setPositiveButton("确定接单", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        confirmBuilder = null;
                        OrderDetailsActivity.start(MainActivity.this, pid, true,true);
                    }
                });
                confirmBuilder.setCancelable(false);
                confirmBuilder.create().show();
            } catch (Exception e) {
                Log.e("zc", "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    //当用户在其他设备登录时
    private void showConflictDialog() {
        isConflictDialogShow = true;
        mUserModel.logout();
        String st = "下线通知";
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage("同一帐号已在其他设备登录");
                conflictBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                Log.e("zc", "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        super.onSaveInstanceState(outState);
    }

    @OnClick({R.id.iv_icon, R.id.iv_head})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                dl.open();
                break;
            case R.id.iv_head:
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                break;
        }
    }


    private void initBottomNav() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("待收取",
                R.mipmap.ic_tab_strip_icon_feed_selected);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("已收取",
                R.mipmap.ic_tab_strip_icon_category_selected);


        mAhBottomNavigation.addItem(item1);
        mAhBottomNavigation.addItem(item2);


        mAhBottomNavigation.setColored(false);
        mAhBottomNavigation.setForceTint(false);
        mAhBottomNavigation.setBehaviorTranslationEnabled(true);
        mAhBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mAhBottomNavigation.setAccentColor(getResources().getColor(R.color.black_90));
        mAhBottomNavigation.setInactiveColor(getResources().getColor(R.color.nav_text_color_mormal));
        mAhBottomNavigation.setCurrentItem(0);
        mAhBottomNavigation.setDefaultBackgroundColor(
                getResources().getColor(R.color.bottom_tab_bar_color));

        mAhBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (currentTabIndex != position) {
                    FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                    trx.hide(fragments.get(currentTabIndex));
                    if (!fragments.get(position).isAdded()) {
                        trx.add(R.id.content, fragments.get(position));
                    }
                    trx.show(fragments.get(position)).commit();
                }
                currentTabIndex = position;
                return true;
            }
        });
    }


    private void showFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }

    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragLayout.DragListener() {
            //界面打开的时候
            @Override
            public void onOpen() {
            }

            //界面关闭的时候
            @Override
            public void onClose() {
            }

            //界面滑动的时候
            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(ivIcon, 1 - percent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (dl.getStatus()== DragLayout.Status.OPEN) {
                    dl.close();
                } else {
                    finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        mSubscriptionCollection.cancelAll();
    }


    //for receive customer msg from jpush server
//    private MessageReceiver mMessageReceiver;
//    public static final String MESSAGE_RECEIVED_ACTION = "com.zc.express.MESSAGE_RECEIVED_ACTION";
//    public static final String KEY_TITLE = "title";
//    public static final String KEY_MESSAGE = "message";
//    public static final String KEY_EXTRAS = "extras";
//
//    public void registerMessageReceiver() {
//        mMessageReceiver = new MessageReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
//    }
//
//    public class MessageReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            try {
//                /**
//                 * 	* 当收到有抢单信息时直接跳转到主页面并且刷新列表，(Format:”新的单子”+单号+“在等待您接受”)
//                 * 当收到有必须提取包裹消息时跳转到主页面并且跳出确认信息，点击确定之后直接进去包裹详细信息页面(4) (Format:”运单号”+单号+“需要紧急指派你去收件”)
//                 * 当收到当前用户在其他机子登陆时登出当前用户 (Format:”您的账号已从另一台设备登录”)
//                 * 当收到用户取消下单时直接跳回主页面 (Format:”用户”+userID+“取消了取件单”)
//                 */
//                    if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                        Bundle  mPushBundle = intent.getBundleExtra(KEY_MESSAGE);
//                         if (mPushBundle!=null){
//                             for (String key : mPushBundle.keySet()) {
//                                 if (key.equals("cn.jpush.android.ALERT")) {
//                                     String  value=mPushBundle.getString(key);
//                                 }
//                             }
//                         }
//
//                    }
//
//
//            } catch (Exception e) {
//            }
//        }
//    }

}

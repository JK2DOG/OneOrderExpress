package com.zc.express.view.activity.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.squareup.picasso.Picasso;
import com.zc.express.R;
import com.zc.express.bean.ItemBean;
import com.zc.express.bean.User;
import com.zc.express.model.UserModel;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.activity.login.LoginActivity;
import com.zc.express.view.activity.user.UserInfoActivity;
import com.zc.express.view.fragment.CompleteFragment;
import com.zc.express.view.widget.DragLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
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

    private QuickAdapter<ItemBean> quickAdapter;

    private int currentTabIndex;
    @Inject
    UserModel mUserModel;
    @Inject
    Picasso mPicasso;
    @Inject
    RxSubscriptionCollection mSubscriptionCollection;

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
        super.onResume();
        // 未登录则重新登录
        if (!mUserModel.isLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
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

//                            mPicasso.load(mUser.get)
//                                    .noPlaceholder()
//                                    .error(R.drawable.avatar_default).transform(new PicassoCircleTransform()).into(mAvatarImg);
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
    protected void initViews() {
        JPushInterface.init(getApplicationContext());
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
                }
//                Toast.makeText(MainActivity.this, "Click Item " + position, Toast.LENGTH_SHORT).show();
            }
        });

        fragments.add(new CompleteFragment());
        fragments.add(new CompleteFragment());

        showFragment(fragments.get(0));
        initBottomNav();
    }


    @OnClick({R.id.iv_icon,R.id.iv_head})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                dl.open();
                break;
            case  R.id.iv_head:
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
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptionCollection.cancelAll();
    }

}

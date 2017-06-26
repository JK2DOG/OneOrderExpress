package com.zc.express.view.activity.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.zc.express.R;
import com.zc.express.bean.ItemBean;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.fragment.CompleteFragment;
import com.zc.express.view.widget.DragLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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


    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        setStatusBar();
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
                Toast.makeText(MainActivity.this, "Click Item " + position, Toast.LENGTH_SHORT).show();
            }
        });

        fragments.add(CompleteFragment.newInstance());
        fragments.add(CompleteFragment.newInstance());

        showFragment(fragments.get(0));
        initBottomNav();
    }


    @OnClick({R.id.iv_icon})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                dl.open();
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
}

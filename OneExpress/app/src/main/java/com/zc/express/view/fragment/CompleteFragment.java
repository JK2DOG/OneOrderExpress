package com.zc.express.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zc.express.R;
import com.zc.express.bean.Auth;
import com.zc.express.bean.Order;
import com.zc.express.bean.User;
import com.zc.express.data.preference.ObjectPreference;
import com.zc.express.model.UserModel;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.home.MainActivity;
import com.zc.express.view.activity.login.LoginActivity;
import com.zc.express.view.widget.AutoLoadOnScrollListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import okhttp3.ResponseBody;
import rx.functions.Action1;

/**
 * 已完成的的订单
 * Created by ZC on 2017/6/26.
 */

public class CompleteFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private AutoLoadOnScrollListener mAutoLoadOnScrollListener;

    private LinearLayoutManager mLinearLayoutManager;


    @Inject
    RxSubscriptionCollection mRxSubscriptionCollection;
    @Inject
    User mUser;
    @Inject
    UserModel mUserModel;

    private List<Order> mOrderLists= new ArrayList<>();

    @Override
    protected void initInjector() {
        DaggerCompleteComponent.create().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_daily_list;
    }

    @Override
    public void initViews() {
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    @Override
    public void initData() {
        mUserModel.queryOrder(getContext()).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                try {
                    Log.e("zc", "ResponseBody:"+responseBody.string());
                    String data = responseBody.string();
                    JSONObject jsonObject = new JSONObject(data);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {//请求成功
                        List<Order> entity = JsonUtils.toEntity(data, new TypeToken<List<Order>>(){}.getType());
                        if (entity!=null&&entity.size()>0){//有订单数据
                            mOrderLists.addAll(entity);
                        }else {
                            ToastUtils.showToast("暂无数据！");

                        }
                    } else {//请求失败
                        String errorMsg = jsonObject.optString("message");
                        Log.e("zc", "ErrorMsg:" + errorMsg);
                        ToastUtils.showToast("获取订单列表失败！" + errorMsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                Log.e("zc", "Throwable:" + e.getMessage());
            }
        });
        RetrofitHelper.builder().getLatestNews()
                .compose(bindToLifecycle())
                .map(this::changeReadState)
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(Rxutils.normalSchedulers())
                .subscribe(dailyListBean -> {

                    LogUtil.all(dailyListBean.getStories().get(0).getTitle());
                    //mAdapter.updateData(dailyListBean.getStories());
                    currentTime = dailyListBean.getDate();
                    if (dailyListBean.getStories().size() < 8) {
                        loadMoreDaily(DailyFragment.this.currentTime);
                    }

                    top_stories = dailyListBean.getTop_stories();
                    stories.addAll(dailyListBean.getStories());
                    finishTask();
                }, throwable -> {

                    LogUtil.all("加载失败" + throwable.getMessage());
                });
    }


    private void initRecyclerView() {
//        mAdapter = new DailyListAdapter(getActivity(), stories);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAutoLoadOnScrollListener = new AutoLoadOnScrollListener(mLinearLayoutManager) {

            @Override
            public void onLoadMore(int currentPage) {

//                loadMoreDaily(currentTime);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                mSwipeRefreshLayout.setEnabled(
                        mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        };
        mRecyclerView.addOnScrollListener(mAutoLoadOnScrollListener);

    }


    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.black_90);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }


    private void finishTask() {

        mSwipeRefreshLayout.setRefreshing(false);

//        Observable.from(top_stories)
//                .forEach(topDailys -> banners.add(new BannerEntity(topDailys.getId(),
//                        topDailys.getTitle(), topDailys.getImage())));
//
//        mBannerView.delayTime(5).build(banners);
//        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
//        mAdapter.notifyDataSetChanged();
    }


    private void loadMoreDaily(final String currentTime) {
//
//        RetrofitHelper.builder().getBeforeNews(currentTime)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(this::changeReadState)
//                .subscribe(dailyListBean -> {
//
//                    mAutoLoadOnScrollListener.setLoading(false);
//                    mAdapter.addData(dailyListBean.getStories());
//                    DailyFragment.this.currentTime = dailyListBean.getDate();
//                }, throwable -> {
//
//                    mAutoLoadOnScrollListener.setLoading(false);
//                    LogUtil.all("加载更多数据失败");
//                });
    }

}

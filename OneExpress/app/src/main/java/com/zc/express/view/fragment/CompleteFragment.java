package com.zc.express.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zc.express.R;
import com.zc.express.view.widget.AutoLoadOnScrollListener;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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


    public static CompleteFragment newInstance() {

        return new CompleteFragment();
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
//        RetrofitHelper.builder().getLatestNews()
//                .compose(bindToLifecycle())
//                .map(this::changeReadState)
//                .delay(1000, TimeUnit.MILLISECONDS)
//                .compose(Rxutils.normalSchedulers())
//                .subscribe(dailyListBean -> {
//
//                    LogUtil.all(dailyListBean.getStories().get(0).getTitle());
//                    //mAdapter.updateData(dailyListBean.getStories());
//                    currentTime = dailyListBean.getDate();
//                    if (dailyListBean.getStories().size() < 8) {
//                        loadMoreDaily(DailyFragment.this.currentTime);
//                    }
//
//                    top_stories = dailyListBean.getTop_stories();
//                    stories.addAll(dailyListBean.getStories());
//                    finishTask();
//                }, throwable -> {
//
//                    LogUtil.all("加载失败" + throwable.getMessage());
//                });
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

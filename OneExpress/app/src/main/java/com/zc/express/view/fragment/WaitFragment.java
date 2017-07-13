package com.zc.express.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zc.express.R;
import com.zc.express.bean.MainOrderList;
import com.zc.express.bean.Order;
import com.zc.express.bean.User;
import com.zc.express.bean.WaitOrder;
import com.zc.express.model.UserModel;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.adapter.DailyListAdapter;
import com.zc.express.view.adapter.WaitOrderAdapter;
import com.zc.express.view.widget.AutoLoadOnScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
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
 * 待完成订单
 * Created by ZC on 2017/7/11.
 */

public class WaitFragment extends BaseFragment {

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

    private List<WaitOrder> mOrderLists = new ArrayList<>();
    private WaitOrderAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

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
//        mUserModel.setPushId(getContext()).subscribe(new Action1<ResponseBody>() {
//            @Override
//            public void call(ResponseBody responseBody) {
//                String data = null;
//                try {
//                    data = responseBody.string();
//                    Log.e("zc1", "ResponseBody:" + data);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable e) {
//                Log.e("zc", "Throwable:" + e.getMessage());
//            }
//        });
        mUserModel.getWaitOrder(getContext()).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                mSwipeRefreshLayout.setRefreshing(false);
                try {
                    String data = responseBody.string();
                    Log.e("zc1", "ResponseBody:" + data);
                    JSONArray jsonArray = new JSONArray(data);
                    if (jsonArray != null) {
                        List<WaitOrder> entity = JsonUtils.toEntity(data, new TypeToken<List<WaitOrder>>() {
                        }.getType());
                        if (entity != null && entity.size() > 0) {//有订单数据
                            mOrderLists.addAll(entity);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showToast("暂无数据！");
                        }
                    } else {//请求失败
                        JSONObject jsonObject = new JSONObject(data);
                        final String errorMsg = jsonObject.optString("message");
                        Log.e("zc", "ErrorMsg:" + errorMsg);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast("获取订单列表失败！" + errorMsg);
                            }
                        });

                    }
                } catch (IOException e) {
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
                Log.e("zc", "Throwable:" + e.getMessage());
            }
        });

    }

    //刷新病历
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(Order event) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }


    private void initRecyclerView() {
        mAdapter = new WaitOrderAdapter(getActivity(), mOrderLists);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        mAutoLoadOnScrollListener = new AutoLoadOnScrollListener(mLinearLayoutManager) {
//
//            @Override
//            public void onLoadMore(int currentPage) {
//
////                loadMoreDaily(currentTime);
//            }
//
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                super.onScrolled(recyclerView, dx, dy);
//
//                mSwipeRefreshLayout.setEnabled(
//                        mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
//            }
//        };
//        mRecyclerView.addOnScrollListener(mAutoLoadOnScrollListener);
        mRecyclerView.setAdapter(mAdapter);
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
                if (mOrderLists != null) {
                    mOrderLists.clear();
                }
                initData();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

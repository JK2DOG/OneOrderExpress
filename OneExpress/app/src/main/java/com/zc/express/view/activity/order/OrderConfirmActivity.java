package com.zc.express.view.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.Order;
import com.zc.express.bean.Package;
import com.zc.express.bean.User;
import com.zc.express.model.UserModel;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.activity.home.MainActivity;
import com.zc.express.view.adapter.PackageAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * 确认订单界面
 * Created by ZC on 2017/7/13.
 */

public class OrderConfirmActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private LinearLayoutManager mLinearLayoutManager;


    @Inject
    RxSubscriptionCollection mRxSubscriptionCollection;
    @Inject
    User mUser;
    @Inject
    UserModel mUserModel;

    private List<Package> mPackageLists = new ArrayList<>();
    private PackageAdapter mAdapter;
    private Order mOrder;


    public static void start(Context context, Order entity) {
        Intent intent = new Intent(context, OrderConfirmActivity.class);
        intent.putExtra("entity", entity);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_order_confirm;
    }

    @Override
    protected void initInjector() {
        DaggerOrderComponent.create().inject(this);
    }

    @Override
    protected void initViews() {
        mOrder = getIntent().getParcelableExtra("entity");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText("包裹列表");
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        mAdapter = new PackageAdapter(OrderConfirmActivity.this, mPackageLists);
        mLinearLayoutManager = new LinearLayoutManager(OrderConfirmActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mPackageLists.addAll(mOrder.getPackages());
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_confirm)
    void onClick() {
        showProgress();
        if (mPackageLists != null && mPackageLists.size() > 0) {
            List<Package> mList = new ArrayList<>();
            for (Package entity : mPackageLists) {
                int value = entity.getValue();
                entity.setValue(value + 1);
                mList.add(entity);
            }
            mOrder.setPackages(mList);
            mRxSubscriptionCollection.add(mUserModel.recheckOrder(OrderConfirmActivity.this, mOrder).subscribe(new Action1<Response<ResponseBody>>() {
                @Override
                public void call(Response<ResponseBody> response) {
                    Log.e("retrofit", response.headers().toString());
                    try {
                        Log.e("retrofit", response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dissmissProgress();
                    if (response.isSuccessful()) {
                        String data = null;
                        try {
                            data = response.body().string();
                            Log.e("retrofit", data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Context context = OrderConfirmActivity.this;
                        context.startActivity(new Intent(context, MainActivity.class));
                        finish();
                    } else {
                        ToastUtils.showToast("ERROR:" + response.code());
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable e) {
                    dissmissProgress();
                    Log.e("zc", "Throwable:" + e.getMessage());
                }
            }));
        }


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

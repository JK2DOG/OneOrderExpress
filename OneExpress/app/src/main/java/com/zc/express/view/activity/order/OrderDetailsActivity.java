package com.zc.express.view.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zc.express.R;
import com.zc.express.bean.Order;
import com.zc.express.bean.PushOrder;
import com.zc.express.data.preference.ObjectPreference;
import com.zc.express.model.UserModel;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;
import com.zc.express.view.widget.EmptyLayout;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * 订单详情页面
 * Created by ZC on 2017/7/10.
 */

public class OrderDetailsActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @BindView(R.id.tv_create_time)
    TextView mCreateTimeTv;//创建时间

    @BindView(R.id.tv_departure)
    TextView mDpartureTv;//起点

    @BindView(R.id.tv_destination)
    TextView mDestinationTv;//终点

    @BindView(R.id.tv_stauts)
    TextView mStautsTv;//状态

    @BindView(R.id.tv_final_price)
    TextView mPriceTv;//价格

    @BindView(R.id.tv_commodity_item_name)
    TextView mNameTv;//名字

    @BindView(R.id.tv_carrier)
    TextView mCarrierTv;//快递公司

    @BindView(R.id.tv_eship_service_name)
    TextView mServiceNameTv;//快递公司

    @BindView(R.id.cv_confirm)
    CardView mConfirmCv;//确认收货


    @Inject
    UserModel mUserModel;
    @Inject
    RxSubscriptionCollection mSubscriptionCollection;

    private String oid;
    private boolean isConfirm;
    private boolean isPush;
    private Order mOrder;

    public static void start(Context context, String oid, boolean isConfirm, boolean isPush) {
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra("oid", oid);
        intent.putExtra("isConfirm", isConfirm);
        intent.putExtra("isPush", isPush);
        context.startActivity(intent);
    }

    //    create_time, destination, departure, stauts, final_price, commodity_item_name, carrier, eship_service_name
    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void initInjector() {
        DaggerOrderComponent.create().inject(this);
    }

    @Override
    protected void initViews() {
        oid = getIntent().getStringExtra("oid");
        isConfirm = getIntent().getBooleanExtra("isConfirm", false);
        isPush = getIntent().getBooleanExtra("isPush", false);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mConfirmCv.setVisibility(isConfirm ? View.VISIBLE : View.GONE);
        mTitleTv.setText("订单详情");
        showLoading();
        getData();

    }

    private void getData() {
        mSubscriptionCollection.add(mUserModel.getOrderDetails(OrderDetailsActivity.this, oid).subscribe(new Action1<Response<ResponseBody>>() {
            @Override
            public void call(Response<ResponseBody> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    String data = null;
                    try {
                        data = response.body().string();
                        Log.e("retrofit", data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Order entity = JsonUtils.toEntity(data, new TypeToken<Order>() {
                    }.getType());
                    if (entity != null) {
                        mOrder = entity;
                        String time = entity.getCreate_time();
                        String times[] = time.split("T");
                        mCreateTimeTv.setText(times[0]);
                        mDpartureTv.setText(entity.getDeparture());
                        mDestinationTv.setText(entity.getDestination());
//                        mStautsTv.setText(entity.getStatus().equals("created")?"创建成功":"666");
                        mStautsTv.setText(entity.getStatus());
                        mPriceTv.setText("¥ " + entity.getFinal_price());
                        mNameTv.setText(entity.getCommodity_item_name());
                        mCarrierTv.setText(entity.getCarrier());
                        mServiceNameTv.setText(entity.getEship_service_name());
                    }
                } else {
                    ToastUtils.showToast("ERROR:" + response.code());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                showNetError(new EmptyLayout.OnRetryListener() {
                    @Override
                    public void onRetry() {

                    }
                });
                ToastUtils.showToast(e.getMessage());
                Log.e("zc", "Throwable:" + e.getMessage());
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptionCollection.cancelAll();
    }


    @OnClick(R.id.cv_confirm)
    void onClick() {
        if (mOrder != null) {
            mSubscriptionCollection.add(mUserModel.robOrder(OrderDetailsActivity.this, mOrder.getId()).subscribe(new Action1<Response<ResponseBody>>() {
                @Override
                public void call(Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (isPush){
                                ObjectPreference.clearObject(OrderDetailsActivity.this, PushOrder.class);
                            }
                            String reDataString = response.body().string();
                            Log.e("retrofit", reDataString);
                            if (reDataString.equals("accepted")){
                                mUserModel.saveConfirmOrder(mOrder);
                                ToastUtils.showToast("抢单成功！");
                                Intent intent = new Intent(OrderDetailsActivity.this, OrderConfirmActivity.class);
                                startActivity(intent);
                            }
                            if (reDataString.equals("denied")){//小二已放弃
                                ToastUtils.showToast("denied！");
                            }
                            if (reDataString.equals("expired")){//单已过期
                                ToastUtils.showToast("expired！");
                            }
                            if (reDataString.equals("declined")){//后台拒绝
                                ToastUtils.showToast("declined！");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable e) {
                    Log.e("zc", "Throwable:" + e.getMessage());
                }
            }));

//            OrderConfirmActivity.start(OrderDetailsActivity.this, mOrder);
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

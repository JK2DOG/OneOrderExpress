package com.zc.express.view.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zc.express.R;
import com.zc.express.bean.MainOrderList;
import com.zc.express.bean.Order;
import com.zc.express.model.UserModel;
import com.zc.express.utils.JsonUtils;
import com.zc.express.utils.RxSubscriptionCollection;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import okhttp3.Response;
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

    @Inject
    UserModel mUserModel;
    @Inject
    RxSubscriptionCollection mSubscriptionCollection;

    private String oid;

    public  static void start(Context context,String oid){
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra("oid",oid);
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
        oid=getIntent().getStringExtra("oid");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText("订单详情");
        showProgress();
        mSubscriptionCollection.add(mUserModel.getOrderDetails(OrderDetailsActivity.this,oid).subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                dissmissProgress();
                if (response.code()==200){//请求成功
                    String data=response.body().toString();
                    Log.e("retrofit",data);
                    Order entity = JsonUtils.toEntity(data, new TypeToken<List<Order>>() {
                    }.getType());
                    if (entity!=null){
                        String time = entity.getCreate_time();
                        String times[] = time.split("T");
                        mCreateTimeTv.setText(times[0]);
                        mDpartureTv.setText(entity.getDeparture());
                        mDestinationTv.setText(entity.getDestination());
                        mStautsTv.setText(entity.getStatus().equals("created")?"创建成功":"666");
                        mPriceTv.setText("¥ " + entity.getFinal_price());
                        mNameTv.setText(entity.getCommodity_item_name());
                        mCarrierTv.setText(entity.getCarrier());
                        mServiceNameTv.setText(entity.getEship_service_name());
                    }
                }else {
                    ToastUtils.showToast(response.message());
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                dissmissProgress();
                ToastUtils.showToast(e.getMessage());
                Log.e("zc", "Throwable:" + e.getMessage());
            }
        }));
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

package com.zc.express.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.MainOrderList;
import com.zc.express.bean.WaitOrder;
import com.zc.express.view.activity.order.OrderDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 未完成订单
 * Created by ZC on 2017/7/12.
 */

public class WaitOrderAdapter extends RecyclerView.Adapter<WaitOrderAdapter.ItemContentViewHolder> {


    private List<WaitOrder> orders;

    private Context mContext;


    public WaitOrderAdapter(Context context, List<WaitOrder> orders) {

        this.orders = orders;
        this.mContext = context;
    }


    @Override
    public ItemContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemContentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false));
    }


    @Override
    public void onBindViewHolder(ItemContentViewHolder holder, int position) {
        WaitOrder order = orders.get(position);
        if (order == null) {
            return;
        }
        setDailyDate(holder, order);
    }


    /**
     * 设置数据给普通内容Item
     */
    private void setDailyDate(final ItemContentViewHolder holder, final WaitOrder order) {
//        2017-06-23T04:21:49.000+0000
        holder.mOrderId.setText(order.getOrderId());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsActivity.start(mContext,order.getOrderId(),true);
            }
        });
    }


    // public void updateData(List<DailyListBean.StoriesBean> stories) {
    //
    //   this.stories = stories;
    //   notifyDataSetChanged();
    // }


    public void addData(List<WaitOrder> orders) {
        this.orders.addAll(orders);
        notifyDataSetChanged();
        //
        // if (this.stories == null) {
        //   updateData(stories);
        // } else {
        //   this.stories.addAll(stories);
        //   notifyDataSetChanged();
        // }
    }


    @Override
    public int getItemCount() {

        return orders.size() == 0 ? 0 : orders.size();
    }


    public List<WaitOrder> getmDailyList() {

        return orders;
    }


    class ItemContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView mLayout;

        @BindView(R.id.tv_id)
        TextView mOrderId;


        ItemContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

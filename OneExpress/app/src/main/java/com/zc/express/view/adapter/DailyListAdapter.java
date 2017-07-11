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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单列表Adapter
 */
public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.ItemContentViewHolder> {


    private List<MainOrderList> orders;

    private Context mContext;


    public DailyListAdapter(Context context, List<MainOrderList> orders) {

        this.orders = orders;
        this.mContext = context;
    }


    @Override
    public ItemContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemContentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_list, parent, false));
    }


    @Override
    public void onBindViewHolder(ItemContentViewHolder holder, int position) {
        MainOrderList order = orders.get(position);
        if (order == null) {
            return;
        }
        setDailyDate(holder, order);
    }


    /**
     * 设置数据给普通内容Item
     */
    private void setDailyDate(final ItemContentViewHolder holder, final MainOrderList order) {
//        2017-06-23T04:21:49.000+0000
        holder.mOrderId.setText(order.getOrder_id());
        holder.mOrderStatus.setText((order.isAccept_decision() && order.isAccept_result()) ? "已完成" : "666");
        String time = order.getOrder_date();
        String times[] = time.split("T");
        holder.mOrderCreateTime.setText(times[0]);
//        holder.tv_eship_service_name.setText(order.getEship_service_name());
//        holder.tv_commodity_item_name.setText(order.getCommodity_item_name());
        holder.tv_commodity_item_name.setText(order.getCurrency());
//        holder.tv_where_to.setText(order.getDeparture() + " - " + order.getDestination());
        holder.tv_final_price.setText("¥ " + order.getFinal_price());

    }


    // public void updateData(List<DailyListBean.StoriesBean> stories) {
    //
    //   this.stories = stories;
    //   notifyDataSetChanged();
    // }


    public void addData(List<MainOrderList> orders) {
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


    public List<MainOrderList> getmDailyList() {

        return orders;
    }


    class ItemContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView mLayout;

        @BindView(R.id.tv_id)
        TextView mOrderId;

        @BindView(R.id.tv_status)
        TextView mOrderStatus;

        @BindView(R.id.tv_createtime)
        TextView mOrderCreateTime;

        @BindView(R.id.tv_eship_service_name)
        TextView tv_eship_service_name;
        @BindView(R.id.tv_commodity_item_name)
        TextView tv_commodity_item_name;
        @BindView(R.id.tv_where_to)
        TextView tv_where_to;
        @BindView(R.id.tv_final_price)
        TextView tv_final_price;


        ItemContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

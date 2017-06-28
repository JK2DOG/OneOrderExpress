package com.zc.express.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.Order;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 订单列表Adapter
 */
public class DailyListAdapter extends RecyclerView.Adapter<DailyListAdapter.ItemContentViewHolder> {


    private List<Order> orders;

    private Context mContext;


    public DailyListAdapter(Context context, List<Order> orders) {

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
        Order order = orders.get(position);
        if (order == null) {
            return;
        }
            setDailyDate(holder, storiesBean);
    }


    /**
     * 设置数据给普通内容Item
     */
    private void setDailyDate(final ItemContentViewHolder holder, final DailyInfo.StoriesBean storiesBean) {

        holder.mTitle.setText(storiesBean.getTitle());
        List<String> images = storiesBean.getImages();
        if (images != null && images.size() > 0) {
            Glide.with(mContext)
                    .load(images.get(0))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.account_avatar)
                    .into(holder.mPic);
        }
        boolean multipic = storiesBean.isMultipic();
        if (multipic) {

            holder.mMorePic.setVisibility(View.VISIBLE);
        } else {
            holder.mMorePic.setVisibility(View.GONE);
        }
        if (!storiesBean.isRead()) {
            holder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_unread));
        } else {
            holder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_read));
        }
        holder.mLayout.setOnClickListener(v -> {

            if (!storiesBean.isRead()) {
                storiesBean.setRead(true);
                holder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_read));
                new Thread(() -> {

                    //mDailyDao.insertReadNew(storiesBean.getId() + "");
                }).start();
            }
            //跳转到详情界面
            DailyDetailsActivity.lanuch(mContext, storiesBean.getId());
        });
    }


    // public void updateData(List<DailyListBean.StoriesBean> stories) {
    //
    //   this.stories = stories;
    //   notifyDataSetChanged();
    // }


    public void addData(List<DailyInfo.StoriesBean> stories) {
        this.stories.addAll(stories);
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


    public List<Order> getmDailyList() {

        return orders;
    }


    class ItemContentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.card_view)
        CardView mLayout;

        @Bind(R.id.item_image)
        ImageView mPic;

        @Bind(R.id.item_title)
        TextView mTitle;

        @Bind(R.id.item_more_pic)
        ImageView mMorePic;


        ItemContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

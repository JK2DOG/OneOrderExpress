package com.zc.express.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.Package;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 包裹适配器
 * Created by ZC on 2017/7/13.
 */

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ItemContentViewHolder> {


    private List<Package> packages;

    private Context mContext;


    public PackageAdapter(Context context, List<Package> packages) {

        this.packages = packages;
        this.mContext = context;
    }


    @Override
    public ItemContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemContentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package, parent, false));
    }


    @Override
    public void onBindViewHolder(ItemContentViewHolder holder, int position) {
        Package entity= packages.get(position);
        if (entity == null) {
            return;
        }
        setDailyDate(holder, entity);
    }


    /**
     * 设置数据给普通内容Item
     */
    private void setDailyDate(final ItemContentViewHolder holder, final Package entity) {
//        2017-06-23T04:21:49.000+0000
//        holder.mOrderId.setText(entity.getOrderId());
        holder.mPackageName.setText(entity.getDescription());
        holder.mLengthTv.setText(entity.getLength()+"");
        holder.mWidthTv.setText(entity.getWidth()+"");
        holder.mHeightTv.setText(entity.getHeight()+"");
        holder.mWeightTv.setText(entity.getWeight()+"");
        holder.mValueTv.setText(entity.getValue()+"");
    }





    public void addData(List<Package> packages) {
        this.packages.addAll(packages);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {

        return packages.size() == 0 ? 0 : packages.size();
    }


    public List<Package> getmDailyList() {

        return packages;
    }


    class ItemContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView mLayout;

        @BindView(R.id.tv_name)
        TextView mPackageName;
        @BindView(R.id.tv_length)
        TextView mLengthTv;
        @BindView(R.id.tv_width)
        TextView mWidthTv;
        @BindView(R.id.tv_height)
        TextView mHeightTv;
        @BindView(R.id.tv_weight)
        TextView mWeightTv;
        @BindView(R.id.tv_value)
        TextView mValueTv;


        ItemContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

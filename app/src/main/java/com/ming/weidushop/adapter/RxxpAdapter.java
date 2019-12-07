package com.ming.weidushop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.CommodityDetailsActivity;
import com.ming.weidushop.bean.IndexListBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/4/19
 * 热销新品
 */
public class RxxpAdapter extends RecyclerView.Adapter<RxxpAdapter.RxxpViewHolder> {

    private Context context;
    private List<IndexListBean.ResultBean.RxxpBean.CommodityListBean> commodityList = new ArrayList<>();

    public RxxpAdapter(Context context, List<IndexListBean.ResultBean.RxxpBean.CommodityListBean> commodityList) {
        this.context = context;
        this.commodityList = commodityList;
    }

    @NonNull
    @Override
    public RxxpViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.adapter_rxxp, null);
        RxxpViewHolder rxxpViewHolder = new RxxpViewHolder(view);
        return rxxpViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RxxpViewHolder rxxpViewHolder, final int i) {
        //rxxpViewHolder.mImage.setImageURI(commodityList.get(i).getMasterPic());
        Glide.with(context).load(commodityList.get(i).getMasterPic())
                .into( rxxpViewHolder.mImage);
        rxxpViewHolder.mTItle.setText(commodityList.get(i).getCommodityName());
        rxxpViewHolder.mPrice.setText("¥"+commodityList.get(i).getPrice()+"");
        rxxpViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Integer> map=new HashMap<>();
                map.put("id",commodityList.get(i).getCommodityId());
                AppUtils.startInt(context,CommodityDetailsActivity.class,map);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commodityList.size();
    }

    public class RxxpViewHolder extends RecyclerView.ViewHolder {
        TextView mTItle, mPrice;
        ImageView mImage;

        public RxxpViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.imageview);
            mTItle = (TextView) itemView.findViewById(R.id.tv_title);
            mPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}

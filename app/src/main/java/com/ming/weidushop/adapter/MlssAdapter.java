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
 * 魔力时尚
 */
public class MlssAdapter extends RecyclerView.Adapter<MlssAdapter.MlssViewHolder> {

    private Context context;
    private List<IndexListBean.ResultBean.MlssBean.CommodityListBeanXX> commodityList = new ArrayList<>();

    public MlssAdapter(Context context, List<IndexListBean.ResultBean.MlssBean.CommodityListBeanXX> commodityList) {
        this.context = context;
        this.commodityList = commodityList;
    }

    @NonNull
    @Override
    public MlssViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.adapter_mlss, null);
        MlssViewHolder mlssViewHolder = new MlssViewHolder(view);
        return mlssViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MlssViewHolder mlssViewHolder, final int i) {
       // mlssViewHolder.mImage.setImageURI(commodityList.get(i).getMasterPic());
        Glide.with(context).load(commodityList.get(i).getMasterPic())
                .into( mlssViewHolder.mImage);
        mlssViewHolder.mTItle.setText(commodityList.get(i).getCommodityName());
        mlssViewHolder.mPrice.setText("¥" + commodityList.get(i).getPrice());
        mlssViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Integer> map = new HashMap<>();
                map.put("id", commodityList.get(i).getCommodityId());
                AppUtils.startInt(context, CommodityDetailsActivity.class, map);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commodityList.size();
    }

    public class MlssViewHolder extends RecyclerView.ViewHolder {
        TextView mTItle, mPrice;
        ImageView mImage;

        public MlssViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.imageview);
            mTItle = (TextView) itemView.findViewById(R.id.tv_title);
            mPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}

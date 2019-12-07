package com.ming.weidushop.adapter;

import android.content.Context;
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
 * 品质生活
 */
public class PzshAdapter extends RecyclerView.Adapter<PzshAdapter.PzshViewHolder> {

    private Context context;
    private List<IndexListBean.ResultBean.PzshBean.CommodityListBeanX> commodityList = new ArrayList<>();

    public PzshAdapter(Context context, List<IndexListBean.ResultBean.PzshBean.CommodityListBeanX> commodityList) {
        this.context = context;
        this.commodityList = commodityList;
    }

    @NonNull
    @Override
    public PzshViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.adapter_pzsh, null);
        PzshViewHolder pzshViewHolder = new PzshViewHolder(view);
        return pzshViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PzshViewHolder pzshViewHolder, final int i) {
//        pzshViewHolder.mImage.setImageURI(commodityList.get(i).getMasterPic());
        Glide.with(context).load(commodityList.get(i).getMasterPic())
                .into( pzshViewHolder.mImage);
        pzshViewHolder.mTItle.setText(commodityList.get(i).getCommodityName());
        pzshViewHolder.mPrice.setText("¥" + commodityList.get(i).getPrice() + "");
        pzshViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public class PzshViewHolder extends RecyclerView.ViewHolder {
        TextView mTItle, mPrice;
        ImageView mImage;

        public PzshViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.imageview);
            mTItle = (TextView) itemView.findViewById(R.id.tv_title);
            mPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}

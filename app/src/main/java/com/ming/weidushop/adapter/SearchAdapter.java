package com.ming.weidushop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.CommodityDetailsActivity;
import com.ming.weidushop.bean.SearchBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/6
 */
public class SearchAdapter extends BaseRecyclerAdapter<SearchBean.ResultBean> {
    public SearchAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_search;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, final SearchBean.ResultBean resultBean) {
        baseViewHolder.setText(R.id.tv_title, resultBean.getCommodityName());
        baseViewHolder.setText(R.id.tv_price, "¥" + resultBean.getPrice());
        baseViewHolder.setText(R.id.tv_num,"已售"+resultBean.getSaleNum()+"件");
        ImageView simpleDraweeView = (ImageView) baseViewHolder.get(R.id.imageview);
        AppUtils.setGlide(context, resultBean.getMasterPic(), simpleDraweeView);
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Integer> map = new HashMap<>();
                map.put("id", resultBean.getCommodityId());
                AppUtils.startInt(context, CommodityDetailsActivity.class, map);
            }
        });
    }
}

package com.ming.weidushop.adapter;

import android.content.Context;
import android.view.View;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.utils.DateUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.CommodityDetailsActivity;
import com.ming.weidushop.bean.FootBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/4
 * 我的足迹适配器
 */
public class FootAdapter extends BaseRecyclerAdapter<FootBean.ResultBean> {

    public FootAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_foot;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, final FootBean.ResultBean resultBean) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) baseViewHolder.get(R.id.imageview);
        simpleDraweeView.setImageURI(resultBean.getMasterPic());
        baseViewHolder.setText(R.id.tv_title, resultBean.getCommodityName());
        baseViewHolder.setText(R.id.tv_price, "¥" + resultBean.getPrice());
        baseViewHolder.setText(R.id.tv_num, "已浏览" + resultBean.getBrowseNum() + "次");
        baseViewHolder.setText(R.id.tv_time, DateUtils.getTime(resultBean.getBrowseTime()));
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

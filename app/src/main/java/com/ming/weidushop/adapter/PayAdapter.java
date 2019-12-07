package com.ming.weidushop.adapter;

import android.content.Context;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.utils.DateUtils;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.PayBean;

/**
 * author:AbnerMing
 * date:2019/9/10
 */
public class PayAdapter extends BaseRecyclerAdapter<PayBean.ResultBean.DetailListBean> {
    public PayAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_pay;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, PayBean.ResultBean.DetailListBean detailListBean) {
        baseViewHolder.setText(R.id.tv_money,"¥"+detailListBean.getAmount());
        baseViewHolder.setText(R.id.tv_time, DateUtils.getYMDTime(detailListBean.getConsumerTime()));
    }


}

package com.ming.weidushop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.utils.Logger;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.CommentActivity;
import com.ming.weidushop.bean.OrderAllBean;
import com.ming.weidushop.utils.AppUtils;
import com.ming.weidushop.view.ShopAddView;

/**
 * author:AbnerMing
 * date:2019/9/9
 */
public class OrderAllItemAdapter extends BaseRecyclerAdapter<OrderAllBean.OrderListBean.DetailListBean> {
    private int mChangeState;

    public OrderAllItemAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_order_all_child_item;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, final OrderAllBean.OrderListBean.DetailListBean detailListBean) {
        ImageView simpleDraweeView = (ImageView) baseViewHolder.get(R.id.order_image);
        Logger.i("bindViewData", detailListBean.getCommodityPic());
        String pic = detailListBean.getCommodityPic();
        AppUtils.setGlide(context, pic.split(",")[0], simpleDraweeView);
        baseViewHolder.setText(R.id.order_title, detailListBean.getCommodityName());
        baseViewHolder.setText(R.id.order_price, "¥" + detailListBean.getCommodityPrice());
        ShopAddView mShopAddView = (ShopAddView) baseViewHolder.get(R.id.order_add);
        TextView mPj = (TextView) baseViewHolder.get(R.id.tv_pj);
        if (mChangeState == 2 || mChangeState == 3 || mChangeState == 9) {
            mShopAddView.setVisibility(View.GONE);
        } else {
            mShopAddView.setCount(detailListBean.getCommodityCount());
            mShopAddView.setVisibility(View.VISIBLE);
        }
        mShopAddView.setEditCancle();
        if (mChangeState == 3 || AppUtils.mOrderPosoition == 3) {
            mPj.setVisibility(View.VISIBLE);
            mPj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailListBean.setOrderId(mOrderId);
                    AppUtils.mOrderPosoition = 3;
                    Logger.i("====2", AppUtils.mOrderPosoition + "");
                    AppUtils.startObject(context, CommentActivity.class, detailListBean);
                }
            });
        } else {
            mPj.setVisibility(View.GONE);
        }

    }

    public void setDataState(int mChangeState) {
        this.mChangeState = mChangeState;
    }

    private String mOrderId;

    public void setOrderId(String id) {
        mOrderId = id;
    }
}

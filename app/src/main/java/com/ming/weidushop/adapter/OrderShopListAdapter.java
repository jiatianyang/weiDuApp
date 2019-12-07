package com.ming.weidushop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.ShopCarBean;

/**
 * author:AbnerMing
 * date:2019/9/15
 */
public class OrderShopListAdapter extends BaseRecyclerAdapter<ShopCarBean.ResultBean.ShoppingCartListBean> {
    public OrderShopListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_order_shop_list_item;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder,
                             ShopCarBean.ResultBean.ShoppingCartListBean shoppingCartListBean) {
        baseViewHolder.setText(R.id.order_title, shoppingCartListBean.getCommodityName());
        baseViewHolder.setText(R.id.order_price, "¥" + shoppingCartListBean.getPrice());
        ImageView mImage = (ImageView) baseViewHolder.get(R.id.order_image);
        Glide.with(context).load(shoppingCartListBean.getPic()).into(mImage);


    }

}

package com.ming.weidushop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.ShopCarBean;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/6
 */
public class OrderShopCarAdapter extends BaseRecyclerAdapter<ShopCarBean.ResultBean> {
    private Context context;

    public OrderShopCarAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_shopcar_order;
    }


    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, ShopCarBean.ResultBean resultBean) {

        baseViewHolder.setText(R.id.shop_car_name, resultBean.getCategoryName());
        SwipeRecyclerView mRecyclerView = (SwipeRecyclerView) baseViewHolder.get(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        OrderShopListAdapter shopCarItemAdapter = new OrderShopListAdapter(context);
        mRecyclerView.setAdapter(shopCarItemAdapter);

        List<ShopCarBean.ResultBean.ShoppingCartListBean> shoppingCartList = resultBean.getShoppingCartList();
        List<ShopCarBean.ResultBean.ShoppingCartListBean> shoppingCartListTemp = new ArrayList<>();
        for (int i = 0; i < shoppingCartList.size(); i++) {
            ShopCarBean.ResultBean.ShoppingCartListBean shoppingCartListBean = shoppingCartList.get(i);
            if (shoppingCartListBean.isSelected()) {
                shoppingCartListTemp.add(shoppingCartListBean);
            }
        }
        shopCarItemAdapter.setList(shoppingCartListTemp);

    }


}

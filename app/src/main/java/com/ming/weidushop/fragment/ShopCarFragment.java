package com.ming.weidushop.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abner.ming.base.BaseFragment;
import com.abner.ming.base.model.Api;
import com.ming.weidushop.activity.MainActivity;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.ShopCarAdapter;
import com.ming.weidushop.bean.ShopCarBean;
import com.ming.weidushop.utils.AppUtils;
import com.ming.weidushop.view.LayoutDataNull;

import java.util.ArrayList;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/4
 * 购物车
 */
public class ShopCarFragment extends BaseFragment {
    private final static String TAG = ShopCarFragment.class.getName();
    private RecyclerView mRecyclerView;
    private ShopCarAdapter mShopCarAdapter;
    private List<ShopCarBean.ResultBean> mShopCarList = new ArrayList<>();
    private LayoutDataNull mLayoutNull;


    @Override
    protected void initData() {
        mShopCarAdapter = new ShopCarAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mShopCarAdapter);
        mShopCarAdapter.setOnChangeDataListener(new ShopCarAdapter.OnChangeDataListener() {
            @Override
            public void changeData(List<ShopCarBean.ResultBean> list) {
                float allPrice = 0;
                int allNum = 0;
                int allListSize = 0;
                for (int i = 0; i < list.size(); i++) {
                    List<ShopCarBean.ResultBean.ShoppingCartListBean> listBean =
                            list.get(i).getShoppingCartList();
                    allListSize = allListSize + listBean.size();
                    for (int j = 0; j < listBean.size(); j++) {
                        if (listBean.get(j).isSelected()) {
                            ShopCarBean.ResultBean.ShoppingCartListBean bean = listBean.get(j);
                            int count = bean.getCount();
                            float price = bean.getPrice();
                            allPrice = allPrice + (count * price);
                            allNum++;
                        }
                    }
                }
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).setAllPrice(allPrice);
                    ((MainActivity) getActivity()).setShopList(list);
                    if (allNum == allListSize) {//全选
                        ((MainActivity) getActivity()).setAllNum(true);
                    } else {
                        ((MainActivity) getActivity()).setAllNum(false);
                    }
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppUtils.getUserInFo() == null) {
            mLayoutNull.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mLayoutNull.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            initHeadMap();
            net(false, false, ShopCarBean.class)
                    .get(0, Api.QUERY_CAR_URL, null);
        }

    }


    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            int shopListSize = 0;
            ShopCarBean shopCarBean = (ShopCarBean) o;
            if ("0000".equals(shopCarBean.getStatus())) {
                mShopCarList = shopCarBean.getResult();
                mShopCarAdapter.setList(mShopCarList);
                shopListSize = mShopCarList.size();
            } else {
                shopListSize = 0;
            }

            if (shopListSize != 0) {
                mLayoutNull.setVisibility(View.GONE);
            } else {
                mLayoutNull.setVisibility(View.VISIBLE);
            }

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).changeShopList(shopListSize);
                ((MainActivity) getActivity()).setShopList(mShopCarList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) get(R.id.recycler);
        mLayoutNull = (LayoutDataNull) get(R.id.layout_null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shopcar;
    }


    //是否全选
    public void setAll(boolean isAll) {
        float allPrice = 0;
        for (int i = 0; i < mShopCarList.size(); i++) {
            List<ShopCarBean.ResultBean.ShoppingCartListBean> list =
                    mShopCarList.get(i).getShoppingCartList();
            for (int j = 0; j < list.size(); j++) {
                list.get(j).setSelected(isAll);
                ShopCarBean.ResultBean.ShoppingCartListBean bean = list.get(j);
                int count = bean.getCount();
                float price = bean.getPrice();
                allPrice = allPrice + (count * price);
            }
        }
        if (!isAll) {
            allPrice = 0;
        }
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setAllPrice(allPrice);
            ((MainActivity) getActivity()).setShopList(mShopCarList);
        }
        mShopCarAdapter.setList(mShopCarList);
    }


}

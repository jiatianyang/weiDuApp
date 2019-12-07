package com.ming.weidushop.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abner.ming.base.BaseFragment;
import com.abner.ming.base.refresh.material.Util;
import com.abner.ming.base.utils.Logger;
import com.abner.ming.base.utils.NetworkUtils;
import com.abner.ming.base.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.OrderFragmentAdapter;
import com.ming.weidushop.bean.OrderAllBean;
import com.ming.weidushop.bean.OrderHeadBean;
import com.ming.weidushop.utils.AppUtils;
import com.ming.weidushop.view.LayoutDataNull;

import java.util.ArrayList;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/8
 * 我的订单
 */
public class OrderFragment extends BaseFragment {
    private LinearLayout mLayoutHead;
    private List<OrderHeadBean> mOrderHeadBeanList = new ArrayList<>();
    private ViewPager mViewPager;
    private OrderFragmentAdapter mOrderFragmentAdapter;
    private LayoutDataNull mLayoutNull;

    @Override
    protected void initData() {
        initHeadData(0);
        mOrderFragmentAdapter =
                new OrderFragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mOrderFragmentAdapter);
        OrderAllFragment mOrderAllFragment = (OrderAllFragment) mOrderFragmentAdapter.getItem(0);
        mOrderAllFragment.setOnShopListListener(new OrderAllFragment.OnShopListListener() {
            @Override
            public void list(int size) {
                if (size > 0) {
                    isShowOrHint(false);
                } else {
                    isShowOrHint(true);
                }
            }

            @Override
            public void change(int state) {
                initHeadData(state);
            }
        });
    }

    private void initHeadData(int position) {
        mOrderHeadBeanList.clear();
        OrderHeadBean mOrderHeadBean = new OrderHeadBean();
        mOrderHeadBean.setName("全部订单");
        mOrderHeadBean.setImage(R.drawable.order_all);
        mOrderHeadBeanList.add(mOrderHeadBean);
        mOrderHeadBean = new OrderHeadBean();
        mOrderHeadBean.setName("待付款");
        mOrderHeadBean.setImage(R.drawable.order_pay);
        mOrderHeadBeanList.add(mOrderHeadBean);
        mOrderHeadBean = new OrderHeadBean();
        mOrderHeadBean.setName("待收货");
        mOrderHeadBean.setImage(R.drawable.order_receive);
        mOrderHeadBeanList.add(mOrderHeadBean);
        mOrderHeadBean = new OrderHeadBean();
        mOrderHeadBean.setName("待评价");
        mOrderHeadBean.setImage(R.drawable.order_comment);
        mOrderHeadBeanList.add(mOrderHeadBean);
        mOrderHeadBean = new OrderHeadBean();
        mOrderHeadBean.setName("已完成");
        mOrderHeadBean.setImage(R.drawable.shop_car_all);
        mOrderHeadBeanList.add(mOrderHeadBean);

        mLayoutHead.removeAllViews();
        for (int i = 0; i < mOrderHeadBeanList.size(); i++) {
            View view = View.inflate(getActivity(), R.layout.order_head, null);
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.order_image);
            TextView mText = (TextView) view.findViewById(R.id.order_desc);
            if (position == i) {
                mText.setTextColor(getResources().getColor(R.color.color_ff5));
            } else {
                mText.setTextColor(getResources().getColor(R.color.color666));
            }
            simpleDraweeView.setImageResource(mOrderHeadBeanList.get(i).getImage());
            mText.setText(mOrderHeadBeanList.get(i).getName());
            mLayoutHead.addView(view);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.weight = 1;
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            view.setLayoutParams(params);

            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderClick(finalI);
                }
            });

        }
    }

    //点击事件
    private void orderClick(int finalI) {
        AppUtils.mOrderPosoition = finalI;
        Logger.i("====3",AppUtils.mOrderPosoition+"");
        initHeadData(finalI);
        if (AppUtils.getUserInFo() == null && NetworkUtils.isConnected(getActivity())) {
            ToastUtils.show("暂未登录，请您登录后再查看！");
            return;
        }
        OrderAllFragment mOrderAllFragment = (OrderAllFragment) mOrderFragmentAdapter.getItem(0);
        mOrderAllFragment.changeOrderData(finalI);
    }

    @Override
    protected void initView(View view) {
        mLayoutHead = (LinearLayout) get(R.id.order_head);
        mViewPager = (ViewPager) get(R.id.view_pager);
        mLayoutNull = (LayoutDataNull) get(R.id.layout_null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void refreshData() {
        super.refreshData();
        // changeData();
        if (AppUtils.getUserInFo() == null) {
            mLayoutNull.setVisibility(View.VISIBLE);
        }
    }

    public void changeData() {
        Fragment fragment = mOrderFragmentAdapter.getItem(0);
        if (fragment instanceof OrderAllFragment) {
            ((OrderAllFragment) fragment).changeData();
        }
    }

    public void isShowOrHint(boolean isShow) {
        if (isShow) {
            mLayoutNull.setVisibility(View.VISIBLE);
        } else {
            mLayoutNull.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppUtils.mCommodityByOrder == 1) {
            orderClick(0);
        } else {
            Logger.i("====4",AppUtils.mOrderPosoition+"");
            initHeadData(AppUtils.mOrderPosoition);
        }
    }
}

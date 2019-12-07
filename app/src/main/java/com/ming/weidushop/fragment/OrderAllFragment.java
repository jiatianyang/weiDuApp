package com.ming.weidushop.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abner.ming.base.BaseFragment;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.utils.Logger;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.OrderAllAdapter;
import com.ming.weidushop.bean.OrderAllBean;
import com.ming.weidushop.utils.AppUtils;
import com.ming.weidushop.utils.BottomDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/9
 * 全部订单
 */
public class OrderAllFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private int mPager = 1;
    private OrderAllAdapter mOrderAllAdapter;
    private BottomDialog mBottomDialog;

    @Override
    protected void initData() {
        mOrderAllAdapter = new OrderAllAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mOrderAllAdapter);

        mOrderAllAdapter.setOnOrderListener(new OrderAllAdapter.OnOrderListener() {
            @Override
            public void cancle(String id) {
                initHeadMap();
                Map<String, String> map = new HashMap<>();
                map.put("orderId", id);
                net(false, false, AppBean.class)
                        .delete(1, Api.DELETE_ORDER_URL, map);
            }

            @Override
            public void submit(final String id, String allPrice) {
                mBottomDialog = new BottomDialog(getActivity())
                        .setContent("请在2小时内完成支付 金额<font color='#ff5f71'>¥" + allPrice + "</font>")
                        .showDialog()
                        .setOnClickListener(new BottomDialog.OnClickListener() {
                            @Override
                            public void click() {
                                initHeadMap();
                                Map<String, String> mapPay = new HashMap<>();
                                mapPay.put("orderId", id);
                                mapPay.put("payType", "1");
                                net(false, false, AppBean.class)
                                        .post(3, Api.PAY_URL, mapPay);
                            }
                        });
            }
        });

        mOrderAllAdapter.setOnSubmitOrderListener(new OrderAllAdapter.OnSubmitOrderListener() {
            @Override
            public void submit(String id) {
                //确认收货
                initHeadMap();
                Map<String, String> map = new HashMap<>();
                map.put("orderId", id);
                net(false, false, AppBean.class)
                        .put(2, Api.SUBMIT_ORDER_URL, map);
            }
        });

        mOrderAllAdapter.setOnChangeStateListener(new OrderAllAdapter.OnChangeStateListener() {
            @Override
            public void changeSate(int state) {
                if (mOnShopListListener != null) {
                    mOnShopListListener.change(state);
                }
                mChangeState = state;
                doOrderList(state);
            }

            @Override
            public void chanStateOk() {//点击已完成
                if (mOnShopListListener != null) {
                    mOnShopListListener.change(4);
                    changeOrderData(4);
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        isClick = false;
        Logger.i("====1", AppUtils.mOrderPosoition + "");
        doOrderList(AppUtils.mOrderPosoition);
    }

    @Override
    public void refreshData() {
        super.refreshData();

    }

    private void doOrderList(int status) {
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("status", String.valueOf(status));
        map.put("page", String.valueOf(mPager));
        map.put("count", "5");
        net(isClick, false, OrderAllBean.class)
                .get(0, Api.QUERY_ORDER_URL, map);
    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            if (type == 0) {
                OrderAllBean orderAllBean = (OrderAllBean) o;
                if ("0000".equals(orderAllBean.getStatus())) {
                    mOrderAllAdapter.setDataState(mChangeState);
                    mOrderAllAdapter.setList(orderAllBean.getOrderList());
                    if (mOnShopListListener != null) {
                        mOnShopListListener.list(orderAllBean.getOrderList().size());
                    } else {
                        mOnShopListListener.list(0);
                    }
                }
            } else if (type == 1) {
                AppBean appBean = (AppBean) o;
                toast(appBean.getMessage());
                doOrderList(mChangeState);
            } else if (type == 2) {
                AppBean appBean = (AppBean) o;
                if ("0000".equals(appBean.getStatus())) {
                    doOrderList(mChangeState);
                }
                toast(appBean.getMessage());
                // doOrderList(0);
            } else if (type == 3) {
                AppBean appBean = (AppBean) o;
                toast(appBean.getMessage());
                if ("0000".equals(appBean.getStatus())) {
                    if (mBottomDialog != null) {
                        mBottomDialog.cancle();
                    }
                    doOrderList(mChangeState);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) get(R.id.recycler);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_all;
    }

    public void changeData() {
        doOrderList(0);
    }

    private int mChangeState = 0;

    private boolean isClick;

    public void changeOrderData(int finalI) {
        isClick = true;
        mChangeState = finalI;
        if (finalI == 4) {
            mChangeState = 9;
            doOrderList(9);
        } else {
            doOrderList(finalI);
        }
    }

    private OnShopListListener mOnShopListListener;

    public void setOnShopListListener(OnShopListListener mOnShopListListener) {
        this.mOnShopListListener = mOnShopListListener;
    }

    public interface OnShopListListener {
        void list(int size);

        void change(int state);
    }
}

package com.ming.weidushop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.utils.DateUtils;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.CommentActivity;
import com.ming.weidushop.bean.OrderAllBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/9
 */
public class OrderAllAdapter extends BaseRecyclerAdapter<OrderAllBean.OrderListBean> {
    private int mChangeState;
    private boolean mClick = true;

    public OrderAllAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_order_all_item;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, final OrderAllBean.OrderListBean orderListBean) {
        RelativeLayout mOrderAll = (RelativeLayout) baseViewHolder.get(R.id.order_all);
        RelativeLayout mOrderWait = (RelativeLayout) baseViewHolder.get(R.id.order_wait);
        RelativeLayout mPjTimeLayout = (RelativeLayout) baseViewHolder.get(R.id.layout_pj_time);
        TextView mHeadTime = (TextView) baseViewHolder.get(R.id.order_time);
        mHeadTime.setText(DateUtils.getYMDTime(orderListBean.getOrderTime()));
        final TextView mTextSub = (TextView) baseViewHolder.get(R.id.order_submit);
        final TextView mTextDelete = (TextView) baseViewHolder.get(R.id.tv_order_delete);
        ImageView mImageMore = (ImageView) baseViewHolder.get(R.id.order_more);
        if (mChangeState == 0) {
            mTextDelete.setVisibility(View.GONE);
            mHeadTime.setVisibility(View.VISIBLE);
            mOrderAll.setVisibility(View.VISIBLE);
            mPjTimeLayout.setVisibility(View.GONE);
            mOrderWait.setVisibility(View.GONE);
            mImageMore.setVisibility(View.GONE);
            int orderStatus = orderListBean.getOrderStatus();
            if (orderStatus == 1) {//待付款
                mTextSub.setText("去付款");
            } else if (orderStatus == 2) {//确认收货
                mTextSub.setText("确认收货");
            } else if (orderStatus == 3) {//待评价
                mTextSub.setText("待评价");
            } else {//已完成
                mTextSub.setText("已完成");
            }
        } else if (mChangeState == 1) {//待付款
            mTextDelete.setVisibility(View.GONE);
            mHeadTime.setVisibility(View.VISIBLE);
            mOrderAll.setVisibility(View.VISIBLE);
            mPjTimeLayout.setVisibility(View.GONE);
            mOrderWait.setVisibility(View.GONE);
            mImageMore.setVisibility(View.GONE);
            mTextSub.setText("去付款");
        } else if (mChangeState == 2) {//确认
            mTextDelete.setVisibility(View.GONE);
            mHeadTime.setVisibility(View.VISIBLE);
            mOrderWait.setVisibility(View.VISIBLE);
            mOrderAll.setVisibility(View.GONE);
            mPjTimeLayout.setVisibility(View.GONE);
            mImageMore.setVisibility(View.GONE);
            mTextDelete.setVisibility(View.GONE);
            baseViewHolder.setText(R.id.tv_order_num, "快递单号 " + orderListBean.getOrderId());
            baseViewHolder.setText(R.id.tv_order_com, "派件公司 " + orderListBean.getExpressCompName());
            baseViewHolder.get(R.id.order_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSubmitOrderListener != null) {
                        mOnSubmitOrderListener.submit(orderListBean.getOrderId());
                    }
                }
            });
        } else if (mChangeState == 3) {//待评价
            mTextDelete.setVisibility(View.GONE);
            mHeadTime.setVisibility(View.GONE);
            mOrderAll.setVisibility(View.GONE);
            mImageMore.setVisibility(View.GONE);
            mImageMore.setVisibility(View.VISIBLE);
            mPjTimeLayout.setVisibility(View.VISIBLE);
            mOrderWait.setVisibility(View.GONE);
            baseViewHolder.setText(R.id.tv_pj_time, DateUtils.getYMDTime(orderListBean.getOrderTime()));
            mImageMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClick) {
                        mTextDelete.setVisibility(View.VISIBLE);
                        mClick = false;
                    } else {
                        mTextDelete.setVisibility(View.GONE);
                        mClick = true;
                    }
                }
            });

        } else if (mChangeState == 9) {//已完成
            mTextDelete.setVisibility(View.GONE);
            mHeadTime.setVisibility(View.GONE);
            mOrderAll.setVisibility(View.GONE);
            mPjTimeLayout.setVisibility(View.VISIBLE);

            mOrderWait.setVisibility(View.GONE);
            mImageMore.setVisibility(View.VISIBLE);
            mImageMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClick) {
                        mTextDelete.setVisibility(View.VISIBLE);
                        mClick = false;
                    } else {
                        mTextDelete.setVisibility(View.GONE);
                        mClick = true;
                    }

                }
            });
        }

        mTextDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnOrderListener != null) {
                    mOnOrderListener.cancle(orderListBean.getOrderId());
                }
            }
        });

        baseViewHolder.setText(R.id.order_num, "订单号：" + orderListBean.getOrderId());

        TextView textView = (TextView) baseViewHolder.get(R.id.order_all_money);
        int size = orderListBean.getDetailList().size();
        int allPrice = 0;
        for (int i = 0; i < size; i++) {
            OrderAllBean.OrderListBean.DetailListBean detailListBean = orderListBean.getDetailList().get(i);
            allPrice = allPrice + (detailListBean.getCommodityPrice() * detailListBean.getCommodityCount());
        }
        String content = "共<font color='#ff0000'>" + size + "</font>种商品，需付款<font color='#ff0000'>" + allPrice + "</font>元";
        textView.setText(Html.fromHtml(content));
//        baseViewHolder.setText(R.id.order_time, DateUtils.getYMDTime());
        RecyclerView mRecyclerView = (RecyclerView) baseViewHolder.get(R.id.order_recycler);
        OrderAllItemAdapter mOrderAllItemAdapter = new OrderAllItemAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mOrderAllItemAdapter);
        mOrderAllItemAdapter.setDataState(mChangeState);
        mOrderAllItemAdapter.setList(orderListBean.getDetailList());
        String id = orderListBean.getOrderId();
        mOrderAllItemAdapter.setOrderId(id);
        itemClick(baseViewHolder.get(R.id.order_cancle), 0, id, String.valueOf(allPrice));
        if ("去付款".equals(mTextSub.getText().toString())) {
            itemClick(baseViewHolder.get(R.id.order_submit), 1, id, String.valueOf(allPrice));
        } else if ("确认收货".equals(mTextSub.getText().toString())) {
            content = "共<font color='#ff0000'>" + size + "</font>种商品，已付款<font color='#ff0000'>" + allPrice + "</font>元";
            textView.setText(Html.fromHtml(content));
            mOrderAllItemAdapter.setDataState(2);
            mTextSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSubmitOrderListener != null) {
                        mOnSubmitOrderListener.submit(orderListBean.getOrderId());
                    }
                }
            });

        } else if ("已完成".equals(mTextSub.getText().toString())) {
            mTextSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnChangeStateListener != null) {
                        mOnChangeStateListener.chanStateOk();
                    }
                }
            });
        } else if ("待评价".equals(mTextSub.getText().toString())) {
            content = "共<font color='#ff0000'>" + size + "</font>种商品，已付款<font color='#ff0000'>" + allPrice + "</font>元";
            textView.setText(Html.fromHtml(content));
            if (mChangeState == 0) {
                mOrderAllItemAdapter.setDataState(2);
            }
            mTextSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnChangeStateListener != null) {
                        if ("已完成".equals(mTextSub.getText().toString())) {
                            mOnChangeStateListener.chanStateOk();
                        } else {
                            AppUtils.mOrderPosoition = 3;
                            mOnChangeStateListener.changeSate(3);

                        }
                    }
                }
            });

        }

    }

    private OnChangeStateListener mOnChangeStateListener;

    public void setOnChangeStateListener(OnChangeStateListener mOnChangeStateListener) {
        this.mOnChangeStateListener = mOnChangeStateListener;
    }

    public interface OnChangeStateListener {
        void changeSate(int state);

        void chanStateOk();
    }

    private void itemClick(View view, final int type, final String id, final String allPrice) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOrderListener != null) {
                    if (type == 0) {
                        mOnOrderListener.cancle(id);
                    } else {
                        mOnOrderListener.submit(id, allPrice);
                    }

                }
            }
        });
    }

    private OnOrderListener mOnOrderListener;

    public void setOnOrderListener(OnOrderListener mOnOrderListener) {
        this.mOnOrderListener = mOnOrderListener;
    }

    public void setDataState(int mChangeState) {
        this.mChangeState = mChangeState;
    }

    public interface OnOrderListener {
        void cancle(String id);

        void submit(String id, String allPrice);
    }

    private OnSubmitOrderListener mOnSubmitOrderListener;

    public void setOnSubmitOrderListener(OnSubmitOrderListener mOnSubmitOrderListener) {
        this.mOnSubmitOrderListener = mOnSubmitOrderListener;
    }

    public interface OnSubmitOrderListener {
        void submit(String id);
    }
}

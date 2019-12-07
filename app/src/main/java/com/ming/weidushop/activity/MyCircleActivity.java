package com.ming.weidushop.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.refresh.material.Util;
import com.abner.ming.base.refresh.recy.XRecyclerView;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.MyCircleAdapter;
import com.ming.weidushop.bean.CircleBean;
import com.ming.weidushop.view.LayoutDataNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/12
 * 我的圈子
 */
public class MyCircleActivity extends BaseAppCompatActivity {
    private int mPage = 1;
    private MyCircleAdapter mMyCircleAdapter;
    private XRecyclerView mXRecyclerView;
    private List<CircleBean.ResultBean> mCircleList = new ArrayList<>();
    private LayoutDataNull mLayoutDataNull;
    private boolean mShow = true;
    private String mCommodityId;

    @Override
    protected void initData() {
        mMyCircleAdapter = new MyCircleAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(linearLayoutManager);
        mXRecyclerView.setAdapter(mMyCircleAdapter);
        doHttp();
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                doHttp();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                doHttp();
            }
        });

        mMyCircleAdapter.setOnCircleListener(new MyCircleAdapter.OnCircleListener() {
            @Override
            public void circle(int id) {
                //点赞
                initHeadMap();
                Map<String, String> map = new HashMap<>();
                map.put("circleId", String.valueOf(id));
                net(false, false, AppBean.class)
                        .post(1, Api.ZAN_URL, map);
            }

            @Override
            public void cancle(int id) {
                initHeadMap();
                Map<String, String> map = new HashMap<>();
                map.put("circleId", String.valueOf(id));
                net(false, false, AppBean.class)
                        .delete(1, Api.DELETE_ZAN_URL, map);
            }

            @Override
            public void deleteList(List<CircleBean.ResultBean> list) {
                mCommodityId = "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isClick()) {
                        int commodityId = list.get(i).getId();
                        mCommodityId = mCommodityId + commodityId + ",";
                    }
                }
            }
        });
    }

    private void doHttp() {
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(mPage));
        map.put("count", "10");
        net(false, false, CircleBean.class)
                .get(0, Api.MY_CIRCLE_URL, map);
    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            if (type == 0) {
                mXRecyclerView.refreshComplete();
                mXRecyclerView.loadMoreComplete();
                CircleBean circleBean = (CircleBean) o;
                if ("0000".equals(circleBean.getStatus())) {
                    if (mPage == 1) {
                        mCircleList.clear();
                    }
                    mCircleList.addAll(circleBean.getResult());
                    mMyCircleAdapter.setList(mCircleList);
                    if (mCircleList.size() == 0) {
                        mXRecyclerView.setPullRefreshEnabled(false);
                        mLayoutDataNull.setVisibility(View.VISIBLE);
                    } else {
                        mXRecyclerView.setPullRefreshEnabled(true);
                        mLayoutDataNull.setVisibility(View.GONE);
                    }
                }
            } else if (type == 1) {
                AppBean appBean = (AppBean) o;
                if ("0000".equals(appBean.getStatus())) {
                    toast(appBean.getMessage());
                }
            } else if (type == 2) {
                AppBean appBean = (AppBean) o;
                toast(appBean.getMessage());
                if ("0000".equals(appBean.getStatus())) {
                    doHttp();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initView() {
        setShowTitle(false);
        setTitle("我的圈子");
        isShowBack(true);
        setWindowTitleBlack(true);
        getRightTextView().setVisibility(View.VISIBLE);
        getRightTextView().setText("");
        getRightTextView().setBackgroundResource(R.drawable.delete);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getRightTextView().getLayoutParams();
        params.height = Util.dip2px(this, 20);
        params.width = Util.dip2px(this, 15);
        params.rightMargin = Util.dip2px(this, 10);
        getRightTextView().setLayoutParams(params);
        getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayoutDataNull.getVisibility() == View.VISIBLE) {
                    return;
                }
                if (mShow) {

                    mShow = false;
                } else {
                    deleteCircle();
                    mShow = true;
                }
                mMyCircleAdapter.setShowDelete(!mShow);
            }
        });
        mXRecyclerView = (XRecyclerView) get(R.id.recycler);
        mLayoutDataNull = (LayoutDataNull) get(R.id.layout_data_null);
    }


    private void deleteCircle() {
        if (TextUtils.isEmpty(mCommodityId)) {
            return;
        }
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("circleId", mCommodityId.substring(0, mCommodityId.length() - 1));
        net(false, false, AppBean.class)
                .delete(2, Api.DELETE_CIRCLE_URL, map);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_circle;
    }
}

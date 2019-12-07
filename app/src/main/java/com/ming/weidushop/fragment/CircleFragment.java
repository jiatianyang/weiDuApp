package com.ming.weidushop.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.abner.ming.base.BaseFragment;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.refresh.recy.XRecyclerView;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.CircleAdapter;
import com.ming.weidushop.bean.CircleBean;
import com.ming.weidushop.view.LayoutDataNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/4
 * 圈子
 */
public class CircleFragment extends BaseFragment {
    private int mPage = 1;
    private CircleAdapter mCircleAdapter;
    private XRecyclerView mXRecyclerView;
    private List<CircleBean.ResultBean> mCircleList = new ArrayList<>();
    private LayoutDataNull mLayoutDataNull;

    @Override
    protected void initData() {
        mCircleAdapter = new CircleAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(linearLayoutManager);
        mXRecyclerView.setAdapter(mCircleAdapter);

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

        mCircleAdapter.setOnCircleListener(new CircleAdapter.OnCircleListener() {
            @Override
            public void circle(int id) {
                //点赞
                Map<String, String> map = new HashMap<>();
                map.put("circleId", String.valueOf(id));
                net(false, false, AppBean.class)
                        .post(1, Api.ZAN_URL, map);
            }

            @Override
            public void cancle(int id) {
                Map<String, String> map = new HashMap<>();
                map.put("circleId", String.valueOf(id));
                net(false, false, AppBean.class)
                        .delete(1, Api.DELETE_ZAN_URL, map);
            }
        });
        doHttp();
    }

    @Override
    public void refreshData() {
        super.refreshData();

    }

    private void doHttp() {
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(mPage));
        map.put("count", "5");
        net(false, true, CircleBean.class)
                .get(0, Api.CIRCLE_URL, map);
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
                    mCircleAdapter.setList(mCircleList);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initView(View view) {
        mXRecyclerView = (XRecyclerView) get(R.id.xrecycler);
        mLayoutDataNull = (LayoutDataNull) get(R.id.layout_data_null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_foot;
    }
}

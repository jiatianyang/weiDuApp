package com.ming.weidushop.activity;

import android.support.v7.widget.StaggeredGridLayoutManager;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.refresh.recy.XRecyclerView;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.FootAdapter;
import com.ming.weidushop.bean.FootBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/4
 * 我的足迹
 */
public class MyFootActivity extends BaseAppCompatActivity {
    private XRecyclerView mXRecyclerView;
    private FootAdapter mFootAdapter;
    private int mPage = 1;
    private List<FootBean.ResultBean> mFootList = new ArrayList<>();

    @Override
    protected void initData() {

        setShowTitle(false);
        setTitle("我的足迹");
        isShowBack(true);
        setWindowTitleBlack(true);
        mFootAdapter = new FootAdapter(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mXRecyclerView.setAdapter(mFootAdapter);

        doFoot();
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                doFoot();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                doFoot();
            }
        });
    }

    private void doFoot() {
        initHeadMap();
        Map<String, String> map = new HashMap();
        map.put("page", String.valueOf(mPage));
        map.put("count", "10");
        net(false, false, FootBean.class)
                .get(0, Api.FOOT_URL, map);
    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            mXRecyclerView.refreshComplete();
            mXRecyclerView.loadMoreComplete();
            FootBean mFootBean = (FootBean) o;
            if ("0000".equals(mFootBean.getStatus())) {
                if (mPage == 1) {
                    mFootList.clear();
                }
                mFootList.addAll(mFootBean.getResult());
                mFootAdapter.setList(mFootList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initView() {
        mXRecyclerView = (XRecyclerView) get(R.id.xrecycler);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_foot;
    }
}

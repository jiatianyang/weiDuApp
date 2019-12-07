package com.ming.weidushop.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.refresh.recy.XRecyclerView;
import com.abner.ming.base.utils.Logger;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.PayAdapter;
import com.ming.weidushop.bean.PayBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/10
 * 我的钱包
 */
public class MyPayActivity extends BaseAppCompatActivity {
    private int mPager = 1;
    private TextView mMoney;
    private XRecyclerView mRecycler;
    private PayAdapter mPayAdapter;
    private List<PayBean.ResultBean.DetailListBean> mPayList = new ArrayList<>();

    @Override
    protected void initData() {
        doPayData();
        mPayAdapter = new PayAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(mPayAdapter);
        mRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPager = 1;
                doPayData();
            }

            @Override
            public void onLoadMore() {
                mPager++;
                doPayData();
            }
        });
    }

    private void doPayData() {
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(mPager));
        map.put("count", "10");
        net(false, false, PayBean.class)
                .get(0, Api.QUERY_PAY_URL, map);
    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            mRecycler.refreshComplete();
            mRecycler.loadMoreComplete();
            PayBean payBean = (PayBean) o;
            if ("0000".equals(payBean.getStatus())) {
                String p = String.valueOf(payBean.getResult().getBalance());
                BigDecimal db = new BigDecimal(p);
                String ii = db.toPlainString();
                mMoney.setText(ii);

                if (mPager == 1) {
                    mPayList.clear();
                }
                mPayList.addAll(payBean.getResult().getDetailList());
                mPayAdapter.setList(mPayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        setShowTitle(false);
        setTitle("我的钱包");
        isShowBack(true);
        setWindowTitleBlack(true);
        mMoney = (TextView) get(R.id.tv_money);
        mRecycler = (XRecyclerView) get(R.id.recycler);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_pay;
    }
}

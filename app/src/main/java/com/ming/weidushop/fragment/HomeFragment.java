package com.ming.weidushop.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abner.ming.base.BaseFragment;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.refresh.material.Util;
import com.abner.ming.base.refresh.recy.XRecyclerView;
import com.abner.ming.base.utils.NetworkUtils;
import com.abner.ming.base.utils.SharedPreUtils;
import com.abner.ming.base.utils.ToastUtils;
import com.abner.ming.base.utils.WindowUtils;
import com.google.gson.Gson;
import com.ming.weidushop.activity.MainActivity;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.SearchActivity;
import com.ming.weidushop.adapter.IndexAdapter;
import com.ming.weidushop.bean.BannerBean;
import com.ming.weidushop.bean.BaseBean;
import com.ming.weidushop.bean.IndexListBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/3
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private XRecyclerView mXRecycler;
    private List<BaseBean> baseBeanList = new ArrayList<>();
    private IndexAdapter mIndexAdapter;
    private RelativeLayout mHomeTitle;
    private int mHeight,mOverallXScroll;// 滑动开始变色的高,真实项目中此高度是由广告轮播或其他首页view高度决定
    private ImageView mHomeSearch,mHomeSetting;

    @Override
    protected void initData() {
        mHeight = Util.dip2px(getActivity(), 150);
        mIndexAdapter = new IndexAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecycler.setLayoutManager(linearLayoutManager);
        mXRecycler.setAdapter(mIndexAdapter);
        //请求数据


        mXRecycler.setLoadingMoreEnabled(false);//关闭上拉
        mXRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected(getActivity())) {
                    long oldTime = SharedPreUtils.getLong(getActivity(), "shop_time");
                    long newTime = System.currentTimeMillis();
                    if ((newTime - oldTime) < 1000 * 60 * 10) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (baseBeanList.isEmpty()) {
                                    net(false, false).get(0, Api.INDEX_BANNER, null);
                                } else {
                                    mXRecycler.refreshComplete();
                                }
                            }
                        }, 1000);

                    } else {
                        SharedPreUtils.put(getActivity(), "shop_time", newTime);
                        net(false, false).get(0, Api.INDEX_BANNER, null);
                    }
                } else {
                    ToastUtils.show(NetworkUtils.networkMessage);
                    mXRecycler.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {

            }
        });

        net(false, true).get(0, Api.INDEX_BANNER, null);

        mXRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mOverallXScroll = mOverallXScroll + dy;// 累加y值 解决滑动一半y值为0
                if (mOverallXScroll <= 0) {   //设置标题的背景颜色
                    mHomeTitle.setBackgroundColor(Color.TRANSPARENT);
                } else if (mOverallXScroll > 0 && mOverallXScroll <= mHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    mHomeTitle.setBackgroundColor(Color.TRANSPARENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mHomeSearch.setImageDrawable(getActivity().getDrawable(R.drawable.index_seach_white));
                        mHomeSetting.setImageDrawable(getActivity().getDrawable(R.drawable.common_btn_menu_white));
                    }
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).setWindowBaseViewBlack(false);
                        setMarginTop(mHomeTitle, WindowUtils.getStatusBarHeight(getActivity()));
                    }
                } else {
                    mHomeTitle.setBackgroundColor(Color.WHITE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mHomeSearch.setImageDrawable(getActivity().getDrawable(R.drawable.index_seach));
                        mHomeSetting.setImageDrawable(getActivity().getDrawable(R.drawable.common_btn_menu));
                    }
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).setWindowBaseViewBlack(true);
                        setMarginTop(mHomeTitle, 0);
                    }
                }

            }
        });
    }

    @Override
    public void success(int type, String data) {
        super.success(type, data);
        try {
            mXRecycler.loadMoreComplete();
            mXRecycler.refreshComplete();
            if (type == 0) {//banner请求
                baseBeanList.clear();
                BannerBean bean = new Gson().fromJson(data, BannerBean.class);
                baseBeanList.add(bean);
                net(true, true)
                        .get(1, Api.INDEX_LIST_URL, null);
            } else {//列表请求
                IndexListBean bean = new Gson().fromJson(data, IndexListBean.class);
                IndexListBean.ResultBean.RxxpBean rxxpBean = bean.getResult().getRxxp();  //热销新品
                IndexListBean.ResultBean.MlssBean mlssBean = bean.getResult().getMlss();   //魔力时尚
                IndexListBean.ResultBean.PzshBean pzshBean = bean.getResult().getPzsh();   //品质生活


                baseBeanList.add(rxxpBean);
                baseBeanList.add(mlssBean);
                baseBeanList.add(pzshBean);

                mIndexAdapter.setList(baseBeanList);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initView(View view) {
        mXRecycler = (XRecyclerView) get(R.id.recycler);
        mHomeTitle = (RelativeLayout) get(R.id.home_title);
        mHomeSearch = (ImageView) get(R.id.home_search);
        mHomeSetting = (ImageView) get(R.id.home_setting);
        mHomeSearch.setOnClickListener(this);
        mHomeSetting.setOnClickListener(this);
        setMarginTop(mXRecycler, 0);
        setMarginTop(mHomeTitle, WindowUtils.getStatusBarHeight(getActivity()));
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    public void setMarginTop(View view, int size) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = size;
        view.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search://点击搜索
                AppUtils.start(getActivity(), SearchActivity.class);
                break;
            case R.id.home_setting:
                Map<String, String> map = new HashMap<>();
                map.put("key", "0");
                AppUtils.startString(getActivity(), SearchActivity.class, map);
                break;
        }
    }
}

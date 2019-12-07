package com.ming.weidushop.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.refresh.recy.XRecyclerView;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.SearchAdapter;
import com.ming.weidushop.bean.FindCategoryBean;
import com.ming.weidushop.bean.SearchBean;
import com.ming.weidushop.view.LayoutDataNull;
import com.ming.weidushop.view.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/5
 * 搜索商品页
 */
public class SearchActivity extends BaseAppCompatActivity {
    private XRecyclerView mXRecyclerView;
    private int mPage = 1;
    private SearchView mSearch;
    private SearchAdapter mSearchAdapter;
    private List<SearchBean.ResultBean> mSearchList = new ArrayList<>();
    private String mSearchContent = "手机";
    private LayoutDataNull mLayoutNull;
    private RecyclerView mSearchRecycler, mSearchListRecycler;
    private SearchHeadAdapter mSearchHeadAdapter;
    private SearchFootAdapter mSearchFootAdapter;
    private RelativeLayout mLayoutSetting;
    private boolean mLayoutShopShow = true;
    private String mShopId, mKey;
    private List<FindCategoryBean.ResultBean> mFindCategoryList = new ArrayList<>();

    @Override
    protected void initData() {
        mShopId = getIntent().getStringExtra("shop_id");
        mKey = getIntent().getStringExtra("key");
        mSearchAdapter = new SearchAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mXRecyclerView.setLayoutManager(gridLayoutManager);
        mXRecyclerView.setAdapter(mSearchAdapter);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                if (!TextUtils.isEmpty(mShopId)) {
                    doBannerShopList();
                } else {
                    doSearch(mSearchContent);

                }
            }

            @Override
            public void onLoadMore() {
                mPage++;
                if (!TextUtils.isEmpty(mShopId)) {
                    doBannerShopList();
                } else {
                    doSearch(mSearchContent);

                }
            }
        });
        doSearch(mSearchContent);

        mSearchHeadAdapter = new SearchHeadAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSearchRecycler.setLayoutManager(linearLayoutManager);
        mSearchRecycler.setAdapter(mSearchHeadAdapter);
        net(false, false, FindCategoryBean.class)
                .get(1, Api.SHOP_CATEGORY_URL, null);

        hideSoftKeyboard(this);

        mSearchFootAdapter = new SearchFootAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSearchListRecycler.setLayoutManager(layoutManager);
        mSearchListRecycler.setAdapter(mSearchFootAdapter);
        if (!TextUtils.isEmpty(mShopId)) {
            doBannerShopList();
        }

        if (!TextUtils.isEmpty(mKey)) {
            showShopList();
        }


    }

    private void doBannerShopList() {
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("categoryId", mShopId);
        map.put("page", String.valueOf(mPage));
        map.put("count", "10");
        net(false, false, SearchBean.class).get(0, Api.SHOP_CATEGORY_BOTTOM, map);
    }


    private void doSearch(String content) {
        mSearchContent = content;
        Map<String, String> map = new HashMap<>();
        map.put("keyword", content);
        map.put("page", String.valueOf(mPage));
        map.put("count", "10");
        net(true, false, SearchBean.class)
                .get(0, Api.SEARCH_URL, map);

    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            if (type == 0) {
                mXRecyclerView.loadMoreComplete();
                mXRecyclerView.refreshComplete();
                SearchBean searchBean = (SearchBean) o;
                if ("0000".equals(searchBean.getStatus())) {
                    if (mPage == 1) {
                        mSearchList.clear();
                    }
                    mSearchList.addAll(searchBean.getResult());
                    if (mSearchList.isEmpty()) {
                        mLayoutNull.setVisibility(View.VISIBLE);
                    } else {
                        mLayoutNull.setVisibility(View.GONE);
                    }
                    mSearchAdapter.setList(mSearchList);
                }
            } else if (type == 1) {
                FindCategoryBean shopListLeftBean = (FindCategoryBean) o;
                if ("0000".equals(shopListLeftBean.getStatus())) {
                    mFindCategoryList = shopListLeftBean.getResult();
                    mFindCategoryList.get(0).setClick(true);
                    mSearchHeadAdapter.setList(mFindCategoryList);
                    doFootShop(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        setWindowBaseViewBlack(true);
        mXRecyclerView = (XRecyclerView) get(R.id.xrecycler);
        mSearch = (SearchView) get(R.id.searchview);
        mLayoutNull = (LayoutDataNull) get(R.id.layout_null);
        mSearchRecycler = (RecyclerView) get(R.id.search_recycler);
        mSearchListRecycler = (RecyclerView) get(R.id.search_list_recycler);
        mLayoutSetting = (RelativeLayout) get(R.id.layout_setting);
        mSearch.setOnSerachListener(new SearchView.OnSerachListener() {
            @Override
            public void search(String content) {
                if (TextUtils.isEmpty(content)) {
                    toast("商品不能为空");
                    return;
                }
                mShopId = null;
                commoditySearch(content);
            }

            @Override
            public void setting() {
                if (mLayoutShopShow) {
                    mLayoutSetting.setVisibility(View.VISIBLE);
                    mLayoutShopShow = false;
                } else {
                    mLayoutSetting.setVisibility(View.GONE);
                    mLayoutShopShow = true;
                }
            }
        });
    }

    public void showShopList() {
        mLayoutSetting.setVisibility(View.VISIBLE);
        mLayoutShopShow = false;
    }

    //搜索商品
    private void commoditySearch(String content) {
        doSearch(content);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    private class SearchHeadAdapter extends BaseRecyclerAdapter<FindCategoryBean.ResultBean> {

        public SearchHeadAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.search_shop_adapter_top;
        }

        @Override
        public void bindViewDataPosition(BaseViewHolder baseViewHolder, FindCategoryBean.ResultBean resultBean, final int i) {
            super.bindViewDataPosition(baseViewHolder, resultBean, i);
            TextView mText = (TextView) baseViewHolder.get(R.id.search_text);
            mText.setText(resultBean.getName());
            if (resultBean.isClick()) {
                mText.setTextColor(getResources().getColor(R.color.color_ff5));
            } else {
                mText.setTextColor(getResources().getColor(R.color.colorFFF));
            }
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < getList().size(); j++) {
                        if (i == j) {
                            getList().get(j).setClick(true);
                        } else {
                            getList().get(j).setClick(false);
                        }
                    }
                    doFootShop(i);
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public void bindViewData(BaseViewHolder baseViewHolder, FindCategoryBean.ResultBean resultBean) {

        }


    }

    //切换底部
    private void doFootShop(int i) {
        List<FindCategoryBean.ResultBean.SecondCategoryVoBean> secondCategoryVo =
                mFindCategoryList.get(i).getSecondCategoryVo();
        mSearchFootAdapter.setList(secondCategoryVo);

    }

    private class SearchFootAdapter extends BaseRecyclerAdapter<FindCategoryBean.ResultBean.SecondCategoryVoBean> {

        public SearchFootAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.search_shop_adapter_top;
        }

        @Override
        public void bindViewData(BaseViewHolder baseViewHolder, final FindCategoryBean.ResultBean.SecondCategoryVoBean resultBean) {
            TextView mText = (TextView) baseViewHolder.get(R.id.search_text);
            mText.setText(resultBean.getName());
            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = resultBean.getName();
                    mLayoutSetting.setVisibility(View.GONE);
                    mLayoutShopShow = true;
                    doSearch(name);
                }
            });
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

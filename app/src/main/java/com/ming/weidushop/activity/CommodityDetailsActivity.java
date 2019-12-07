package com.ming.weidushop.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.refresh.recy.XRecyclerView;
import com.abner.ming.base.utils.Logger;
import com.abner.ming.base.utils.SharedPreUtils;
import com.abner.ming.base.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.CircleAdapter;
import com.ming.weidushop.bean.CircleBean;
import com.ming.weidushop.bean.CommodityDetailsBean;
import com.ming.weidushop.bean.DetailsBannerBean;
import com.ming.weidushop.bean.ShopCarBean;
import com.ming.weidushop.utils.AppUtils;
import com.stx.xhb.xbanner.XBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/5
 * 商品详情页
 */
public class CommodityDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private final static String TAG = CommodityDetailsActivity.class.getName();
    private int mCommodityId;
    private XBanner mXBanner;
    private TextView mDetailsPrice, mDetailsNum, mDetailsDes,
            mCommoduty_1, mCommoduty_2, mCommoduty_3, mDetailsWebTitle, mDetailsInfoTitle;
    private WebView mDetailsWeb;
    private ScrollView mScrollView;
    private JSONArray mJSONArray = new JSONArray();
    private XRecyclerView mXRecyclerView;
    private float mPrice;
    private CircleAdapter mCircleAdapter;
    private CommodityDetailsBean.ResultBean mBeanTemp;

    @Override
    protected void initData() {

        mCommodityId = getIntent().getIntExtra("id", -1);
        mCircleAdapter = new CircleAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(linearLayoutManager);
        mXRecyclerView.setAdapter(mCircleAdapter);
        mXRecyclerView.setPullRefreshEnabled(false);
        mXRecyclerView.setHasFixedSize(true);
        mXRecyclerView.setNestedScrollingEnabled(false);

        doHttp();
        detailsListener();
    }

    private void detailsListener() {
        mCircleAdapter.setOnCircleListener(new CircleAdapter.OnCircleListener() {
            @Override
            public void circle(int id) {
                Map<String, String> map = new HashMap<>();
                map.put("circleId", String.valueOf(id));
                net(false, false, AppBean.class)
                        .post(1000, Api.ZAN_URL, map);
            }

            @Override
            public void cancle(int id) {
                Map<String, String> map = new HashMap<>();
                map.put("circleId", String.valueOf(id));
                net(false, false, AppBean.class)
                        .delete(1000, Api.DELETE_ZAN_URL, map);
            }
        });
    }

    //请求网络
    private void doHttp() {
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("commodityId", String.valueOf(mCommodityId));
        net(false, false, CommodityDetailsBean.class)
                .get(0, Api.COMMODITYDETAILS_URL, map);
        doShop();
        Map<String, String> comment = new HashMap<>();
        comment.put("commodityId", String.valueOf(mCommodityId));
        comment.put("page", "1");
        comment.put("count", "10");
        net(true, false, CircleBean.class)
                .get(100, Api.COMMODITY_COMMENT_URL, comment);
    }

    //请求购物数据
    private void doShop() {
        net(false, false, ShopCarBean.class)
                .get(1, Api.QUERY_CAR_URL, null);
    }




    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            if (type == 0) {
                CommodityDetailsBean commodityDetailsBean = (CommodityDetailsBean) o;
                if ("0000".equals(commodityDetailsBean.getStatus())) {
                    CommodityDetailsBean.ResultBean bean = commodityDetailsBean.getResult();
                    mBeanTemp = bean;
                    String picture = bean.getPicture();
                    String[] pics = picture.split(",");
                    List<DetailsBannerBean> bannerList = new ArrayList<>();
                    for (int i = 0; i < pics.length; i++) {
                        DetailsBannerBean bannerBean = new DetailsBannerBean();
                        bannerBean.setPic(pics[i]);
                        bannerList.add(bannerBean);
                    }
                    createBanner(bannerList);
                    mPrice = bean.getPrice();
                    mDetailsPrice.setText("¥" + bean.getPrice());
                    mDetailsNum.setText("已售" + bean.getSaleNum() + "件");
                    mDetailsDes.setText(bean.getCommodityName() + " " + bean.getDescribe());

                    String details = bean.getDetails();
                    if (details.contains("http:http")) {
                        details = details.replaceAll("http:http", "http");
                    }
                    Logger.i("loadDataWithBaseURL", bean.getDetails());
                    Utils.setttingWebView(mDetailsWeb);
                    mDetailsWeb.loadDataWithBaseURL(null, details, "text/html",
                            "UTF-8", null);
                }
            } else if (type == 1) {
                try {
                    ShopCarBean shopCarBean = (ShopCarBean) o;
                    List<ShopCarBean.ResultBean> shopCarList = shopCarBean.getResult();
                    for (int i = 0; i < shopCarList.size(); i++) {
                        ShopCarBean.ResultBean bean = shopCarList.get(i);
                        List<ShopCarBean.ResultBean.ShoppingCartListBean> carList =
                                bean.getShoppingCartList();
                        for (int j = 0; j < carList.size(); j++) {
                            ShopCarBean.ResultBean.ShoppingCartListBean b = carList.get(j);
                            int commod = b.getCommodityId();
                            int count = b.getCount();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("commodityId", String.valueOf(commod));
                            jsonObject.put("count", String.valueOf(count));
                            mJSONArray.put(jsonObject);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (type == 3) {
                AppBean appBean = (AppBean) o;
                if ("0000".equals(appBean.getStatus())) {
                    toast("商品已成功添加至购物车");
                }
            } else if (type == 4) {
                AppBean appBean = (AppBean) o;
                toast(appBean.getMessage());
                if ("0000".equals(appBean.getStatus())) {
                    //成功创建订单
                    finish();
                }
            } else if (type == 100) {
                CircleBean mCircleBean = (CircleBean) o;
                if ("0000".equals(mCircleBean.getStatus())) {
                    List<CircleBean.ResultBean> list = mCircleBean.getResult();
                    mCircleAdapter.setList(list);

                }

            } else if (type == 1000) {
                AppBean appBean = (AppBean) o;
                if ("0000".equals(appBean.getStatus())) {
                    toast(appBean.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //初始化Banner
    private void createBanner(List<DetailsBannerBean> bannerList) {
        mXBanner.setBannerData(R.layout.image_fresco, bannerList);
        mXBanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                DetailsBannerBean b = (DetailsBannerBean) model;
                ((SimpleDraweeView) view).setImageURI(b.getPic());
            }
        });
    }

    @Override
    protected void initView() {
        setWindowBaseViewBlack(true);
        mXBanner = (XBanner) get(R.id.xbanner);
        mXRecyclerView = (XRecyclerView) get(R.id.xrecycler);
        mDetailsPrice = (TextView) get(R.id.details_price);
        mDetailsNum = (TextView) get(R.id.details_num);
        mDetailsDes = (TextView) get(R.id.details_desc);
        mDetailsWeb = (WebView) get(R.id.details_web);
        mScrollView = (ScrollView) get(R.id.scrollview);
        mCommoduty_1 = (TextView) get(R.id.commodity_1);
        mCommoduty_2 = (TextView) get(R.id.commodity_2);
        mCommoduty_3 = (TextView) get(R.id.commodity_3);
        mDetailsWebTitle = (TextView) get(R.id.details_web_title);
        mDetailsInfoTitle = (TextView) get(R.id.details_info_title);

        get(R.id.details_iv_back).setOnClickListener(this);
        get(R.id.details_layout_car).setOnClickListener(this);
        get(R.id.layout_buy).setOnClickListener(this);
        mCommoduty_1.setOnClickListener(this);
        mCommoduty_2.setOnClickListener(this);
        mCommoduty_3.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Logger.i(TAG, scrollY + "===" + oldScrollY);
                    int top = mDetailsWebTitle.getTop();
                    int topInFo = mDetailsInfoTitle.getTop();
                    if (scrollY < top) {
                        setTitleColor(mCommoduty_1, mCommoduty_2, mCommoduty_3);
                    } else if (scrollY > top && scrollY < topInFo) {
                        setTitleColor(mCommoduty_2, mCommoduty_1, mCommoduty_3);
                    } else if (scrollY > topInFo) {
                        setTitleColor(mCommoduty_3, mCommoduty_1, mCommoduty_2);
                    }
                }
            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_commodity_details;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details_iv_back:
                finish();
                break;
            case R.id.commodity_1://商品
                mScrollView.fullScroll(ScrollView.FOCUS_UP);//滑动到顶部
                setTitleColor(mCommoduty_1, mCommoduty_2, mCommoduty_3);
                break;
            case R.id.commodity_2://详情
                setTitleColor(mCommoduty_2, mCommoduty_1, mCommoduty_3);
                int top = mDetailsWebTitle.getTop();
                scrollTo(top);
                Logger.i(TAG, top + "");
                break;
            case R.id.commodity_3://评论
                setTitleColor(mCommoduty_3, mCommoduty_1, mCommoduty_2);
                int topInFo = mDetailsInfoTitle.getTop();
                scrollTo(topInFo);
                break;
            case R.id.details_layout_car://添加购物车
                if (AppUtils.getUserInFo() == null) {
                    AppUtils.start(this, LoginActivity.class);
                    return;
                }
                try {
                    String islogin = SharedPreUtils.getString(this, "islogin");
                    if (TextUtils.isEmpty(islogin) || "0".equals(islogin)) {
                        AppUtils.start(this, LoginActivity.class);
                        return;
                    }
                    addCar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.layout_buy://购买
                try {
                    if (AppUtils.getUserInFo() == null) {
                        AppUtils.start(this, LoginActivity.class);
                        return;
                    }
                    String islogin = SharedPreUtils.getString(this, "islogin");
                    if (TextUtils.isEmpty(islogin) || "0".equals(islogin)) {
                        AppUtils.start(this, LoginActivity.class);
                        return;
                    }
                    List<ShopCarBean.ResultBean> shopCarListTemp = new ArrayList<>();
                    ShopCarBean.ResultBean bean = new ShopCarBean.ResultBean();
                    if (mBeanTemp != null) {
                        String categoryName = mBeanTemp.getCategoryName();
                        bean.setCategoryName(categoryName);
                        List<ShopCarBean.ResultBean.ShoppingCartListBean> shoppingCartList = new ArrayList<>();
                        ShopCarBean.ResultBean.ShoppingCartListBean b = new ShopCarBean.ResultBean.ShoppingCartListBean();
                        b.setSelected(true);
                        b.setCommodityName(mBeanTemp.getCommodityName());
                        b.setCommodityId(mBeanTemp.getCommodityId());
                        b.setCount(1);
                        b.setPrice(mBeanTemp.getPrice());
                        String s = mBeanTemp.getPicture().split(",")[0];
                        b.setPic(s);
                        shoppingCartList.add(b);
                        bean.setShoppingCartList(shoppingCartList);
                        shopCarListTemp.add(bean);
                    }
                    AppUtils.startResultObject(CommodityDetailsActivity.this, SubmitOrderActivity.class, shopCarListTemp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    //购买
    private void doBuy() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("commodityId", mCommodityId);
            jsonObject.put("amount", 1);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String adaress = SharedPreUtils.getString(this, "adaress");
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("orderInfo", jsonArray.toString());
        map.put("totalPrice", String.valueOf(mPrice));
        map.put("addressId", adaress);
        net(false, false, AppBean.class).post(4, Api.CREATE_ORDER_URL, map);
    }

    //添加购物车
    private void addCar() throws Exception {
        boolean isCommodityId = false;
        for (int i = 0; i < mJSONArray.length(); i++) {
            JSONObject jsonObject = mJSONArray.getJSONObject(i);
            String commod = jsonObject.getString("commodityId");
            String carCount = jsonObject.getString("count");
            if (String.valueOf(mCommodityId).equals(commod)) {
                isCommodityId = true;
                jsonObject.put("commodityId", commod);
                jsonObject.put("count", (Integer.parseInt(carCount) + 1) + "");
            }
        }

        if (!isCommodityId) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("commodityId", String.valueOf(mCommodityId));
            jsonObject.put("count", "1");
            mJSONArray.put(jsonObject);
        }
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("data", mJSONArray.toString());

        Logger.i("JSONObject", mJSONArray.toString());
        net(false, false, AppBean.class)
                .put(3, Api.ADD_CAR_URL, map);

    }


    private void setTitleColor(View view1, View view2, View view3) {
        view1.setBackgroundResource(R.drawable.details_shape);
        view2.setBackgroundResource(R.drawable.details_shape_fff);
        view3.setBackgroundResource(R.drawable.details_shape_fff);
    }

    //滚动到指定位置
    private void scrollTo(final int y) {
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                //滚动到指定位置（滚动要跳过的控件的高度的距离）
                mScrollView.scrollTo(0, y);
                //如果要平滑滚动，可以这样写
                //scrollView.smoothScrollTo(0, llNeedToSkip.getMeasuredHeight());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10001) {
            finish();
        }
    }

}

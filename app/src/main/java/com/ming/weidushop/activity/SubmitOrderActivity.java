package com.ming.weidushop.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.refresh.material.Util;
import com.google.gson.Gson;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.OrderShopCarAdapter;
import com.ming.weidushop.adapter.SubmitOrderAddressAdapter;
import com.ming.weidushop.bean.AddressBean;
import com.ming.weidushop.bean.OrderAllBean;
import com.ming.weidushop.bean.ShopCarBean;
import com.ming.weidushop.utils.AppUtils;
import com.ming.weidushop.utils.BottomDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/15
 * 确认订单
 */
public class SubmitOrderActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private List<ShopCarBean.ResultBean> mShopList;
    private TextView mOrderName, mOrderPhone, mOrderAddress, mOrderDesc;
    private RecyclerView mAddressRecycler, mRecyclerOrderList;
    private boolean mIsDown = true;
    private SubmitOrderAddressAdapter mSubmitOrderAddressAdapter;
    private ImageView mAddressDown;
    private RelativeLayout mLayoutAddress;
    private View mViewAddress;
    private RelativeLayout mParamsHeight;
    private OrderShopCarAdapter mOrderShopCarAdapter;
    private String mAddress;
    private float mAllPrice = 0;
    private int mNum = 0;
    private JSONArray mJSONArray = new JSONArray();

    @Override
    protected void initData() {
        try {
            AppUtils.mCommodityByOrder = 0;
            mShopList = (List<ShopCarBean.ResultBean>) getIntent().getSerializableExtra("object");
            mSubmitOrderAddressAdapter = new SubmitOrderAddressAdapter(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mAddressRecycler.setLayoutManager(linearLayoutManager);
            mAddressRecycler.setAdapter(mSubmitOrderAddressAdapter);

            doAddressHttp();


            mOrderShopCarAdapter = new OrderShopCarAdapter(this);
            LinearLayoutManager linearOrder = new LinearLayoutManager(this);
            linearOrder.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerOrderList.setLayoutManager(linearOrder);
            mRecyclerOrderList.setAdapter(mOrderShopCarAdapter);


            for (int i = 0; i < mShopList.size(); i++) {
                List<ShopCarBean.ResultBean.ShoppingCartListBean> shoppingCartList
                        = mShopList.get(i).getShoppingCartList();

                int selected = 0;
                for (int j = 0; j < shoppingCartList.size(); j++) {
                    ShopCarBean.ResultBean.ShoppingCartListBean shoppingCartListBean = shoppingCartList.get(j);
                    if (shoppingCartListBean.isSelected()) {
                        selected++;
                    }
                }
                if (selected == 0) {
                    mShopList.remove(i);
                }
            }

            for (int i = 0; i < mShopList.size(); i++) {
                ShopCarBean.ResultBean resultBean = mShopList.get(i);
                List<ShopCarBean.ResultBean.ShoppingCartListBean> shoppingCartList =
                        resultBean.getShoppingCartList();

                for (int j = 0; j < shoppingCartList.size(); j++) {
                    ShopCarBean.ResultBean.ShoppingCartListBean shoppingCartListBean = shoppingCartList.get(j);
                    if (shoppingCartListBean.isSelected()) {
                        int count = shoppingCartList.get(j).getCount();
                        float price = shoppingCartList.get(j).getPrice();
                        mAllPrice = mAllPrice + (count * price);
                        mNum = mNum + count;

                        int commodityId = shoppingCartList.get(j).getCommodityId();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("commodityId", String.valueOf(commodityId));
                        jsonObject.put("amount", String.valueOf(count));
                        mJSONArray.put(jsonObject);
                    }

                }
            }

            mOrderShopCarAdapter.setList(mShopList);
            mOrderDesc.setText(Html.fromHtml("共<font color='#ff5f71'>" + mNum + "" +
                    "</font>件商品，需付款<font color='#ff5f71'>" + mAllPrice + "</font>元"));

            mSubmitOrderAddressAdapter.setOnSelectedDataListener(new SubmitOrderAddressAdapter.OnSelectedDataListener() {
                @Override
                public void selectedData(AddressBean.ResultBean bean) {
                    mAddress = bean.getId() + "";
                    mOrderName.setText(bean.getRealName());
                    mOrderPhone.setText(bean.getPhone());
                    mOrderAddress.setText(bean.getAddress());

                    mAddressRecycler.setVisibility(View.GONE);
                    mAddressDown.setImageResource(R.drawable.address_down);
                    mIsDown = true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        setShowTitle(false);
        setTitle("确认订单");
        isShowBack(true);
        setWindowTitleBlack(true);
        mOrderName = (TextView) get(R.id.order_name);
        mOrderPhone = (TextView) get(R.id.order_phone);
        mOrderAddress = (TextView) get(R.id.order_address);
        mAddressRecycler = (RecyclerView) get(R.id.order_rexycler);
        mAddressDown = (ImageView) get(R.id.address_down);
        mLayoutAddress = (RelativeLayout) get(R.id.layout_add_address);
        mParamsHeight = (RelativeLayout) get(R.id.address_layout);
        mRecyclerOrderList = (RecyclerView) get(R.id.order_rexycler_list);
        mOrderDesc = (TextView) get(R.id.tv_order_desc);
        mViewAddress = get(R.id.order_head);
        mAddressDown.setOnClickListener(this);
        mLayoutAddress.setOnClickListener(this);
        get(R.id.submit_order).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_submit_order;
    }

    private void doAddressHttp() {
        initHeadMap();
        net(false, false, AddressBean.class)
                .get(0, Api.QUERY_ADDRESS_URL, null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        doAddressHttp();
    }

    private boolean mIsNoAddress = true;

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            if (type == 0) {
                AddressBean addressBean = (AddressBean) o;
                if ("0000".equals(addressBean.getStatus())) {
                    List<AddressBean.ResultBean> resultAddress = addressBean.getResult();
                    if (resultAddress == null) {
                        mIsNoAddress = true;
                        mLayoutAddress.setVisibility(View.VISIBLE);
                        mViewAddress.setVisibility(View.GONE);
                        return;
                    }
                    for (int i = 0; i < resultAddress.size(); i++) {
                        AddressBean.ResultBean bean = resultAddress.get(i);
                        if (bean.getWhetherDefault() == 1) {
                            mAddress = bean.getId() + "";
                            mOrderName.setText(bean.getRealName());
                            mOrderPhone.setText(bean.getPhone());
                            mOrderAddress.setText(bean.getAddress());
                        }
                    }
                    int paramsHeight = 100;
                    if (resultAddress.size() == 1) {
                        mAddressDown.setVisibility(View.GONE);
                        paramsHeight = 80;
                    }

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mParamsHeight.getLayoutParams();
                    params.height = Util.dip2px(this, paramsHeight);
                    mParamsHeight.setLayoutParams(params);

                    if (resultAddress.isEmpty()) {
                        mIsNoAddress = true;
                        mLayoutAddress.setVisibility(View.VISIBLE);
                        mViewAddress.setVisibility(View.GONE);
                    } else {
                        mIsNoAddress = false;
                        mLayoutAddress.setVisibility(View.GONE);
                        mViewAddress.setVisibility(View.VISIBLE);
                    }
                    mSubmitOrderAddressAdapter.setList(resultAddress);
                }
            } else if (type == 1) {
                AppBean appBean = (AppBean) o;
                if ("0000".equals(appBean.getStatus())) {
                    //成功创建订单 跳到确认订单页
                    new BottomDialog(this)
                            .setContent("请在2小时内完成支付 金额<font color='#ff5f71'>¥" + mAllPrice + "</font>")
                            .showDialog().setOnClickListener(new BottomDialog.OnClickListener() {
                        @Override
                        public void click() {
                            doOrderList(0);
                        }
                    });
                } else {
                    toast(appBean.getMessage());
                }
            } else if (type == 2) {
                AppBean appBean = (AppBean) o;
                toast(appBean.getMessage());
                if ("0000".equals(appBean.getStatus())) {
                    AppUtils.mCommodityByOrder = 1;
                    setResult(10001);
                    finish();
                }
            } else if (type == 3) {
                OrderAllBean orderAllBean = (OrderAllBean) o;
                if ("0000".equals(orderAllBean.getStatus())) {
                    String orderId = orderAllBean.getOrderList().get(0).getOrderId();
                    initHeadMap();
                    Map<String, String> mapPay = new HashMap<>();
                    mapPay.put("orderId", orderId);
                    mapPay.put("payType", "1");
                    net(false, false, AppBean.class)
                            .post(2, Api.PAY_URL, mapPay);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_down://下拉
                if (mIsDown) {
                    mIsDown = false;
                    mAddressRecycler.setVisibility(View.VISIBLE);
                    mAddressDown.setImageResource(R.drawable.address_up);
                } else {
                    mAddressRecycler.setVisibility(View.GONE);
                    mAddressDown.setImageResource(R.drawable.address_down);
                    mIsDown = true;
                }
                break;
            case R.id.layout_add_address:
                AppUtils.start(this, AddAddressActivity.class);
                break;
            case R.id.submit_order://提交订单
                if (TextUtils.isEmpty(mAddress)) {
                    toast("请选择收货地址");
                    return;
                }
                if (mIsNoAddress) {
                    toast("请选择收货地址");
                    return;
                }
                initHeadMap();
                Map<String, String> map = new HashMap<>();
                map.put("orderInfo", mJSONArray.toString());
                map.put("totalPrice", String.valueOf(mAllPrice));
                map.put("addressId", mAddress);
                net(false, false, AppBean.class).post(1, Api.CREATE_ORDER_URL, map);
                break;

        }
    }

    private void doOrderList(int status) {
        initHeadMap();
        Map<String, String> map = new HashMap<>();
        map.put("status", String.valueOf(status));
        map.put("page", String.valueOf(1));
        map.put("count", "5");

        net(false, false, OrderAllBean.class)
                .get(3, Api.QUERY_ORDER_URL, map);
    }


}

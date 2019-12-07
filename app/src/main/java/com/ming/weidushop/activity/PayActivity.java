package com.ming.weidushop.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.ming.weidushop.R;

import java.util.HashMap;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/9
 * 支付
 */
public class PayActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView mMoneySelect, mWeiXinSelect, mZhiFuBaoSelect;
    private Button mBtnPay;
    private String mPrice, mId, mMessage;
    private RelativeLayout mLayoutSuccessFail, mLayoutFail;


    @Override
    protected void initData() {
        mPrice = getIntent().getStringExtra("price");
        mId = getIntent().getStringExtra("id");
        settingImage(0, mMoneySelect, mWeiXinSelect, mZhiFuBaoSelect);
    }

    @Override
    protected void initView() {
        setShowTitle(false);
        setTitle("支付");
        isShowBack(true);
        setWindowTitleBlack(true);
        mMoneySelect = (ImageView) get(R.id.iv_money_select);
        mWeiXinSelect = (ImageView) get(R.id.iv_weixin_select);
        mZhiFuBaoSelect = (ImageView) get(R.id.iv_zhifubao_select);
        mLayoutSuccessFail = (RelativeLayout) get(R.id.layout_success_fail);
        mLayoutFail = (RelativeLayout) get(R.id.layout_fail_fail);
        mBtnPay = (Button) get(R.id.btn_pay);
        mMoneySelect.setOnClickListener(this);
        mWeiXinSelect.setOnClickListener(this);
        mZhiFuBaoSelect.setOnClickListener(this);
        mBtnPay.setOnClickListener(this);
        get(R.id.order_back_home).setOnClickListener(this);
        get(R.id.order_see).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_money_select:
                settingImage(0, mMoneySelect, mWeiXinSelect, mZhiFuBaoSelect);
                break;
            case R.id.iv_weixin_select:
                settingImage(1, mWeiXinSelect, mMoneySelect, mZhiFuBaoSelect);
                break;
            case R.id.iv_zhifubao_select:
                settingImage(2, mZhiFuBaoSelect, mMoneySelect, mWeiXinSelect);
                break;
            case R.id.btn_pay://支付
                initHeadMap();
                Map<String, String> map = new HashMap<>();
                map.put("orderId", mId);
                map.put("payType", "1");
                net(false, false, AppBean.class)
                        .post(0, Api.PAY_URL, map);
                break;
            case R.id.order_back_home://返回主页
                finish();
                break;
            case R.id.order_see:
                finish();
                break;
        }
    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            AppBean appBean = (AppBean) o;
            toast(appBean.getMessage());

            if ("0000".equals(appBean.getStatus())) {
                //支付成功
                mLayoutSuccessFail.setVisibility(View.VISIBLE);
            } else {
                //支付失败
                mLayoutFail.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void settingImage(int type, View view, View view1, View view2) {
        switch (type) {
            case 0:
                mMessage = "余额";
                break;
            case 1:
                mMessage = "微信";
                break;
            case 2:
                mMessage = "支付宝";
                break;
        }
        view.setBackgroundResource(R.drawable.shop_car_all);
        view1.setBackgroundResource(R.drawable.shop_car_cricle);
        view2.setBackgroundResource(R.drawable.shop_car_cricle);
        mBtnPay.setText(mMessage + "支付" + mPrice + "元");
    }
}

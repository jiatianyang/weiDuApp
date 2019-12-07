package com.ming.weidushop.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.utils.SharedPreUtils;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.AddressAdapter;
import com.ming.weidushop.bean.AddressBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/9
 * 我的收获地址
 */
public class MyAddressActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private static final String TAG = MyAddressActivity.class.getName();
    private RecyclerView mRecycler;
    private AddressAdapter mAddressAdapter;
    private List<AddressBean.ResultBean> mResultAddress = new ArrayList<>();

    @Override
    protected void initData() {
        setShowTitle(false);
        isShowBack(false);
        setTitle("我的收货地址");
        setWindowTitleBlack(true);
        getRightTextView().setText("完成");
        getRightTextView().setOnClickListener(this);
        get(R.id.address_add).setOnClickListener(this);

        mAddressAdapter = new AddressAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(mAddressAdapter);
        doAddressHttp();
        mAddressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int id, int type, int position) {
                initHeadMap();
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));

                if (type == 0) {
                    net(false, false, AppBean.class)
                            .post(1, Api.SETTING_ADDRESS_URL, map);
                } else {
                    mResultAddress.remove(position);
                    mAddressAdapter.setList(mResultAddress);
                    net(false, false, AppBean.class)
                            .delete(2, Api.DELETE_ADDRESS_URL, map);
                }

            }
        });

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

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            if (type == 0) {
                AddressBean addressBean = (AddressBean) o;
                if ("0000".equals(addressBean.getStatus())) {
                    mResultAddress = addressBean.getResult();
                    if (mResultAddress != null) {
                        mAddressAdapter.setList(mResultAddress);
                    } else {
                        toast(addressBean.getMessage());
                    }

                }
            } else if (type == 1) {
                AppBean appBean = (AppBean) o;
                if ("0000".equals(appBean.getStatus())) {
                    doAddressHttp();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initView() {
        mRecycler = (RecyclerView) get(R.id.recycler);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_right://右边
                if (mResultAddress == null || mResultAddress.isEmpty()) {
                    setResult(1001);
                    finish();
                    return;
                }
                for (int i = 0; i < mResultAddress.size(); i++) {
                    if (mResultAddress.get(i).getWhetherDefault() == 1) {
                        int id = mResultAddress.get(i).getId();
                        SharedPreUtils.put(this, "adaress", String.valueOf(id));
                    }
                }
                setResult(1001);
                finish();
                break;
            case R.id.address_add://新增收获地址
                AppUtils.start(this, AddAddressActivity.class);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toast("请点击完成");
            return false;
        }
        return true;
    }
}

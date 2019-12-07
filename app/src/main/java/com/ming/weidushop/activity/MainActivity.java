package com.ming.weidushop.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.BaseFragment;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.upload.customview.ConfirmDialog;
import com.abner.ming.base.upload.feature.Callback;
import com.abner.ming.base.upload.util.UpdateAppReceiver;
import com.abner.ming.base.upload.util.UpdateAppUtils;
import com.abner.ming.base.utils.ToastUtils;
import com.ming.weidushop.BuildConfig;
import com.ming.weidushop.R;
import com.ming.weidushop.adapter.MainPagerAdapter;
import com.ming.weidushop.bean.ShopCarBean;
import com.ming.weidushop.fragment.OrderFragment;
import com.ming.weidushop.fragment.ShopCarFragment;
import com.ming.weidushop.utils.AppUtils;
import com.ming.weidushop.view.BottomTabView;
import com.ming.weidushop.view.NoScrollViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * author:AbnerMing
 * date:2019/9/3
 */
public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {
    //选中
    private int[] mTabSelected = {
            R.drawable.tab_home_bottom_home_y, R.drawable.tab_home_bottom_circle_y
            , R.drawable.tab_home_bottom_shop_car, R.drawable.tab_home_bottom_order_y
            , R.drawable.tab_home_bottom_account_y

    };
    //未选中
    private int[] mTabNormal = {
            R.drawable.tab_home_bottom_home_n, R.drawable.tab_home_bottom_circle_n
            , R.drawable.tab_home_bottom_shop_car, R.drawable.tab_home_bottom_order_n
            , R.drawable.tab_home_bottom_account_n

    };
    private BottomTabView mBottomTabView;
    private NoScrollViewPager mViewPager;
    private RelativeLayout mLayoutCar, mLayoutOrder;
    private ImageView mAllCricle;
    private MainPagerAdapter mMainPagerAdapter;
    private TextView mAllPrice;
    private boolean mIsAll = true;
    private List<ShopCarBean.ResultBean> mShopList = new ArrayList<>();
    private int mVersionNum;
    private String mDescription, mDownloadUrl;
    private UpdateAppReceiver mUpdateAppReceiver;

    @Override
    protected void onRestart() {
        super.onRestart();
        mainReStart();
    }


    @Override
    protected void initData() {
        mViewPager = (NoScrollViewPager) get(R.id.viewpager);
        mBottomTabView = (BottomTabView) get(R.id.bottom_view);
        mBottomTabView.setTab(mTabSelected, mTabNormal, 0);

        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainPagerAdapter);

        mBottomTabView.bindTab(mViewPager);//绑定

        mViewPager.setOffscreenPageLimit(5);

        setWindowBaseViewBlack(false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 4 || i == 0) {
                    setWindowBaseViewBlack(false);
                } else {
                    setWindowBaseViewBlack(true);
                }
                if (i == 2) {
                    mLayoutCar.setVisibility(View.VISIBLE);
                } else {
                    mLayoutCar.setVisibility(View.GONE);
                }
                Fragment fragment = mMainPagerAdapter.getItem(i);
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).refreshData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        doVersionApk();

    }

    //请求apk地址
    private void doVersionApk() {
        Map<String, String> map = new HashMap<>();
        map.put("appVersionNum", BuildConfig.VERSION_CODE + "");
        net(false, false).get(0, Api.UPLOAD_URL, map);
    }

    @Override
    public void success(int type, String data) {
        super.success(type, data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String status = jsonObject.getString("status");
            if ("0000".equals(status)) {//成功
                int flag = jsonObject.getInt("flag");
                if (1 == flag) {//需要更新
                    JSONObject result = jsonObject.getJSONObject("result");
                    mDescription = result.getString("description");
                    mDownloadUrl = result.getString("downloadUrl");
                    mVersionNum = result.getInt("versionNum");
                    checkPermissions();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initView() {
        mLayoutCar = (RelativeLayout) get(R.id.layout_shop_car);
        mAllCricle = (ImageView) get(R.id.iv_cricle);
        mAllPrice = (TextView) get(R.id.tv_all_price);
        mLayoutOrder = (RelativeLayout) get(R.id.submit_order);
        get(R.id.layout_all).setOnClickListener(this);
        mLayoutOrder.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_all://全选
                if (!mShopListAllClick) {
                    ToastUtils.show("暂时没有商品哦");
                    return;
                }
                Fragment fragment = mMainPagerAdapter.getItem(2);
                if (fragment instanceof ShopCarFragment) {
                    if (mIsAll) {
                        mIsAll = false;
                        mAllCricle.setImageDrawable(getResources().getDrawable(R.drawable.shop_car_all));
                    } else {
                        mIsAll = true;
                        mAllCricle.setImageDrawable(getResources().getDrawable(R.drawable.shop_car_cricle));
                    }
                    ((ShopCarFragment) fragment).setAll(!mIsAll);
                }
                break;
            case R.id.submit_order://去结算
                doBuy();
                break;
        }
    }

    /**
     * 去结算
     */
    private void doBuy() {
        if (!mShopListAllClick) {
            return;
        }
        if (mPrice == 0) {
            toast("请选择商品");
            return;
        }

        AppUtils.startObject(MainActivity.this, SubmitOrderActivity.class, mShopList);
    }

    //总的价格
    private float mPrice;

    public void setAllPrice(float allPrice) {
        mPrice = allPrice;
        mAllPrice.setText("¥" + allPrice);
    }

    public void setAllNum(boolean b) {
        if (b) {
            mAllCricle.setImageDrawable(getResources().getDrawable(R.drawable.shop_car_all));
        } else {
            mAllCricle.setImageDrawable(getResources().getDrawable(R.drawable.shop_car_cricle));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            doBuy();
        }

    }


    //购物车传递过来的数据
    private boolean mShopListAllClick;

    public void changeShopList(int shopListSize) {
        if (shopListSize == 0) {
            mShopListAllClick = false;
            mIsAll = true;
            mLayoutOrder.setBackgroundColor(getResources().getColor(R.color.color_969));
            mAllCricle.setImageDrawable(getResources().getDrawable(R.drawable.shop_car_cricle));
            mAllPrice.setText("¥0");
            mPrice = 0;
        } else {
            mShopListAllClick = true;
            mLayoutOrder.setBackgroundColor(getResources().getColor(R.color.colorFF5F71));
        }
    }

    public void setShopList(List<ShopCarBean.ResultBean> mShopCarList) {
        this.mShopList = mShopCarList;
    }

    private int mClick = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mClick == 0) {
                ToastUtils.show("请再按一次退出");
                mClick++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mClick = 0;
                    }
                }, 2000);
            } else {
                finish();
            }
        }
        return false;
    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            uploadApk();
        } else {//申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    //更新APK
    private void uploadApk() {
        UpdateAppUtils.from(this)
                .setConfig(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
                .serverVersionCode(mVersionNum)
                .serverVersionName(mVersionNum + "")
                .updateInfo(mDescription)
                .apkPath(mDownloadUrl)
                .isForce(true)
                .update();
    }

    //权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadApk();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            if (position == 1) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                startActivity(intent);
                            }
                        }
                    }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
                }
                break;
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        try {

            mUpdateAppReceiver = new UpdateAppReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("teprinciple.update");
            registerReceiver(mUpdateAppReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpdateAppReceiver != null) {
            unregisterReceiver(mUpdateAppReceiver);
        }
    }

    //页面重新可见
    private void mainReStart() {
        //购物车支付后切换到订单
        if(AppUtils.mCommodityByOrder==1){
            mViewPager.setCurrentItem(3);
            mBottomTabView.setTab(mTabSelected, mTabNormal, 3);
            Fragment item = mMainPagerAdapter.getItem(3);
            if (item instanceof OrderFragment) {
                ((OrderFragment) item).changeData();
            }
        }
        changeShopList(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtils.mCommodityByOrder=0;
    }
}

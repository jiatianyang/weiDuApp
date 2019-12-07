package com.abner.ming.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abner.ming.base.model.LoginBean;
import com.abner.ming.base.mvp.model.BaseModelIml;
import com.abner.ming.base.mvp.presenter.BasePresenterIml;
import com.abner.ming.base.mvp.view.BaseView;
import com.abner.ming.base.utils.Utils;
import com.abner.ming.base.utils.WindowUtils;
import com.abner.ming.base.view.UltimateBar;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有的Activity的父类
 * author:AbnerMing
 * date:2019/4/18
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseView {
    private RelativeLayout mTitleLayout,mBaseBack;
    private TextView mBaseTitle, mTitleRight;
    private BasePresenterIml mBasePresenterIml;
    private LinearLayout mBaseLayout,mBaseView;
    private Map<String, String> mHeadMap = new HashMap<>();

    //获取右侧
    public TextView getRightTextView(){
        mTitleRight.setVisibility(View.VISIBLE);
        return mTitleRight;
    }

    /**
     * 设置标题
     */
    protected void setTitle(String title) {
        mBaseTitle.setText(title);
    }

    //是否显示返回键
    protected void isShowBack(boolean showBack) {
        if (showBack) {
            mBaseBack.setVisibility(View.VISIBLE);
        } else {
            mBaseBack.setVisibility(View.GONE);
        }
    }

    protected void setShowTitle(boolean isShow) {
        if (isShow) {
            mTitleLayout.setVisibility(View.GONE);
        } else {
            mTitleLayout.setVisibility(View.VISIBLE);
        }
    }


    public void setSystemWindow(boolean isWindow) {
        mBaseLayout.setFitsSystemWindows(isWindow);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setApply();
        mBaseLayout = (LinearLayout) findViewById(R.id.base_layout);
        mTitleLayout = (RelativeLayout) findViewById(R.id.base_layout_title);
        mBaseTitle = (TextView) findViewById(R.id.base_title);
        mBaseBack = (RelativeLayout) findViewById(R.id.base_view_back);
        mTitleRight = (TextView) findViewById(R.id.base_title_right);

        //创建用于添加子类传递的布局
        mBaseView = (LinearLayout) findViewById(R.id.base_view);
        //拿到子类布局
        View childView = View.inflate(this, getLayoutId(), null);

        mBaseView.addView(childView);

        initView();

        initData();

        mBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //初始化头参
    public void initHeadMap() {
        LoginBean.ResultBean bean = Utils.getUserInFo();
        if (bean != null) {
            mHeadMap.put("userId", String.valueOf(bean.getUserId()));
            mHeadMap.put("sessionId", bean.getSessionId());
        }
    }


    //初始化View
    protected abstract void initData();

    //初始化View
    protected abstract void initView();

    //子类传递的一个layout
    public abstract int getLayoutId();

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //获取BasePresenterIml  null为获取字符串，获取JavaBean需要传JavaBean
    public BasePresenterIml getPresenter(Class cls) {
        BaseModelIml baseModel = new BaseModelIml(cls);
        baseModel.isShowLoading(isShowLoading);
        baseModel.isReadCache(isReadCache);
        baseModel.setContext(this);
        baseModel.setHead(mHeadMap);
        mBasePresenterIml = new BasePresenterIml(baseModel, this);
        return mBasePresenterIml;
    }

    //isShowLoading 是否显示加载框  isReadCache 是否阅读缓存  获取String
    protected BasePresenterIml net(boolean isShowLoading, boolean isReadCache) {
        return doHttp(null, isShowLoading, isReadCache);
    }

    //获取JavaBean
    protected BasePresenterIml net(boolean isShowLoading, boolean isReadCache, Class cls) {
        return doHttp(cls, isShowLoading, isReadCache);
    }

    protected BasePresenterIml doHttp(Class cls, boolean isShowLoading, boolean isReadCache) {
        isShowLoading(isShowLoading);
        isReadCache(isReadCache);
        return getPresenter(cls);
    }

    //是否读取缓存
    private boolean isReadCache;

    public void isReadCache(boolean isReadCache) {
        this.isReadCache = isReadCache;
    }

    //是否显示加载框
    private boolean isShowLoading;

    public void isShowLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }



    //设置头参
    public void setHead(Map<String, String> headMap) {
        this.mHeadMap = headMap;
    }

    @Override
    public void success(int type, String data) {

    }

    @Override
    public void successBean(int type, Object o) {

    }

    @Override
    public void fail(int type, String error) {

    }

    private SparseArray<View> sparseArray = new SparseArray<>();

    //用于获取控件的方法
    public View get(int id) {
        View view = sparseArray.get(id);
        if (view == null) {
            view = findViewById(id);
            sparseArray.put(id, view);
        }
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBasePresenterIml != null) {
            mBasePresenterIml.destory();
        }
    }

    /**
     * 改变状态栏颜色
     */
    public void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    //设置沉浸式
    public void setApply() {
        UltimateBar.newImmersionBuilder()
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    //设置状态栏颜色
    public void setWindowColor(int color) {
        UltimateBar.newTransparentBuilder()
                .statusColor(color)        // 状态栏颜色
                .applyNav(false)         // 是否应用到导航栏
                .build(this)
                .apply();
    }

    //设置状态栏颜色  带标题栏
    public void setWindowTitleBlack(boolean isBlack) {
        setAndroidNativeLightStatusBar(this, isBlack);
        setMarginTop(mTitleLayout, isBlack);
    }

    //设置状态栏颜色 不带
    public void setWindowBaseViewBlack(boolean isBlack) {
        setAndroidNativeLightStatusBar(this, isBlack);
        setMarginTop(mBaseView, isBlack);
    }

    public void setMarginTop(View view, boolean isHeight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (isHeight) {
            params.topMargin = WindowUtils.getStatusBarHeight(this);
        } else {
            params.topMargin = 0;
        }

        view.setLayoutParams(params);
    }
}

package com.ming.weidushop.fragment;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.BaseFragment;
import com.abner.ming.base.model.LoginBean;
import com.abner.ming.base.refresh.material.Util;
import com.abner.ming.base.utils.SharedPreUtils;
import com.abner.ming.base.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.BuildConfig;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.LoginActivity;
import com.ming.weidushop.activity.MyAddressActivity;
import com.ming.weidushop.activity.MyCircleActivity;
import com.ming.weidushop.activity.MyFootActivity;
import com.ming.weidushop.activity.MyInfoActivity;
import com.ming.weidushop.activity.MyPayActivity;
import com.ming.weidushop.adapter.AccountAdapter;
import com.ming.weidushop.bean.AccountBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * author:AbnerMing
 * date:2019/9/2
 * 我的
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView mRecycler;
    private List<AccountBean> mAccountBeanList = new ArrayList<>();
    private RelativeLayout mAccountLogin;
    private TextView mAccountInFo;
    private ImageView mAccountPic,mSimplePic;
    private AccountAdapter mAccountAdapter;


    @Override
    protected void initData() {
        mAccountLogin.setOnClickListener(this);
        mAccountAdapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                LoginBean.ResultBean infoBean = AppUtils.getUserInFo();
                if (infoBean == null) {
                    AppUtils.start(getActivity(), LoginActivity.class);
                } else {
                    switch (position) {
                        case 0://个人资料
                            AppUtils.start(getActivity(), MyInfoActivity.class);
                            break;
                        case 1://我的圈子
                            AppUtils.start(getActivity(), MyCircleActivity.class);
                            break;
                        case 2://我的足迹
                            AppUtils.start(getActivity(), MyFootActivity.class);
                            break;
                        case 3://我的钱包
                            AppUtils.start(getActivity(), MyPayActivity.class);
                            break;
                        case 4://我的地址
                            AppUtils.start(getActivity(), MyAddressActivity.class);
                            break;
                    }

                }
            }
        });
    }


    @Override
    protected void initView(View view) {
        mAccountPic = (ImageView) get(R.id.account_picture);
        mAccountInFo = (TextView) get(R.id.account_info);
        mRecycler = (RecyclerView) get(R.id.account_recycler);
        mAccountLogin = (RelativeLayout) get(R.id.account_login);
        mSimplePic = (ImageView) get(R.id.simple_pic);
        createData();
        mAccountAdapter = new AccountAdapter(getActivity(), mAccountBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(mAccountAdapter);
    }

    private void createData() {
        AccountBean accountBean = new AccountBean();
        accountBean.setName("个人资料");
        accountBean.setImage(R.drawable.account_informat);
        mAccountBeanList.add(accountBean);
        accountBean = new AccountBean();
        accountBean.setName("我的圈子");
        accountBean.setImage(R.drawable.account_circle);
        mAccountBeanList.add(accountBean);
        accountBean = new AccountBean();
        accountBean.setName("我的足迹");
        accountBean.setImage(R.drawable.account_foot);
        mAccountBeanList.add(accountBean);
        accountBean = new AccountBean();
        accountBean.setName("我的钱包");
        accountBean.setImage(R.drawable.account_walletn);
        mAccountBeanList.add(accountBean);
        accountBean = new AccountBean();
        accountBean.setName("我的收获地址");
        accountBean.setImage(R.drawable.account_address);
        mAccountBeanList.add(accountBean);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_login://登录
                LoginBean.ResultBean infoBean = AppUtils.getUserInFo();
                if (infoBean == null) {
                    AppUtils.start(getActivity(), LoginActivity.class);
                } else {
                    AppUtils.start(getActivity(), MyInfoActivity.class);
                }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginBean.ResultBean infoBean = AppUtils.getUserInFo();

        if (infoBean != null) {
            String nick = SharedPreUtils.getString(getActivity(), "nick");
            if (!TextUtils.isEmpty(nick)) {
                infoBean.setNickName(nick);
            }
            String head = SharedPreUtils.getString(getActivity(), "head");
            if (!TextUtils.isEmpty(head)) {
                infoBean.setHeadPic(head);
            }
            String nickName = infoBean.getNickName();
            mAccountInFo.setText(nickName);
            AppUtils.setGlide(getActivity(), infoBean.getHeadPic(), mSimplePic);
            AppUtils.setGlideCircle(getActivity(), infoBean.getHeadPic(), mAccountPic);
            //setImageHeight(200);

        } else {
            mAccountInFo.setText("请先登录");
            mAccountPic.setImageResource(R.drawable.account_logo);
            mSimplePic.setImageResource(R.drawable.account_bg);
           // setImageHeight(500);
        }
    }


    private void setImageHeight(int size) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSimplePic.getLayoutParams();
        params.height = Util.dip2px(getActivity(), size);
        mSimplePic.setLayoutParams(params);
    }


}

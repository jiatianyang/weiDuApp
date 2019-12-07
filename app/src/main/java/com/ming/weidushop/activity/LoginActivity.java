package com.ming.weidushop.activity;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.LoginBean;
import com.abner.ming.base.utils.CacheUtils;
import com.abner.ming.base.utils.SharedPreUtils;
import com.abner.ming.base.utils.Utils;
import com.google.gson.Gson;
import com.ming.weidushop.R;
import com.ming.weidushop.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/2
 * 登录
 */
public class LoginActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private EditText mPhone, mPass;
    private boolean mShowPass = true;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mPhone = (EditText) get(R.id.login_ed_phone);
        mPass = (EditText) get(R.id.login_ed_pass);
        get(R.id.login).setOnClickListener(this);
        get(R.id.login_register).setOnClickListener(this);
        get(R.id.logon_eye).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登录
                doLogin();
                break;
            case R.id.login_register://注册
                AppUtils.startResult(LoginActivity.this, RegisterActivity.class);
                break;
            case R.id.logon_eye://显示和隐藏
                if (mShowPass) {
                    mPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mShowPass = false;
                } else {
                    mPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mShowPass = true;
                }
                mPass.setSelection(mPass.getText().length());
                break;
        }
    }

    //登录
    private void doLogin() {
        String phone = mPhone.getText().toString().trim();
        String pass = mPass.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            toast("请输入手机号");
            return;
        }
        if (!Utils.isMobilePhone(phone)) {
            toast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            toast("请输入密码");
            return;
        }
        if (Utils.isPassWord(pass)) {
            toast("请输入正确的密码");
            return;
        }
        //pass = Utils.md5(pass);
        //走登录
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pwd", pass);
        net(false, false)
                .post(0, Api.LOGIN_URL, map);


    }

    @Override
    public void success(int type, String data) {
        super.success(type, data);
        try {
            LoginBean loginBean = new Gson().fromJson(data, LoginBean.class);
            toast(loginBean.getMessage());
            if ("0000".equals(loginBean.getStatus())) {
                CacheUtils.getCacheUtils().insert("login", data);//存储用户信息
                SharedPreUtils.put(LoginActivity.this, "islogin", "1");
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String phone = data.getStringExtra("phone");
            if (!TextUtils.isEmpty(phone)) {
                mPhone.setText(phone);//注册成功后回显
                finish();
            }
        }

    }
}

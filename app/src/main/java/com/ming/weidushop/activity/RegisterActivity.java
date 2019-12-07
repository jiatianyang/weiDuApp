package com.ming.weidushop.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.model.AppBean;
import com.abner.ming.base.model.LoginBean;
import com.abner.ming.base.utils.CacheUtils;
import com.abner.ming.base.utils.SharedPreUtils;
import com.abner.ming.base.utils.Utils;
import com.google.gson.Gson;
import com.ming.weidushop.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE;

/**
 * author:AbnerMing
 * date:2019/9/2
 * 注册
 */
public class RegisterActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private EditText mPhone, mPass, mRegisterCode;
    private boolean mShowPass = true;
    private TextView mCode;
    private Timer mTimer = new Timer();
    private int mTime = 61;
    private boolean mClick = true;
    private TimerTask getTimerTask() {
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                //要推迟执行的方法
                mTime--;
                if (mTime == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClick = true;
                            mTime = 61;
                            mCode.setText("重新获取");
                            mTimer.cancel();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClick = false;
                            mCode.setText(mTime + "秒");
                        }
                    });
                }
            }
        };
        return mTask;
    }


    @Override
    protected void initData() {
//注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    protected void initView() {
        mPhone = (EditText) get(R.id.register_ed_phone);
        mPass = (EditText) get(R.id.register_ed_pass);
        mRegisterCode = (EditText) get(R.id.register_ed_code);

        mCode = (TextView) get(R.id.tv_code);
        get(R.id.register).setOnClickListener(this);
        get(R.id.login_register).setOnClickListener(this);
        get(R.id.register_eye).setOnClickListener(this);
        mCode.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register://注册
                doRegister();
                break;
            case R.id.login_register://注册
                finish();
                break;
            case R.id.register_eye://显示和隐藏

                if (mShowPass) {
                    mPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mShowPass = false;
                } else {
                    mPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mShowPass = true;
                }
                mPass.setSelection(mPass.getText().length());
                break;
            case R.id.tv_code://获取验证码
                String mPhoneNum = mPhone.getText().toString().trim();
                if (TextUtils.isEmpty(mPhoneNum)) {
                    toast("请输入手机号");
                    return;
                }
                if (!Utils.isMobilePhone(mPhoneNum)) {
                    toast("请输入正确的手机号");
                    return;
                }
                if (mClick) {
                    toast("验证码已发送，请注意查收");
                    mTimer = new Timer();
                    mTimer.schedule(getTimerTask(), 0, 1000);
                }
                // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
                SMSSDK.getVerificationCode("86", mPhoneNum);
                break;
        }
    }

    private String mPhoneNum, mPassWord;

    private void doRegister() {
        mPhoneNum = mPhone.getText().toString().trim();
        mPassWord = mPass.getText().toString().trim();
        String code = mRegisterCode.getText().toString().trim();
        if (TextUtils.isEmpty(mPhoneNum)) {
            toast("请输入手机号");
            return;
        }
        if (!Utils.isMobilePhone(mPhoneNum)) {
            toast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            toast("请输入验证码");
            return;
        }
        // 提交验证码，其中的code表示验证码，如“1357”
        SMSSDK.submitVerificationCode("86", mPhoneNum, code);


    }

    private void doRegisterCode() {
        if (TextUtils.isEmpty(mPassWord)) {
            toast("请输入密码");
            return;
        }
        if (Utils.isPassWord(mPassWord)) {
            toast("请输入字母或字母+数字组合的密码");
            return;
        }
        if (mPassWord.length() < 6 || mPassWord.length() > 12) {
            toast("请输入6—12位密码");
            return;
        }


        //  mPassWord = Utils.md5(mPassWord);
        //走注册
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhoneNum);
        map.put("pwd", mPassWord);
        net(false, false, AppBean.class)
                .post(0, Api.REGISTER_URL, map);
    }

    @Override
    public void successBean(int type, Object o) {
        super.successBean(type, o);
        try {
            if (type == 0) {
                AppBean appBean = (AppBean) o;
                toast(appBean.getMessage());
                if (appBean.getStatus().equals("0000")) {
                    //走登录
                    doLogin(mPhoneNum, mPassWord);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(int type, String data) {
        super.success(type, data);
        if (type == 1) {
            LoginBean loginBean = new Gson().fromJson(data, LoginBean.class);
            toast(loginBean.getMessage());
            if ("0000".equals(loginBean.getStatus())) {
                CacheUtils.getCacheUtils().insert("login", data);//存储用户信息
                SharedPreUtils.put(RegisterActivity.this, "islogin", "1");
                Intent intent = new Intent();
                intent.putExtra("phone", mPhoneNum);
                setResult(1000, intent);
                finish();
            }

        }
    }

    //登录
    private void doLogin(String mPhoneNum, String mPassWord) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhoneNum);
        map.put("pwd", mPassWord);
        net(false, false)
                .post(1, Api.LOGIN_URL, map);
    }

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            // TODO 此处不可直接处理UI线程，处理后续操作需传到主线程中操作
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            if (event == EVENT_SUBMIT_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    doRegisterCode();
                } else {
                    toast("短信验证失败");
                }
            }
        }
    };

    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}

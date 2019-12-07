package com.ming.weidushop.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.abner.ming.base.utils.ToastUtils;
import com.ming.weidushop.R;

/**
 * author:AbnerMing
 * date:2019/9/15
 */
public class BottomDialog {
    private Activity mContext;
    private Dialog mDialog;
    private String mContent;

    public BottomDialog(Activity context) {
        this.mContext = context;
    }

    public BottomDialog setContent(String desc) {
        mContent = desc;
        return this;
    }

    public BottomDialog showDialog() {
        View localView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_bottom_layout, null);

        mDialog = new Dialog(mContext, R.style.ModifyDialog);
        mDialog.setContentView(localView);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);

        localView.findViewById(R.id.dialog_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        TextView mTextDesc = (TextView) localView.findViewById(R.id.tv_money_desc);
        mTextDesc.setText(Html.fromHtml(mContent));
        localView.findViewById(R.id.iv_weixin_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("暂不支持微信支付");
            }
        });
        localView.findViewById(R.id.iv_zhifubao_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("暂不支持支付宝支付");
            }
        });
        localView.findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.click();
                }
            }
        });
        // 设置全屏
        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        mDialog.getWindow().setAttributes(lp);
        mDialog.show();
        return this;
    }

    private OnClickListener mOnClickListener;

    public BottomDialog setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        return this;
    }

    public void cancle() {
        if(mDialog!=null){
            mDialog.dismiss();
        }
    }

    public interface OnClickListener {
        void click();
    }
}

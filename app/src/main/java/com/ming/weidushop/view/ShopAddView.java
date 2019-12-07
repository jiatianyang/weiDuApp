package com.ming.weidushop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ming.weidushop.R;


/**
 * author:AbnerMing
 * date:2019/9/5
 * 自定义加减器
 */
public class ShopAddView extends RelativeLayout implements View.OnClickListener {
    private EditText mEdit;
    private TextView mTextDelete, mTextAdd;

    public ShopAddView(Context context) {
        super(context);
        init(context);
    }

    public ShopAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private Context context;

    private void init(Context context) {
        this.context = context;
        View view = View.inflate(context, R.layout.view_shop_add, null);
        mEdit = (EditText) view.findViewById(R.id.shop_edit);
        mTextDelete = (TextView) view.findViewById(R.id.shop_delete);
        mTextDelete.setOnClickListener(this);
        mTextAdd = (TextView) view.findViewById(R.id.shop_add);
        mTextAdd.setOnClickListener(this);
        addView(view);
    }

    //传递数量
    public void setCount(int count) {
        mEdit.setText(count + "");
        mEdit.setSelection(mEdit.getText().toString().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop_delete:
                String count = mEdit.getText().toString().trim();
                int count1 = Integer.parseInt(count);
                if (count1 <= 1) {
                    Toast.makeText(context, "您至少有一个商品", Toast.LENGTH_LONG).show();
                    return;
                }
                count1--;
                mEdit.setText(count1 + "");
                if (mOnNumListener != null) {
                    mOnNumListener.count(count1);
                }
                break;
            case R.id.shop_add:
                String countAdd = mEdit.getText().toString().trim();
                int countAdd1 = Integer.parseInt(countAdd);
                countAdd1++;
                mEdit.setText(countAdd1 + "");
                if (mOnNumListener != null) {
                    mOnNumListener.count(countAdd1);
                }
                break;
        }
    }

    private OnNumListener mOnNumListener;

    public void setOnNumListener(OnNumListener mOnNumListener) {
        this.mOnNumListener = mOnNumListener;
    }


    public void setEditCancle() {
        mEdit.setEnabled(false);
        mTextAdd.setEnabled(false);
        mTextDelete.setEnabled(false);
    }

    public interface OnNumListener {
        void count(int count);
    }
}

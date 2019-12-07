package com.ming.weidushop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ming.weidushop.R;

/**
 * author:AbnerMing
 * date:2019/9/5
 * 搜索自定义View
 */
public class SearchView extends RelativeLayout {
    private EditText mEdit;

    public SearchView(Context context) {
        super(context);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.layout_search, null);
        mEdit = (EditText) view.findViewById(R.id.search_edit);
        view.findViewById(R.id.tv_serach).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEdit.getText().toString();
                if (mOnSerachListener != null) {
                    mOnSerachListener.search(message);
                }
            }
        });
        view.findViewById(R.id.search_setting).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnSerachListener != null) {
                    mOnSerachListener.setting();
                }
            }
        });

        addView(view);
    }

    private OnSerachListener mOnSerachListener;

    public void setOnSerachListener(OnSerachListener mOnSerachListener) {
        this.mOnSerachListener = mOnSerachListener;
    }

    public interface OnSerachListener {
        void search(String content);
        void setting();
    }

}

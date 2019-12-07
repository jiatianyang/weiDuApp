package com.ming.weidushop.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ming.weidushop.R;

/**
 * author:AbnerMing
 * date:2019/9/4
 * 暂无数据
 */
public class LayoutDataNull extends RelativeLayout {
    public LayoutDataNull(Context context) {
        super(context);
        init(context);
    }

    public LayoutDataNull(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.layout_data_null, null);
        addView(view);
    }
}

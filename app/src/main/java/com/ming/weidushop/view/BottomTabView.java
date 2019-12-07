package com.ming.weidushop.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.ming.weidushop.R;

/**
 * author:AbnerMing
 * date:2019/9/2
 * 自定义底部按钮
 */
public class BottomTabView extends RelativeLayout {
    private LinearLayout mBottom;
    private Context mContext;
    private ViewPager mViewPager;

    public BottomTabView(Context context) {
        super(context);
        init(context);
    }

    public BottomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = View.inflate(context, R.layout.layout_bottom_tab, null);
        mBottom = (LinearLayout) view.findViewById(R.id.layout_bottom);
        addView(view);
    }

    //绑定ViewPage
    public void bindTab(ViewPager viewPager){
        this.mViewPager=viewPager;
    }

    //给子view设置点击事件
    private void setChildViewClick() {
        for (int a = 0; a < mBottom.getChildCount(); a++) {
            final int finalA = a;
            mBottom.getChildAt(a).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context, finalA + "", Toast.LENGTH_LONG).show();
                    setTab(tabsYes,tabsNo,finalA);
                    mViewPager.setCurrentItem(finalA);
                }
            });
        }
    }

    //设置Tab键
    private int[] tabsYes, tabsNo;

    public void setTab(int[] tabsYes, int[] tabsNo, int position) {
        this.tabsYes = tabsYes;
        this.tabsNo = tabsNo;
        mBottom.removeAllViews();
        for (int a = 0; a < tabsYes.length; a++) {
            View view = View.inflate(mContext, R.layout.layout_bottom_tab_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.tab_img);
            if (position == a) {
                imageView.setImageResource(tabsYes[a]);
            } else {
                imageView.setImageResource(tabsNo[a]);
            }
            mBottom.addView(view);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.weight = 1;
            if(a==2){
                params.topMargin=8;
            }
            view.setLayoutParams(params);
        }

        setChildViewClick();
    }

}

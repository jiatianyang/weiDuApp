package com.ming.weidushop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.refresh.material.Util;
import com.abner.ming.base.utils.DateUtils;
import com.abner.ming.base.utils.Utils;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.ImageViewActivity;
import com.ming.weidushop.bean.CircleBean;
import com.ming.weidushop.bean.CircleListBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/12
 */
public class MyCircleAdapter extends BaseRecyclerAdapter<CircleBean.ResultBean> {
    public MyCircleAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_circle_item;
    }

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder, CircleBean.ResultBean resultBean) {

    }

    @Override
    public void bindViewDataPosition(BaseViewHolder baseViewHolder, final CircleBean.ResultBean resultBean, final int position) {
        super.bindViewDataPosition(baseViewHolder, resultBean, position);
        baseViewHolder.get(R.id.layout_zan_my).setVisibility(View.GONE);
        baseViewHolder.setText(R.id.circle_time, DateUtils.getTime(resultBean.getCreateTime()));
        baseViewHolder.setText(R.id.tv_content, resultBean.getContent());
        baseViewHolder.setText(R.id.tv_zan_num, String.valueOf(resultBean.getGreatNum()));
        ImageView zanImage = (ImageView) baseViewHolder.get(R.id.iv_zan);
        ImageView circleCheck = (ImageView) baseViewHolder.get(R.id.circle_check);
        if (isShow) {
            circleCheck.setVisibility(View.VISIBLE);
        } else {
            circleCheck.setVisibility(View.GONE);
        }

        if (resultBean.isClick()) {
            circleCheck.setBackgroundResource(R.drawable.shop_car_all);
        } else {
            circleCheck.setBackgroundResource(R.drawable.shop_car_cricle);
        }

        circleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList().get(position).setClick(!getList().get(position).isClick());
                notifyDataSetChanged();
                List<CircleBean.ResultBean> list = getList();
                if (mOnCircleListener != null) {
                    mOnCircleListener.deleteList(list);
                }
            }
        });
        if (resultBean.getWhetherGreat() == 1) {
            zanImage.setImageResource(R.drawable.zan_ok);
        } else {
            zanImage.setImageResource(R.drawable.zan_no);
        }

        final String image = resultBean.getImage();
        String[] split = image.split(",");
        List<CircleListBean> circleListBeanList = new ArrayList<>();

        LinearLayout linearLayoutv = (LinearLayout) baseViewHolder.get(R.id.layout_list);
        LinearLayout linearLayouth = (LinearLayout) View.inflate(context, R.layout.adapter_circle_layout, null);
        linearLayoutv.removeAllViews();
        linearLayouth.removeAllViews();
        linearLayoutv.addView(linearLayouth);

        for (int i = 0; i < split.length; i++) {
            if (i == 3) {
                linearLayouth = (LinearLayout) View.inflate(context, R.layout.adapter_circle_layout, null);
                linearLayoutv.addView(linearLayouth);
            }
            ImageView viewIamge = (ImageView) View.inflate(context, R.layout.adapter_list_image, null);
            AppUtils.setGlide(context, split[i], viewIamge);
            linearLayouth.addView(viewIamge);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewIamge.getLayoutParams();
            if (split.length == 1) {
                int width = Utils.getDisplayMetrics(context)[0] / 2;
                layoutParams.width = width;
                layoutParams.height = Util.dip2px(context, 200);
            } else if (split.length == 2) {
                layoutParams.weight = 1;
                layoutParams.rightMargin = Util.dip2px(context, 10);
                layoutParams.height = Util.dip2px(context, 200);
            } else {
                if (i > 2) {
                    int width = Utils.getDisplayMetrics(context)[0] - Util.dip2px(context, 62);
                    layoutParams.rightMargin = Util.dip2px(context, 10);
                    layoutParams.width = width / 3;
                    layoutParams.height = Util.dip2px(context, 120);
                    layoutParams.topMargin = Util.dip2px(context, 10);
                } else {
                    layoutParams.weight = 1;
                    layoutParams.rightMargin = Util.dip2px(context, 10);
                    layoutParams.height = Util.dip2px(context, 120);
                }

            }

            viewIamge.setLayoutParams(layoutParams);
            final int finalI = i;
            viewIamge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> map = new HashMap<>();
                    map.put("image", image);
                    map.put("imagePosition", String.valueOf(finalI));
                    AppUtils.startString(context, ImageViewActivity.class, map);
                }
            });
        }

        zanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resultBean.getWhetherGreat() != 1) {
                    getList().get(position).setWhetherGreat(1);
                    getList().get(position).setGreatNum(resultBean.getGreatNum() + 1);
                    notifyDataSetChanged();
                    int id = resultBean.getId();
                    if (mOnCircleListener != null) {
                        mOnCircleListener.circle(id);
                    }
                } else {
                    getList().get(position).setWhetherGreat(2);
                    getList().get(position).setGreatNum(resultBean.getGreatNum() - 1);
                    notifyDataSetChanged();
                    int id = resultBean.getId();
                    if (mOnCircleListener != null) {
                        mOnCircleListener.cancle(id);
                    }
                }


            }
        });
    }

    private OnCircleListener mOnCircleListener;

    public void setOnCircleListener(OnCircleListener mOnCircleListener) {
        this.mOnCircleListener = mOnCircleListener;
    }

    private boolean isShow;

    public void setShowDelete(boolean b) {
        isShow = b;
        notifyDataSetChanged();
    }

    public interface OnCircleListener {
        void circle(int id);

        void cancle(int id);

        void deleteList(List<CircleBean.ResultBean> list);
    }
}

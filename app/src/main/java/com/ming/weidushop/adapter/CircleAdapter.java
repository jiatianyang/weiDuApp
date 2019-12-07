package com.ming.weidushop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.abner.ming.base.adapter.BaseRecyclerAdapter;
import com.abner.ming.base.refresh.material.Util;
import com.abner.ming.base.utils.DateUtils;
import com.abner.ming.base.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.ImageViewActivity;
import com.ming.weidushop.bean.CircleBean;
import com.ming.weidushop.bean.CircleListBean;
import com.ming.weidushop.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * author:AbnerMing
 * date:2019/9/4
 */
public class CircleAdapter extends BaseRecyclerAdapter<CircleBean.ResultBean> {
    public CircleAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_circle_item;
    }

    @Override
    public void bindViewDataPosition(BaseViewHolder baseViewHolder, final CircleBean.ResultBean resultBean,
                                     final int position) {
        super.bindViewDataPosition(baseViewHolder, resultBean, position);
        baseViewHolder.setText(R.id.circle_name, resultBean.getNickName());
        baseViewHolder.setText(R.id.circle_time, DateUtils.getTime(resultBean.getCreateTime()));
        baseViewHolder.setText(R.id.tv_content, resultBean.getContent());
        baseViewHolder.setText(R.id.tv_zan_num, String.valueOf(resultBean.getGreatNum()));
        ImageView zanImage = (ImageView) baseViewHolder.get(R.id.iv_zan);
        if (resultBean.getWhetherGreat() == 1) {
            zanImage.setImageResource(R.drawable.zan_ok);
        } else {
            zanImage.setImageResource(R.drawable.zan_no);
        }
        ImageView simpleDraweeView = (ImageView) baseViewHolder.get(R.id.circle_pic);
        Glide.with(context).load(resultBean.getHeadPic())
                .centerCrop()
                //默认淡入淡出动画
                .transition(withCrossFade())
                //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                .skipMemoryCache(false)
                //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(simpleDraweeView);
        final String image = resultBean.getImage();
        if (!TextUtils.isEmpty(image)) {

            String[] split = image.split(",");
            final List<CircleListBean> circleListBeanList = new ArrayList<>();
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
                Glide.with(context).load(split[i])
                        //默认淡入淡出动画
                        .transition(withCrossFade())
                        //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                        .skipMemoryCache(false)
                        //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewIamge);

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
                CircleListBean circleListBean = new CircleListBean();
                //  Logger.i("CircleListBean", split[i]);
                circleListBean.setImage(split[i]);
                circleListBeanList.add(circleListBean);

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

        }

        zanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("setOnClickListener", position + "");
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

    @Override
    public void bindViewData(BaseViewHolder baseViewHolder,
                             final CircleBean.ResultBean resultBean) {


    }

    private OnCircleListener mOnCircleListener;

    public void setOnCircleListener(OnCircleListener mOnCircleListener) {
        this.mOnCircleListener = mOnCircleListener;
    }

    public interface OnCircleListener {
        void circle(int id);

        void cancle(int id);
    }
}

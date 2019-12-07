package com.ming.weidushop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;

import com.abner.ming.base.model.LoginBean;
import com.abner.ming.base.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;

import java.io.Serializable;
import java.util.Map;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * author:AbnerMing
 * date:2019/9/3
 * 主module工具类
 */
public class AppUtils {

    public static int mOrderPosoition= 0;
    public static int mCommodityByOrder = 0;
    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
    }

    //跳转Activity
    public static void start(Context context, Class cls) {
        context.startActivity(new Intent(context, cls));
    }

    //跳转Activity 携带参数 String
    public static void startString(Context context, Class cls, Map<String, String> map) {
        Intent intent = new Intent(context, cls);
        for (String key : map.keySet()) {
            intent.putExtra(key, map.get(key));
        }
        context.startActivity(intent);
    }

    //跳转Activity 携带参数 int
    public static void startInt(Context context, Class cls, Map<String, Integer> map) {
        Intent intent = new Intent(context, cls);
        for (String key : map.keySet()) {
            intent.putExtra(key, map.get(key));
        }
        context.startActivity(intent);
    }

    //跳转Activity 携带参数 对象
    public static void startObject(Context context, Class cls, Object o) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("object", (Serializable) o);
        context.startActivity(intent);
    }

    //带返回值进行跳转
    public static void startResult(Context context, Class cls) {
        ((Activity) context).startActivityForResult(new Intent(context, cls), 1000);
    }

    //跳转Activity 携带参数 对象
    public static void startResultObject(Context context, Class cls, Object o) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("object", (Serializable) o);
        ((Activity) context).startActivityForResult(intent, 1000);
    }

    //获取用户信息
    public static LoginBean.ResultBean getUserInFo() {
        return Utils.getUserInFo();
    }


    //设置圆角
    public static GenericDraweeHierarchy cricleDrawee(Context context) {
        //圆角图片
        RoundingParams rp = new RoundingParams();
        //设置边框颜色 宽度
        rp.setBorder(Color.parseColor("#e8e8e8"), 1);
        //设置圆角
        rp.setRoundAsCircle(true);
        GenericDraweeHierarchy build = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setRoundingParams(RoundingParams.asCircle()) //直接设置圆角
                .setRoundingParams(rp)
                .build();
        return build;
    }

    public static void setGlide(Context context, String headPic, ImageView imageView) {
        Glide.with(context).load(headPic)
                //默认淡入淡出动画
                .transition(withCrossFade())
                //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                .skipMemoryCache(false)
                //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void setGlideCircle(Context context, String headPic, ImageView imageView) {
        Glide.with(context).load(headPic)
                //默认淡入淡出动画
                .transition(withCrossFade())
                //缓存策略,跳过内存缓存【此处应该设置为false，否则列表刷新时会闪一下】
                .skipMemoryCache(false)
                //缓存策略,硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }


}

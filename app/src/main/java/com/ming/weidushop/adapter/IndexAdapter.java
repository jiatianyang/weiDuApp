package com.ming.weidushop.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abner.ming.base.refresh.material.Util;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ming.weidushop.R;
import com.ming.weidushop.activity.CommodityDetailsActivity;
import com.ming.weidushop.activity.SearchActivity;
import com.ming.weidushop.activity.WebViewActivity;
import com.ming.weidushop.bean.BannerBean;
import com.ming.weidushop.bean.BaseBean;
import com.ming.weidushop.bean.IndexListBean;
import com.ming.weidushop.utils.AppUtils;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:AbnerMing
 * date:2019/9/2
 * 首页多条目
 */
public class IndexAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseBean> mlist = new ArrayList<>();


    public IndexAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (i) {//i是返回的类型
            case 0://banner
                View view_0 = View.inflate(mContext, R.layout.adapter_item_0, null);
                viewHolder = new ViewHolder_0(view_0);
                break;
            case 1://热销新品
            case 2://魔力时尚
            case 3://品质生活
                View viewShop = View.inflate(mContext, R.layout.adapter_item, null);
                viewHolder = new ShopViewHolder(viewShop);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        BaseBean baseBean = mlist.get(i);
        //第一种类型
        if (baseBean instanceof BannerBean) {
            final ViewHolder_0 mViewHolder_0 = (ViewHolder_0) viewHolder;
            final BannerBean bannerBean = (BannerBean) baseBean;
            mViewHolder_0.mXBanner.setBannerData(R.layout.image_fresco, bannerBean.getResult());
            mViewHolder_0.mXBanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    BannerBean.ResultBean b = (BannerBean.ResultBean) model;
                    RoundingParams roundingParams = new RoundingParams();
                    roundingParams.setCornersRadius(Util.dip2px(mContext, 10));
                    GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
                    GenericDraweeHierarchy hierarchy = builder.build();
                    hierarchy.setRoundingParams(roundingParams);
                    ((SimpleDraweeView) view).setHierarchy(hierarchy);
                    ((SimpleDraweeView) view).setImageURI(b.getImageUrl());

                }
            });
            setBannerColor(0, mViewHolder_0, bannerBean);
            mViewHolder_0.mXBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int position) {
                    setBannerColor(position, mViewHolder_0, bannerBean);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
            mViewHolder_0.mXBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                @Override
                public void onItemClick(XBanner banner, Object model, View view, int position) {
                    try {
                        BannerBean.ResultBean b = (BannerBean.ResultBean) model;
                        if (b.getJumpUrl().contains("wd://")) {
                            String s = b.getJumpUrl().split("=")[1];

                            if (b.getJumpUrl().contains("commodity_list")) {//商品列表
                                Map<String, String> map = new HashMap<>();
                                map.put("shop_id", s);
                                AppUtils.startString(mContext, SearchActivity.class, map);
                            } else {
                                Map<String, Integer> map = new HashMap<>();
                                map.put("id", Integer.parseInt(s));
                                AppUtils.startInt(mContext, CommodityDetailsActivity.class, map);
                            }

                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put("web_url", b.getJumpUrl());
                            map.put("web_title", b.getTitle());
                            AppUtils.startString(mContext, WebViewActivity.class, map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (baseBean instanceof IndexListBean.ResultBean.RxxpBean) {
            //第二种；类型
            IndexListBean.ResultBean.RxxpBean rxxpBean = (IndexListBean.ResultBean.RxxpBean) baseBean;

            ShopViewHolder viewHolder_1 = (ShopViewHolder) viewHolder;
            viewHolder_1.mTitle.setText("热销新品");
            viewHolder_1.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorFF7F57));
            viewHolder_1.mLyout.setBackgroundResource(R.drawable.rxxp_bg);
            // viewHolder_1.mIamge.setImageResource(R.drawable.rxxp_more);
            RxxpAdapter rxxpAdapter = new RxxpAdapter(mContext, rxxpBean.getCommodityList());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder_1.mRecyclerView.setLayoutManager(linearLayoutManager);
            viewHolder_1.mRecyclerView.setAdapter(rxxpAdapter);

        } else if (baseBean instanceof IndexListBean.ResultBean.MlssBean) {
            //第三种；类型
            IndexListBean.ResultBean.MlssBean mlssBean = (IndexListBean.ResultBean.MlssBean) baseBean;
            ShopViewHolder viewHolder_2 = (ShopViewHolder) viewHolder;
            viewHolder_2.mTitle.setText("魔力时尚");
            viewHolder_2.mTitle.setTextColor(mContext.getResources().getColor(R.color.color787AF6));
            viewHolder_2.mLyout.setBackgroundResource(R.drawable.mlss_bg);
            // viewHolder_2.mIamge.setImageResource(R.drawable.mlss_more);
            MlssAdapter mlssAdapter = new MlssAdapter(mContext, mlssBean.getCommodityList());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            viewHolder_2.mRecyclerView.setLayoutManager(linearLayoutManager);
            viewHolder_2.mRecyclerView.setAdapter(mlssAdapter);


        } else if (baseBean instanceof IndexListBean.ResultBean.PzshBean) {
            //第四种；类型
            IndexListBean.ResultBean.PzshBean pzshBean = (IndexListBean.ResultBean.PzshBean) baseBean;
            ShopViewHolder viewHolder_3 = (ShopViewHolder) viewHolder;
            viewHolder_3.mTitle.setText("品质生活");
            viewHolder_3.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorFEA820));
            viewHolder_3.mLyout.setBackgroundResource(R.drawable.pzsh_bg);
            // viewHolder_3.mIamge.setImageResource(R.drawable.pzsh_more);
            PzshAdapter pzshAdapter = new PzshAdapter(mContext, pzshBean.getCommodityList());

            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);

            viewHolder_3.mRecyclerView.setLayoutManager(gridLayoutManager);
            viewHolder_3.mRecyclerView.setAdapter(pzshAdapter);


        }
    }

    private void setBannerColor(int position, ViewHolder_0 mViewHolder_0, BannerBean bannerBean) {
        showUrlBlur(mViewHolder_0.mBannerLayout,
                bannerBean.getResult().get(position).getImageUrl(), 20, 20);
    }

    @Override
    public int getItemViewType(int position) {
        BaseBean baseBean = mlist.get(position);
        if (baseBean instanceof BannerBean) {//banner
            return 0;
        } else if (baseBean instanceof IndexListBean.ResultBean.RxxpBean) {
            return 1;
        } else if (baseBean instanceof IndexListBean.ResultBean.MlssBean) {
            return 2;
        } else {
            return 3;//品质生活
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    //传递数据
    public void setList(List<BaseBean> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }


    //Banner
    private class ViewHolder_0 extends RecyclerView.ViewHolder {
        XBanner mXBanner;
        SimpleDraweeView mBannerLayout;

        public ViewHolder_0(@NonNull View itemView) {
            super(itemView);
            mXBanner = itemView.findViewById(R.id.xbanner);
            mBannerLayout = (SimpleDraweeView) itemView.findViewById(R.id.banner_layout);

        }
    }

    //热销新品  魔力时尚   品质生活
    private class ShopViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        RecyclerView mRecyclerView;
        RelativeLayout mLyout;
        ImageView mIamge;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mLyout = (RelativeLayout) itemView.findViewById(R.id.layout);
            mIamge = (ImageView) itemView.findViewById(R.id.iv_more);
        }
    }


    //高斯模糊
    public static void showUrlBlur(SimpleDraweeView draweeView, String url,
                                   int iterations, int blurRadius) {
        try {
            Uri uri = Uri.parse(url);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setPostprocessor(new IterativeBoxBlurPostProcessor(iterations, blurRadius))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.ming.weidushop.activity;

import android.view.View;

import com.abner.ming.base.BaseAppCompatActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ming.weidushop.R;
import com.ming.weidushop.bean.CircleListBean;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/13
 * 查看图片
 */
public class ImageViewActivity extends BaseAppCompatActivity {
    private String mImage, mImagePosition;
    private XBanner mXbanner;
    private List<CircleListBean> mListBean = new ArrayList<>();

    @Override
    protected void initData() {

        mImage = getIntent().getStringExtra("image");
        mImagePosition = getIntent().getStringExtra("imagePosition");
        String[] split = mImage.split(",");
        for (int i = 0; i < split.length; i++) {
            CircleListBean bean = new CircleListBean();
            bean.setImage(split[i]);
            mListBean.add(bean);
        }

        mXbanner.setBannerData(R.layout.image_fresco, mListBean);
        mXbanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                CircleListBean b = (CircleListBean) model;
                ((SimpleDraweeView) view).setImageURI(b.getImage());

            }
        });
        mXbanner.getViewPager().setCurrentItem(Integer.parseInt(mImagePosition));
    }

    @Override
    protected void initView() {
        setShowTitle(false);
        setTitle("查看图片");
        isShowBack(true);
        setWindowTitleBlack(true);
        mXbanner = (XBanner) get(R.id.xbanner);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }
}

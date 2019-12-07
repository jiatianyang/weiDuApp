package com.ming.weidushop.bean;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

/**
 * author:AbnerMing
 * date:2019/4/19
 */
public class ShopDescPictrueBean extends SimpleBannerInfo{
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Object getXBannerUrl() {
        return null;
    }
}

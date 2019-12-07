package com.ming.weidushop.bean;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

/**
 * author:AbnerMing
 * date:2019/9/10
 */
public class CircleListBean extends SimpleBannerInfo {
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public Object getXBannerUrl() {
        return null;
    }
}

package com.ming.weidushop.bean;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

/**
 * author:AbnerMing
 * date:2019/9/5
 */
public class DetailsBannerBean extends SimpleBannerInfo {
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public Object getXBannerUrl() {
        return null;
    }
}

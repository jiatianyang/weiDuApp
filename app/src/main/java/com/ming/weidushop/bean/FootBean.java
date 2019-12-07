package com.ming.weidushop.bean;

import com.abner.ming.base.model.AppBean;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/4
 */
public class FootBean extends AppBean {


    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * browseNum : 1
         * browseTime : 1542818651000
         * commodityId : 99
         * commodityName : 字母绣花宽松女款卫衣
         * masterPic : http://172.17.8.100/images/small/commodity/nz/wy/7/1.jpg
         * price : 179
         * userId : 12
         */

        private int browseNum;
        private long browseTime;
        private int commodityId;
        private String commodityName;
        private String masterPic;
        private int price;
        private int userId;

        public int getBrowseNum() {
            return browseNum;
        }

        public void setBrowseNum(int browseNum) {
            this.browseNum = browseNum;
        }

        public long getBrowseTime() {
            return browseTime;
        }

        public void setBrowseTime(long browseTime) {
            this.browseTime = browseTime;
        }

        public int getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(int commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public String getMasterPic() {
            return masterPic;
        }

        public void setMasterPic(String masterPic) {
            this.masterPic = masterPic;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}

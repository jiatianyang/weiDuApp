package com.ming.weidushop.bean;

import com.abner.ming.base.model.AppBean;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/12
 */
public class CommodityCommentBean extends AppBean {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * commodityId : 6
         * content : 好用不贵，经济实惠
         * createTime : 1542641122000
         * headPic : http://172.17.8.100/images/small/head_pic/2018-11-17/20181117120315.jpg
         * image : http://172.17.8.100/images/small/comment_pic/2018-11-19/4326920181119092522.jpg,http://172.17.8.100/images/small/comment_pic/2018-11-19/1285020181119092522.jpg
         * nickName : 风情的人
         * userId : 1
         */

        private int commodityId;
        private String content;
        private long createTime;
        private String headPic;
        private String image;
        private String nickName;
        private int userId;

        public int getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(int commodityId) {
            this.commodityId = commodityId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}

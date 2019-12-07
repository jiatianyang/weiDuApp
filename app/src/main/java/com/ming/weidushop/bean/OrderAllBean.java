package com.ming.weidushop.bean;

import com.abner.ming.base.model.AppBean;

import java.io.Serializable;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/9
 */
public class OrderAllBean extends AppBean {

    private List<OrderListBean> orderList;

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * detailList : [{"commentStatus":1,"commodityCount":1,"commodityId":27,"commodityName":"休闲马衔扣保暖绒里棉鞋懒人鞋毛毛鞋平底女雪地靴女短靴子豆豆鞋女鞋","commodityPic":"http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/1.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/2.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/3.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/4.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/5.jpg","commodityPrice":139,"orderDetailId":11098}]
         * expressCompName : 京东快递
         * expressSn : 1001
         * orderId : 201909091532161421862
         * orderStatus : 1
         * payAmount : 139
         * payMethod : 1
         * userId : 1862
         */

        private String expressCompName;
        private String expressSn;
        private String orderId;
        private int orderStatus;
        private int payAmount;
        private int payMethod;
        private int userId;
        private long orderTime;

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }

        private List<DetailListBean> detailList;

        public String getExpressCompName() {
            return expressCompName;
        }

        public void setExpressCompName(String expressCompName) {
            this.expressCompName = expressCompName;
        }

        public String getExpressSn() {
            return expressSn;
        }

        public void setExpressSn(String expressSn) {
            this.expressSn = expressSn;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(int payAmount) {
            this.payAmount = payAmount;
        }

        public int getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(int payMethod) {
            this.payMethod = payMethod;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<DetailListBean> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<DetailListBean> detailList) {
            this.detailList = detailList;
        }

        public static class DetailListBean implements Serializable {
            /**
             * commentStatus : 1
             * commodityCount : 1
             * commodityId : 27
             * commodityName : 休闲马衔扣保暖绒里棉鞋懒人鞋毛毛鞋平底女雪地靴女短靴子豆豆鞋女鞋
             * commodityPic : http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/1.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/2.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/3.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/4.jpg,http://mobile.bwstudent.com/images/small/commodity/nx/ddx/3/5.jpg
             * commodityPrice : 139
             * orderDetailId : 11098
             */

            private int commentStatus;
            private int commodityCount;
            private int commodityId;
            private String commodityName;
            private String commodityPic;
            private int commodityPrice;
            private int orderDetailId;
            private long orderTime;
            private String orderId;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public long getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(long orderTime) {
                this.orderTime = orderTime;
            }

            public int getCommentStatus() {
                return commentStatus;
            }

            public void setCommentStatus(int commentStatus) {
                this.commentStatus = commentStatus;
            }

            public int getCommodityCount() {
                return commodityCount;
            }

            public void setCommodityCount(int commodityCount) {
                this.commodityCount = commodityCount;
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

            public String getCommodityPic() {
                return commodityPic;
            }

            public void setCommodityPic(String commodityPic) {
                this.commodityPic = commodityPic;
            }

            public int getCommodityPrice() {
                return commodityPrice;
            }

            public void setCommodityPrice(int commodityPrice) {
                this.commodityPrice = commodityPrice;
            }

            public int getOrderDetailId() {
                return orderDetailId;
            }

            public void setOrderDetailId(int orderDetailId) {
                this.orderDetailId = orderDetailId;
            }
        }
    }
}

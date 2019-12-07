package com.ming.weidushop.bean;

import com.abner.ming.base.model.AppBean;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/10
 */
public class PayBean extends AppBean {

    /**
     * result : {"balance":9.9999643E7,"detailList":[{"amount":278,"consumerTime":1568079671000,"orderId":"201909100940430391862","userId":1862},{"amount":78,"consumerTime":1568079429000,"orderId":"201909100903202791862","userId":1862}]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * balance : 9.9999643E7
         * detailList : [{"amount":278,"consumerTime":1568079671000,"orderId":"201909100940430391862","userId":1862},{"amount":78,"consumerTime":1568079429000,"orderId":"201909100903202791862","userId":1862}]
         */

        private double balance;
        private List<DetailListBean> detailList;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public List<DetailListBean> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<DetailListBean> detailList) {
            this.detailList = detailList;
        }

        public static class DetailListBean {
            /**
             * amount : 278
             * consumerTime : 1568079671000
             * orderId : 201909100940430391862
             * userId : 1862
             */

            private int amount;
            private long consumerTime;
            private String orderId;
            private int userId;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public long getConsumerTime() {
                return consumerTime;
            }

            public void setConsumerTime(long consumerTime) {
                this.consumerTime = consumerTime;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}

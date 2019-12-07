package com.ming.weidushop.bean;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/13
 */
public class ShopListRightBean {

    /**
     * result : [{"id":"1001002001","name":"外套"},{"id":"1001002002","name":"裙装"},{"id":"1001002003","name":"打底毛衣"},{"id":"1001002004","name":"卫衣"},{"id":"1001002005","name":"裤装"}]
     * message : 查询成功
     * status : 0000
     */

    private String message;
    private String status;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1001002001
         * name : 外套
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

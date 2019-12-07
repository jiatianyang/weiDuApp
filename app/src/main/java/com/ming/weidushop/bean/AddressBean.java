package com.ming.weidushop.bean;

import com.abner.ming.base.model.AppBean;

import java.io.Serializable;
import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/9
 */
public class AddressBean extends AppBean {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * address : 北京市  北京市  东城区 你好
         * createTime : 1568048907000
         * id : 1479
         * phone : 18500817723
         * realName : 李小男
         * userId : 1862
         * whetherDefault : 1
         * zipCode : 123456
         */

        private String address;
        private long createTime;
        private int id;
        private String phone;
        private String realName;
        private int userId;
        private int whetherDefault;
        private String zipCode;
        private boolean defaultSelected;

        public boolean isDefaultSelected() {
            return defaultSelected;
        }

        public void setDefaultSelected(boolean defaultSelected) {
            this.defaultSelected = defaultSelected;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getWhetherDefault() {
            return whetherDefault;
        }

        public void setWhetherDefault(int whetherDefault) {
            this.whetherDefault = whetherDefault;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }
}

package com.ming.weidushop.bean;

import com.abner.ming.base.model.AppBean;

import java.util.List;

/**
 * author:AbnerMing
 * date:2019/9/16
 */
public class FindCategoryBean extends AppBean {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1001002
         * name : 女装
         * secondCategoryVo : [{"id":"1001002001","name":"外套"},{"id":"1001002002","name":"裙装"},{"id":"1001002003","name":"打底毛衣"},{"id":"1001002004","name":"卫衣"},{"id":"1001002005","name":"裤装"}]
         */

        private String id;
        private String name;
        private boolean click;

        public boolean isClick() {
            return click;
        }

        public void setClick(boolean click) {
            this.click = click;
        }

        private List<SecondCategoryVoBean> secondCategoryVo;

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

        public List<SecondCategoryVoBean> getSecondCategoryVo() {
            return secondCategoryVo;
        }

        public void setSecondCategoryVo(List<SecondCategoryVoBean> secondCategoryVo) {
            this.secondCategoryVo = secondCategoryVo;
        }

        public static class SecondCategoryVoBean {
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
}

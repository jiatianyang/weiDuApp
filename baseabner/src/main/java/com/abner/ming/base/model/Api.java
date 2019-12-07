package com.abner.ming.base.model;

/**
 * author:AbnerMing
 * date:2019/9/1
 * 接口类
 */
public class Api {
    public final static String BASE_URL = "http://mobile.bwstudent.com/";//"http://172.17.8.100/";

    //首页banner图
    public final static String INDEX_BANNER = "small/commodity/v1/bannerShow";

    //首页多条目
    public final static String INDEX_LIST_URL = "small/commodity/v1/commodityList";

    //注册
    public final static String REGISTER_URL = "small/user/v1/register";

    //登录
    public final static String LOGIN_URL = "small/user/v1/login";

    //我的足迹
    public final static String FOOT_URL = "small/commodity/verify/v1/browseList";

    //获取圈子
    public final static String CIRCLE_URL = "small/circle/v1/findCircleList";

    //获取我的圈子
    public final static String MY_CIRCLE_URL = "small/circle/verify/v1/findMyCircleById";

    //删除圈子
    public final static String DELETE_CIRCLE_URL = "small/circle/verify/v1/deleteCircle";


    //商品详情
    public final static String COMMODITYDETAILS_URL = "small/commodity/v1/findCommodityDetailsById";

    //搜索商品
    public final static String SEARCH_URL = "small/commodity/v1/findCommodityByKeyword";

    //添加 同步购物车
    public final static String ADD_CAR_URL = "small/order/verify/v1/syncShoppingCart";

    //查询购物车
    public final static String QUERY_CAR_URL = "small/order/verify/v1/findShoppingCart";

    //查询订单
    public final static String QUERY_ORDER_URL = "small/order/verify/v1/findOrderListByStatus";

    //创建订单
    public final static String CREATE_ORDER_URL = "small/order/verify/v1/createOrder";

    //创建订单
    public final static String DELETE_ORDER_URL = "small/order/verify/v1/deleteOrder";


    //查询收获地址
    public final static String QUERY_ADDRESS_URL = "small/user/verify/v1/receiveAddressList";

    //新增收货地址
    public final static String ADD_ADDRESS_URL = "small/user/verify/v1/addReceiveAddress";

    //设置默认收货地址
    public final static String SETTING_ADDRESS_URL = "small/user/verify/v1/setDefaultReceiveAddress";

    //修改收货地址
    public final static String CHANGE_ADDRESS_URL = "small/user/verify/v1/changeReceiveAddress";

    //删除收货地址
    public final static String DELETE_ADDRESS_URL = "small/user/verify/v1/deleteReceiveAddress";

    //确认收货
    public final static String SUBMIT_ORDER_URL = "small/order/verify/v1/confirmReceipt";


    //支付
    public final static String PAY_URL = "small/order/verify/v1/pay";

    //查询钱包
    public final static String QUERY_PAY_URL = "small/user/verify/v1/findUserWallet";

    //点赞
    public final static String ZAN_URL = "small/circle/verify/v1/addCircleGreat";

    //取消点赞
    public final static String DELETE_ZAN_URL = "small/circle/verify/v1/cancelCircleGreat";

    //商品评论
    public final static String COMMENT_URL = "small/commodity/verify/v1/addCommodityComment";

    //评论圈子
    public final static String CIRCLE_COMMENT_URL = "small/circle/verify/v1/releaseCircle";

    //修改昵称
    public final static String CHANGE_NICK_URL = "small/user/verify/v1/modifyUserNick";

    //商品评论
    public final static String COMMODITY_COMMENT_URL = "small/commodity/v1/CommodityCommentList";

    //修改头像
    public final static String CHANGE_PICTRUE = "small/user/verify/v1/modifyHeadPic";

    //二级列表 合并
    public final static String SHOP_CATEGORY_URL = "small/commodity/v1/findCategory";

    //二级列表 一级
    public final static String SHOP_CATEGORY_TOP = "small/commodity/v1/findFirstCategory";

    //二级列表 二级
    public final static String SHOP_CATEGORY_BOTTOM = "small/commodity/v1/findCommodityByCategory";

    //更新apk
    public final static String UPLOAD_URL = "small/user/v1/findAppVersion";


}

## 维度商城说明文档
### 主要说明
```
维度商城项目使用Java语言开发，采用MVP架构，网络请求使用Rxjava+Retrofit,
数据缓存使用GreenDao，列表展示使用RecyclerView，下拉刷新和上拉加载使用
XRecyclerView,短信验证集成Mob，轮播图使用XBanner。

除了这些基本的功能之外，也集成了一个众多技术和功能与一体的baselibrary,
有了它，可以一行代码搞定网络请求，有了它，常见工具类无需重复封装，有了它
让开发效率实现倍增……

```
### 相关类说明
```
BaseAppCompatActivity:所有Activity的基类
BaseFragment：所有Fragment的基类
BaseRecyclerAdapter：所有RecyclerView适配器的基类
UniversalAdapter：所有采用ListView时适配器的基类
CacheUtils：数据缓存工具类
Logger：打印log工具类
SharedPreUtils：sp数据操作类
NetworkUtils：网络状态判断工具类
ToastUtils：toast仿多次提示工具类

MainActivity：主Activity
HomeFragment:首页Fragment
CircleFragment：圈子
ShopCarFragment：购物车
OrderFragment：订单
AccountFragment：我的

BottomTabView：自定义主页底部tab
LayoutDataNull：无数据后展示
ShopAddView：自定义加减器
BottomDialog：底部dialog提示
MediaUtils：拍照和相册选取

```

### 网络请求
##### 网络请求分为继承base和非继承base
##### 继承base
```
    直接调用 net方法，两个参数一个是否显示加载框，一个是否读物缓存
    调用之后，可以直接调取请求方式，分为四种，get post put delete,
    每种方式，都有三个参数，一个是请求类型，一个是请求接口，一个是参数传递
    
    //获取返回字符串
    net(false, false).get(0, Api.CHANGE_ADDRESS_URL, map);
    
    //获取返回javabean
    net(false, false, AppBean.class).put(0, Api.CHANGE_ADDRESS_URL, map);
    
    接收成功和失败方法，直接重写successBean或者 success,失败重写fail
    
    
```
##### 下载体验
[维度商城下载](http://mobile.bwstudent.com/media/product/mall/mall_standard.apk)

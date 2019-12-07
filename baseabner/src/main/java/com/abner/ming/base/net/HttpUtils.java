package com.abner.ming.base.net;


import android.content.Context;

import com.abner.ming.base.dialog.DialogLoading;
import com.abner.ming.base.model.Api;
import com.abner.ming.base.utils.CacheUtils;
import com.abner.ming.base.utils.NetworkUtils;
import com.abner.ming.base.utils.ToastUtils;
import com.abner.ming.base.utils.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * author:AbnerMing
 * date:2019/4/18
 * 联网工具类
 */
public class HttpUtils {

    private String mBaseUrl = Api.BASE_URL;
    private String mUrl;
    //传递头参
    private Map<String, String> mHeadMap = new HashMap<>();

    //是否显示加载框
    private boolean mIsShowLoading;

    //更改baseurl
    public HttpUtils setBaseUrl(String base_url) {
        this.mBaseUrl = base_url;
        return this;
    }


    public HttpUtils setHead(Map<String, String> headMap) {
        this.mHeadMap = headMap;
        return this;
    }


    public HttpUtils isShowLoading(boolean isShowLoading) {
        this.mIsShowLoading = isShowLoading;
        return this;
    }

    //是否读取缓存
    private boolean isReadCache;

    public HttpUtils isReadCache(boolean isReadCache) {
        this.isReadCache = isReadCache;
        return this;
    }


    //传递上下文
    private Context mContext;

    public HttpUtils setContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }


    //get请求
    public HttpUtils get(String url, Map<String, String> map) {
        this.mUrl = url;
        if (map == null) {
            map = new HashMap<>();
        }
        HttpService service = getHttpService();
        Observable<ResponseBody> ob = service.get(url, mHeadMap, map);
        send(ob);
        return this;

    }


    //post请求
    public HttpUtils post(String url, Map<String, String> map) {
        this.mUrl = url;
        if (map == null) {
            map = new HashMap<>();
        }
        HttpService service = getHttpService();
        Observable<ResponseBody> ob = service.post(url, mHeadMap, map);
        send(ob);
        return this;
    }

    //put请求
    public HttpUtils put(String url, Map<String, String> map) {
        this.mUrl = url;
        if (map == null) {
            map = new HashMap<>();
        }
        HttpService service = getHttpService();
        Observable<ResponseBody> ob = service.put(url, mHeadMap, map);
        send(ob);
        return this;
    }

    //delete请求
    public HttpUtils delete(String url, Map<String, String> map) {
        this.mUrl = url;
        if (map == null) {
            map = new HashMap<>();
        }
        HttpService service = getHttpService();
        Observable<ResponseBody> ob = service.delete(url, mHeadMap, map);
        send(ob);
        return this;
    }


    //产生订阅
    private DialogLoading.Builder mLoading;

    private void send(Observable<ResponseBody> ob) {
        try {
            if (!NetworkUtils.isConnected(mContext)) {
                ToastUtils.show("网络开小差了，请检查网络！");
            }
            if (mIsShowLoading) {
                mLoading = new DialogLoading.Builder(mContext).alertBg();
                mLoading.show();
            }
            if (isReadCache && !NetworkUtils.isConnected(mContext)) {
                String json = CacheUtils.getCacheUtils().query(Utils.md5(mUrl));
                successHttp(json);
                return;
            }

            ob.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ObserverIml<ResponseBody>() {
                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                successHttp(responseBody.string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            try {
                                if (mIsShowLoading) {
                                    mLoading.hint();
                                }
                                if (cls == null) {
                                    mHttpListener.fail(e.getMessage());
                                } else {
                                    mHttpBeanListener.fail(e.getMessage());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void successHttp(String json) {
        if (isReadCache && NetworkUtils.isConnected(mContext)) {
            CacheUtils.getCacheUtils().insert(Utils.md5(mUrl), json);//缓存
        }
        if (mIsShowLoading) {
            mLoading.hint();
        }
        if (cls == null) {//返回字符串
            mHttpListener.success(json);
        } else {
            //返回JavaBean
            Object bean = new Gson().fromJson(json, cls);
            mHttpBeanListener.success(bean);
        }
    }


    //传递javabean接口
    private HttpBeanListener mHttpBeanListener;
    private Class cls;

    public void resultBean(Class cls, HttpBeanListener mHttpBeanListener) {
        this.cls = cls;
        this.mHttpBeanListener = mHttpBeanListener;
    }


    //返回JavaBean
    public interface HttpBeanListener<T> {
        void success(T t);

        void fail(String error);
    }


    //传递接口
    private HttpListener mHttpListener;

    public void result(HttpListener mHttpListener) {
        this.mHttpListener = mHttpListener;
    }

    //返回字符串
    public interface HttpListener {
        void success(String data);

        void fail(String error);
    }

    //获取请求接口
    private HttpService getHttpService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(mBaseUrl).build();
        return retrofit.create(HttpService.class);
    }


    //单图上传  url 请求接口  map请求参数   path 文件路径
    public HttpUtils upload(String url, Map<String, String> map, String path) {
        MediaType mediaType = MediaType.parse("multipart/form-data; charset=utf-8");
        File file = new File(path);
        RequestBody body = RequestBody.create(mediaType, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), body);
        HttpService service = getHttpService();
        Observable<ResponseBody> call = service.uploadPic(url, map, part);
        send(call);
        return this;

    }

    //多图
    public HttpUtils uploadMorePic(String url, Map<String, String> map, List<String> paths) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            String s = paths.get(i);
            File file = new File(s);
            MediaType mediaType = MediaType.parse("multipart/form-data; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), body);
            parts.add(part);
        }
        HttpService service = getHttpService();
        Observable<ResponseBody> call = service.uploadMorePic(url, mHeadMap, map, parts);
        send(call);
        return this;
    }

    //传json
    public HttpUtils getJson(String url, Map<String, String> map) {
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        HttpService service = getHttpService();
        Observable<ResponseBody> call = service.getJson(url, mHeadMap, requestBody);
        send(call);
        return this;
    }

}

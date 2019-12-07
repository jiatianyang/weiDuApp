package com.abner.ming.base.net;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * author:AbnerMing
 * date:2019/4/18
 */
public interface HttpService {

    @GET
    Observable<ResponseBody> get(@Url String url,
                                 @HeaderMap Map<String, String> headMap,
                                 @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url,
                                  @HeaderMap Map<String, String> headMap,
                                  @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @PUT
    Observable<ResponseBody> put(@Url String url,
                                 @HeaderMap Map<String, String> headMap,
                                 @FieldMap Map<String, String> map);


    @DELETE
    Observable<ResponseBody> delete(@Url String url,
                                    @HeaderMap Map<String, String> headMap,
                                    @QueryMap Map<String, String> map);

    //单图上传
    @Multipart
    @POST
    Observable<ResponseBody> uploadPic(@Url String url,
                                       @HeaderMap Map<String, String> map,
                                       @Part MultipartBody.Part part);

    //多图上传
    @Multipart
    @POST
    Observable<ResponseBody> uploadMorePic(@Url String url,
                                           @HeaderMap Map<String, String> map,
                                           @QueryMap Map<String, String> mapQuery,
                                           @Part List<MultipartBody.Part> parts);

    //传递json
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET
    Observable<ResponseBody> getJson(@Url String url,
                                     @HeaderMap Map<String, String> map,
                                     @Body RequestBody body);
}

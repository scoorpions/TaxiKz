package kz.taxikz.data.api.service;

import kz.taxikz.data.api.pojo.BaseApiData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GoogleService {
    @FormUrlEncoded
    @POST("/api/gcm")
    Call<BaseApiData<String>> orderGCM(@Field("key") String str, @Field("order_id") String str2);

    @FormUrlEncoded
    @POST("/api/gcmreg")
    Call<BaseApiData<String>> registerGCM(@Field("key") String str);
}

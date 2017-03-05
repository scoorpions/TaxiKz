package kz.taxikz.data.api.service;

import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.ClientInfoData;
import kz.taxikz.data.api.pojo.UpdateClientInfo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientApiService {
    @GET("/api/clients/get_client_info")
    Call<BaseApiData<ClientInfoData>> getClientInfo(@Query("client_id") String str);

    @FormUrlEncoded
    @POST("/api/clients/update_client_info")
    Call<BaseApiData<UpdateClientInfo>> updateClientInfo(@Field("phone") String str, @Field("client_id") String str2, @Field("name") String str3, @Field("password") String str4);
}

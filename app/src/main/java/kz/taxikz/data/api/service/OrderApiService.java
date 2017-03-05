package kz.taxikz.data.api.service;

import java.util.List;

import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CreateOrderDataItem;
import kz.taxikz.data.api.pojo.Order;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderApiService {
    @GET("/api/orders/cancel")
    Call<BaseApiData<String>> cancelOrder(@Query("order_id") String str);

    @GET("/api/orders")
    Call<BaseApiData<List<Order>>> getCurrentOrders(@Query("client_id") String str);

    @FormUrlEncoded
    @POST("/api/orders")
    Call<BaseApiData<List<CreateOrderDataItem>>> newOrder(@Field("phone") String str, @Field("autodial_phone") String str2, @Field("client_id") String str3, @Field("customer") String str4, @Field("addresses[]") List<String> list, @Field("city_id") String str5, @Field("porch") String str6, @Field("comment") String str7, @Field("order_params[]") List<Integer> list2, @Field("total_cost") int i, @Field("bonus_sum") int i2);
}

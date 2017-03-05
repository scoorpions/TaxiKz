package kz.taxikz.data.api.service;

import java.util.List;

import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CostData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CostService {
    @GET("/api/routes")
    Call<BaseApiData<CostData>> cost(@Query("addresses[]") List<String> list, @Query("params[]") List<Integer> list2, @Query("city_id") String str);
}

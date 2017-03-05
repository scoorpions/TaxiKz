package kz.taxikz.data.api.service;

import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.ParamData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ParamsService {
    @GET("/api/params")
    Call<BaseApiData<ParamData>> getParam();
}

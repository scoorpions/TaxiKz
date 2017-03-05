package kz.taxikz.data.api.service;

import java.util.List;

import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.data.api.pojo.BaseApiData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AutoCompleteService {
    @GET("/api/autocomplete/streets")
    Call<BaseApiData<List<AddressData>>> getAddresses(@Query("city") String str, @Query("q") String str2);
}

package kz.taxikz.data.api.service;

import java.util.List;

import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CredentialsData;
import kz.taxikz.data.api.pojo.RegistrationData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthService {
    @FormUrlEncoded
    @PATCH("/api/registrations/{key}")
    @Headers({"Accept: application/json", "Content-Type: application/x-www-form-urlencoded"})
    Call<BaseApiData<CredentialsData>> checkRegistration(@Path("key") String str, @Field("phone") String str2);

    @FormUrlEncoded
    @POST("/api/registrations")
    Call<BaseApiData<RegistrationData>> registration(@Field("phone") String str, @Field("name") String str2);

    @GET("/api/registrations/bysms")
    Call<BaseApiData<CredentialsData>> registrationBySms(@Query("phone") String str, @Query("name") String str2, @Query("password") String str3);

    @FormUrlEncoded
    @POST("/api/sms")
    Call<BaseApiData<List<String>>> requestSms(@Field("phone") String str, @Field("key") String str2);
}

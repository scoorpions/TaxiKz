package kz.taxikz.data.api.service;

import java.util.List;

import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.News;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsService {
    @GET("/api/news")
    Call<BaseApiData<List<News>>> getNews();
}

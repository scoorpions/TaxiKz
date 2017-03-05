package kz.taxikz.job.news;

import java.util.List;

import kz.taxikz.controllers.news.NewsController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class GetNewsJob extends BaseNewsJob {
    private NewsController mNewsController;

    public GetNewsJob(NewsController mNewsController) {
        this.mNewsController = mNewsController;
    }

    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mNewsController.handleGetNewsError(getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mNewsController.handleGetNewsError(throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.newsService.getNews().enqueue(new Callback<BaseApiData<List<News>>>() {
            @Override
            public void onResponse(Call<BaseApiData<List<News>>> call, Response<BaseApiData<List<News>>> response) {
                Timber.d("onResponse()%s", response.body());
                if (response.body() != null && response.isSuccessful()) {
                    GetNewsJob.this.mNewsController.handleGetNewsSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<List<News>>> call, Throwable t) {
                Timber.e("onFailure()%s", t);
            }
        });
    }
}

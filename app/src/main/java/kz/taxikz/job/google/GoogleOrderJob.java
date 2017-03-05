package kz.taxikz.job.google;

import kz.taxikz.controllers.google.GoogleController;
import kz.taxikz.data.api.pojo.BaseApiData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class GoogleOrderJob extends BaseGoogleJob {
    private GoogleController mGoogleController;
    private String mOrderId;
    private String mToken;

    public GoogleOrderJob(GoogleController mGoogleController, String mToken, String mOrderId) {
        this.mGoogleController = mGoogleController;
        this.mToken = mToken;
        this.mOrderId = mOrderId;
    }

    @Override
    protected void onCancel() {
        Timber.e("onCancel() %s", getThrowable());
        super.onCancel();
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.mGoogleService.orderGCM(this.mToken,
                this.mOrderId).enqueue(new Callback<BaseApiData<String>>() {
            @Override
            public void onResponse(Call<BaseApiData<String>> call, Response<BaseApiData<String>> response) {
                Timber.d("onResponse() %s", response.body());
            }

            @Override
            public void onFailure(Call<BaseApiData<String>> call, Throwable t) {
                GoogleOrderJob.this.mGoogleController.handleOrderGoogleError(t);
                Timber.e("onFailure() %s", t);
            }
        });
    }
}

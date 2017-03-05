package kz.taxikz.job.params;

import java.net.SocketTimeoutException;

import kz.taxikz.controllers.version.ParamsController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.ParamData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ParamsJob extends BaseParamsJob {
    private ParamsController mParamsController;

    public ParamsJob(ParamsController mParamsController) {
        this.mParamsController = mParamsController;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mParamsController.handleVersionError(getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mParamsController.handleVersionError(throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        mParamsService.getParam().enqueue(new Callback<BaseApiData<ParamData>>() {
            @Override
            public void onResponse(Call<BaseApiData<ParamData>> call, Response<BaseApiData<ParamData>> response) {
                if (response != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", response.body());
                    mParamsController.handleVersionSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<ParamData>> call, Throwable t) {
                Timber.e("onFailure() %s", t);
                if (t instanceof SocketTimeoutException) {
                    Timber.e("t instanceof SocketTimeoutException");
                }
                mParamsController.handleVersionError(t);
            }
        });
    }
}

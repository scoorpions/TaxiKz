package kz.taxikz.job.client;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.client.ClientController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.ClientInfoData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ClientJob extends BaseClientJob {
    private ClientController mClientController;

    public ClientJob(ClientController mClientController) {
        this.mClientController = mClientController;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mClientController.handleGetClientInfoError(getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mClientController.handleGetClientInfoError(throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.clientApiService.getClientInfo(TaxiKzApp.storage.getCliendId()).enqueue(new Callback<BaseApiData<ClientInfoData>>() {
            @Override
            public void onResponse(Call<BaseApiData<ClientInfoData>> call, Response<BaseApiData<ClientInfoData>> response) {
                if (response != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", response.body());
                    ClientJob.this.mClientController.handleGetClientInfoSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<ClientInfoData>> call, Throwable t) {
                Timber.e("onFailure() %s", t);
                ClientJob.this.mClientController.handleGetClientInfoError(t);
            }
        });
    }
}

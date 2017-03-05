package kz.taxikz.job.client;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.client.ClientController;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.UpdateClientInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ClientUpdateJob extends BaseClientJob {
    private ClientController mClientController;
    private String mName;

    public ClientUpdateJob(ClientController mClientController, String mName) {
        this.mClientController = mClientController;
        this.mName = mName;
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        Timber.e("onCancel() %s", getThrowable());
        this.mClientController.handleUpdateClientInfoError(getThrowable());
    }

    @Override
    protected void onCancel(int i, Throwable throwable) {
        super.onCancel();
        Timber.d("onCancel(i, throwable) %s", throwable);
        this.mClientController.handleUpdateClientInfoError(throwable);
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun()");
        this.clientApiService.updateClientInfo(TaxiKzApp.storage.getPhone(),
                TaxiKzApp.storage.getCliendId(),
                this.mName,
                TaxiKzApp.storage.getPassword()).enqueue(new Callback<BaseApiData<UpdateClientInfo>>() {
            @Override
            public void onResponse(Call<BaseApiData<UpdateClientInfo>> call, Response<BaseApiData<UpdateClientInfo>> response) {
                if (response != null && response.isSuccessful()) {
                    Timber.d("onResponse() %s", response.body());
                    ClientUpdateJob.this.mClientController.handleUpdateClientInfoSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseApiData<UpdateClientInfo>> call, Throwable t) {
                ClientUpdateJob.this.mClientController.handleUpdateClientInfoError(t);
                Timber.e("onFailure() %s", t);
            }
        });
    }
}

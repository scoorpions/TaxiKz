package kz.taxikz.controllers.client;

import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import java.net.SocketTimeoutException;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.controllers.client.ClientEvents.GetClientFailure;
import kz.taxikz.controllers.client.ClientEvents.GetClientSuccess;
import kz.taxikz.controllers.client.ClientEvents.UpdateClientFailure;
import kz.taxikz.controllers.client.ClientEvents.UpdateClientSuccess;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.ClientInfoData;
import kz.taxikz.data.api.pojo.UpdateClientInfo;
import kz.taxikz.job.client.ClientJob;
import kz.taxikz.job.client.ClientUpdateJob;
import kz.taxikz.utils.Preconditions;
import timber.log.Timber;

public class ClientController extends BaseController {
    private final Bus mBus;
    private final JobManager mJobManager;
    private String mName;

    public ClientController(Bus mBus, JobManager mJobManager) {
        this.mBus = mBus;
        this.mJobManager = mJobManager;
    }

    public void getClientInfo() {
        Timber.d("getClientInfo()");
        this.mJobManager.addJobInBackground(new ClientJob(this));
    }

    public void handleGetClientInfoError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            getClientInfo();
        }
        this.mBus.post(new GetClientFailure(throwable));
    }

    public void handleGetClientInfoSuccess(BaseApiData<ClientInfoData> reply) {
        Preconditions.checkNotNull(reply);
        if (reply.getCode() == ApiConfig.CODE_OK) {
            ClientInfoData clientInfoData = reply.getData();
            if (clientInfoData != null) {
                Log.e("!", "handleGetClientInfoSuccess: " + clientInfoData.getCode());
                TaxiKzApp.storage.setName(clientInfoData.getClientInfo().getName());
                TaxiKzApp.storage.setBonus(clientInfoData.getClientInfo().getBonus_balance());
                this.mBus.post(new GetClientSuccess());
            }
        }
    }

    public void updateClientInfo(String mName) {
        Preconditions.checkNotNull(mName);
        this.mName = mName;
        this.mJobManager.addJobInBackground(new ClientUpdateJob(this, mName));
    }

    public void handleUpdateClientInfoError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            updateClientInfo(this.mName);
        }
        this.mBus.post(new UpdateClientFailure(throwable));
    }

    public void handleUpdateClientInfoSuccess(BaseApiData<UpdateClientInfo> reply) {
        Preconditions.checkNotNull(reply);
        if (reply.getCode() == ApiConfig.CODE_OK) {
            UpdateClientInfo updateClientInfo = reply.getData();
            this.mBus.post(new UpdateClientSuccess());
        }
    }

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

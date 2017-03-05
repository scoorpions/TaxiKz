package kz.taxikz.controllers.version;

import com.birbit.android.jobqueue.JobManager;
import com.google.gson.Gson;
import com.squareup.otto.Bus;

import java.net.SocketTimeoutException;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.controllers.version.ParamsEvents.FailedEvent;
import kz.taxikz.controllers.version.ParamsEvents.SuccessEvent;
import kz.taxikz.controllers.version.ParamsEvents.UpdateEvent;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.ParamData;
import kz.taxikz.job.params.ParamsJob;
import timber.log.Timber;

public class ParamsController extends BaseController {
    private final Bus mBus;
    private final JobManager mJobManager;

    public ParamsController(Bus bus, JobManager jobManager) {
        this.mJobManager = jobManager;
        this.mBus = bus;
    }

    public void fetchVersion() {
        Timber.d("fetchVersion()");
        this.mJobManager.addJobInBackground(new ParamsJob(this));
    }

    public void handleVersionSuccess(BaseApiData<ParamData> reply) {
        Timber.d("handleVersionSuccess() %s", reply.getData());
        if (reply.getCode() == ApiConfig.CODE_OK) {
            TaxiKzApp.storage.setParams(new Gson().toJson(reply.getData()));
            TaxiKzApp.storage.setSpendBonusLimit(reply.getData().getAndroidVar().getBonusLimit());
            TaxiKzApp.storage.setBonusActive(reply.getData().getAndroidVar().isBonusActive());
            TaxiKzApp.storage.setLastAppVersion(reply.getData().getAndroidVar().getCurrentVersion());
            TaxiKzApp.storage.setAboutAppContent(reply.getData().getAndroidVar().getTaxiKzAboutApp());
            if (reply.getData().getAndroidVar().getMinVersion() <= 51) {
                this.mBus.post(new SuccessEvent());
                return;
            } else {
                this.mBus.post(new UpdateEvent());
                return;
            }
        }
        this.mBus.post(new FailedEvent());
    }

    public void handleVersionError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            fetchVersion();
        } else {
            this.mBus.post(new FailedEvent());
        }
        Timber.e("handleVersionError %s", throwable);
    }

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

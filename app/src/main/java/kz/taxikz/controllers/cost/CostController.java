package kz.taxikz.controllers.cost;

import com.birbit.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import java.net.SocketTimeoutException;
import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.analytics.AnalyticsTrackers;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.controllers.cost.CostEvents.CostFailed;
import kz.taxikz.controllers.cost.CostEvents.CostSuccess;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.CostData;
import kz.taxikz.job.cost.CostJob;
import kz.taxikz.ui.widget.item.NewAddressLocal;

public class CostController extends BaseController {
    private List<NewAddressLocal> mAddresses;
    private AnalyticsTrackers mAnalyticsTrackers;
    private final Bus mBus;
    private final JobManager mJobManager;
    private List<Integer> mOrderDetails;

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    public CostController(Bus mBus, JobManager mJobManager, AnalyticsTrackers mAnalyticsTrackers) {
        this.mBus = mBus;
        this.mJobManager = mJobManager;
        this.mAnalyticsTrackers = mAnalyticsTrackers;
    }

    public void fetchPrice(List<NewAddressLocal> addressList, List<Integer> orderDetails) {
        this.mAddresses = addressList;
        this.mOrderDetails = orderDetails;
        this.mJobManager.addJobInBackground(new CostJob(this, this.mAddresses, this.mOrderDetails));
    }

    public void handleFetchPriceSuccess(BaseApiData<CostData> reply) {
        if (reply.getCode() == ApiConfig.CODE_OK) {
            this.mBus.post(new CostSuccess(reply.getData()));
        } else {
            this.mBus.post(new CostFailed());
        }
    }

    public void handleFetchPriceFailed(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            fetchPrice(this.mAddresses, this.mOrderDetails);
        } else {
            this.mBus.post(new CostFailed());
        }
    }
}

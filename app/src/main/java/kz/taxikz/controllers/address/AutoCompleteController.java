package kz.taxikz.controllers.address;

import android.os.Handler;

import com.birbit.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.controllers.address.AddressEvents.AddressFailed;
import kz.taxikz.controllers.address.AddressEvents.AddressSuccess;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.job.address.AutoCompleteJob;
import kz.taxikz.utils.Preconditions;
import timber.log.Timber;

public class AutoCompleteController extends BaseController {
    private static final int AUTOCOMPLETE_DELAY = 500;
    private Bus mBus;
    private Handler mDelayHandler;
    private JobManager mJobManager;
    private Runnable mRunnable;

    class RunnableImpl implements Runnable {
        final String input;

        RunnableImpl(String input) {
            this.input = input;
        }

        public void run() {
            AutoCompleteController.this.mJobManager.addJobInBackground(new AutoCompleteJob(AutoCompleteController.this, this.input));
        }
    }

    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }

    public AutoCompleteController(Bus bus, JobManager jobManager) {
        this.mBus = bus;
        this.mJobManager = jobManager;
        this.mDelayHandler = new Handler();
        this.mRunnable = null;
    }

    public void autoComplete(String input) {
        if (this.mRunnable != null) {
            this.mDelayHandler.removeCallbacks(this.mRunnable);
            this.mRunnable = null;
        }
        this.mRunnable = new RunnableImpl(input);
        this.mDelayHandler.postDelayed(this.mRunnable, AUTOCOMPLETE_DELAY);
    }

    public void handleAutoCompleteSuccess(BaseApiData<List<AddressData>> baseApiData, String searchString) {
        Preconditions.checkNotNull(baseApiData);
        if (baseApiData.getCode() == ApiConfig.CODE_OK) {
            Timber.d("handleAutoCompleteSuccess");
            this.mBus.post(new AddressSuccess(baseApiData.getData(), searchString));
        }
    }

    public void handleAutoCompleteFailed(Throwable t) {
        Timber.d("handleAutoCompleteSuccess %s", t.toString());
        this.mBus.post(new AddressFailed());
    }
}

package kz.taxikz.controllers.google;

import com.birbit.android.jobqueue.JobManager;

import java.net.SocketTimeoutException;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.job.google.GoogleOrderJob;
import kz.taxikz.job.google.GoogleSignUpJob;
import kz.taxikz.utils.Preconditions;

public class GoogleController extends BaseController {
    private final JobManager mJobManager;
    private String mOrderId;
    private String mToken;

    protected void onInject() {
        TaxiKzApp.get().component().inject((BaseController) this);
    }

    public GoogleController(JobManager jobManager) {
        this.mJobManager = jobManager;
    }

    public void signUpGoogle(String mToken) {
        Preconditions.checkNotNull(mToken);
        this.mToken = mToken;
        this.mJobManager.addJobInBackground(new GoogleSignUpJob(this, mToken));
    }

    public void handleSignUpGoogleError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            signUpGoogle(this.mToken);
        }
    }

    public void orderGoogle(String mToken, String mOrderId) {
        Preconditions.checkNotNull(mToken);
        Preconditions.checkNotNull(mOrderId);
        this.mToken = mToken;
        this.mOrderId = mOrderId;
        this.mJobManager.addJobInBackground(new GoogleOrderJob(this, mToken, mOrderId));
    }

    public void handleOrderGoogleError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            orderGoogle(this.mToken, this.mOrderId);
        }
    }
}

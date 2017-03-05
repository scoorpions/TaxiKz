package kz.taxikz.job;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.squareup.otto.Bus;

import java.net.SocketTimeoutException;

import javax.inject.Inject;

public abstract class BaseJob extends Job {
    private static final int RETRY_LIMIT = 3;
    @Inject
    Bus mBus;
    private Throwable mThrowable;
    private SocketTimeoutException timeoutException;

    protected abstract void onInject();

    public SocketTimeoutException getTimeoutException() {
        return this.timeoutException;
    }

    public BaseJob() {
        super(new Params(5).groupBy(Groups.MAIN_CONTENT));
    }

    public void onAdded() {
        onInject();
    }

    protected void onCancel() {
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }

    protected int getRetryLimit() {
        return RETRY_LIMIT;
    }

    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int a, int b) {
        this.mThrowable = throwable;
        if (!(throwable instanceof SocketTimeoutException)) {
            return RetryConstraint.CANCEL;
        }
        this.timeoutException = (SocketTimeoutException) throwable;
        return RetryConstraint.RETRY;
    }
}

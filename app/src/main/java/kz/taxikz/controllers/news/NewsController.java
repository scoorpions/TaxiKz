package kz.taxikz.controllers.news;

import com.birbit.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import java.net.SocketTimeoutException;
import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.controllers.news.NewsEvents.GetNewsFailed;
import kz.taxikz.controllers.news.NewsEvents.GetNewsSuccess;
import kz.taxikz.data.LocalCache;
import kz.taxikz.data.api.ApiConfig;
import kz.taxikz.data.api.pojo.BaseApiData;
import kz.taxikz.data.api.pojo.News;
import kz.taxikz.job.news.GetNewsJob;
import kz.taxikz.utils.Preconditions;

public class NewsController extends BaseController {
    private final Bus mBus;
    private final JobManager mJobManager;

    public NewsController(Bus bus, JobManager jobManager) {
        this.mJobManager = jobManager;
        this.mBus = bus;
    }

    public void getNews() {
        this.mJobManager.addJobInBackground(new GetNewsJob(this));
    }

    public void handleGetNewsSuccess(BaseApiData<List<News>> reply) {
        Preconditions.checkNotNull(reply);
        if (reply.getCode() == ApiConfig.CODE_OK) {
            LocalCache.getInstance().storeNews(reply.getData());
            this.mBus.post(new GetNewsSuccess(LocalCache.getInstance().readNews()));
            return;
        }
        this.mBus.post(new GetNewsFailed());
    }

    public void handleGetNewsError(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            getNews();
        }
        this.mBus.post(new GetNewsFailed());
    }

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

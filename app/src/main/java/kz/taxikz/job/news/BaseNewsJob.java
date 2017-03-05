package kz.taxikz.job.news;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.NewsService;
import kz.taxikz.job.BaseJob;

public abstract class BaseNewsJob extends BaseJob {
    @Inject
    NewsService newsService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

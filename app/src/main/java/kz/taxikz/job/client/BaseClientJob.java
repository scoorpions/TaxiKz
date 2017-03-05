package kz.taxikz.job.client;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.ClientApiService;
import kz.taxikz.job.BaseJob;

public abstract class BaseClientJob extends BaseJob {
    @Inject
    ClientApiService clientApiService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

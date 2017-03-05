package kz.taxikz.job.order;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.OrderApiService;
import kz.taxikz.job.BaseJob;

public abstract class BaseOrderJob extends BaseJob {
    @Inject
    OrderApiService orderApiService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

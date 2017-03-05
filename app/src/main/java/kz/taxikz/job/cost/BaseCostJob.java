package kz.taxikz.job.cost;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.CostService;
import kz.taxikz.job.BaseJob;

public abstract class BaseCostJob extends BaseJob {
    @Inject
    CostService costService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

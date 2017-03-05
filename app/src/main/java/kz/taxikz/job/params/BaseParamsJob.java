package kz.taxikz.job.params;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.ParamsService;
import kz.taxikz.job.BaseJob;

public abstract class BaseParamsJob extends BaseJob {
    @Inject
    ParamsService mParamsService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

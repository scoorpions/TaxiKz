package kz.taxikz.job.address;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.AutoCompleteService;
import kz.taxikz.job.BaseJob;

public abstract class BaseAutoCompleteJob extends BaseJob {
    @Inject
    AutoCompleteService mAutoCompleteService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

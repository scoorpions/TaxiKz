package kz.taxikz.job.google;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.GoogleService;
import kz.taxikz.job.BaseJob;

public abstract class BaseGoogleJob extends BaseJob {
    @Inject
    GoogleService mGoogleService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

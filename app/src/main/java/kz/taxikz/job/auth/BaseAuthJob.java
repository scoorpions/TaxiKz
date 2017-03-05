package kz.taxikz.job.auth;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.service.AuthService;
import kz.taxikz.job.BaseJob;

public abstract class BaseAuthJob extends BaseJob {
    @Inject
    protected AuthService mAuthService;

    @Override
    protected void onInject() {
        TaxiKzApp.get().component().inject(this);
    }
}

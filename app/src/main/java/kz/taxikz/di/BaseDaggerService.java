package kz.taxikz.di;

import android.app.Service;

import kz.taxikz.TaxiKzApp;

public abstract class BaseDaggerService extends Service {

    public BaseDaggerService() {
        TaxiKzApp.get().component().inject(this);
    }
}

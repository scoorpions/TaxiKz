package kz.taxikz.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.ControllerModule;

@Module(includes = {ControllerModule.class})

public final class TaxiKzAppModule {

    private final TaxiKzApp app;

    public TaxiKzAppModule(TaxiKzApp app) {
        this.app = app;
    }

    @Singleton
    @Provides
    Application provideApplication() {
        return this.app;
    }
}

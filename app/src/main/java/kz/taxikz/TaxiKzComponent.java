package kz.taxikz;

import javax.inject.Singleton;

import dagger.Component;
import kz.taxikz.data.DataModule;
import kz.taxikz.data.api.ApiModule;
import kz.taxikz.di.TaxiKzAppModule;
import kz.taxikz.di.TaxiKzGraph;
import kz.taxikz.ui.UiModule;

@Singleton
@Component(modules = {TaxiKzAppModule.class, DataModule.class, ApiModule.class, UiModule.class})
public interface TaxiKzComponent extends TaxiKzGraph {
    final class Initializer {
        static TaxiKzGraph init(TaxiKzApp taxiKzApp) {
            return DaggerTaxiKzComponent.builder().taxiKzAppModule(new TaxiKzAppModule(taxiKzApp)).build();
        }
        private Initializer() {
        }
    }
}

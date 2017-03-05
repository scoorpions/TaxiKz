package kz.taxikz.di;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.BaseController;
import kz.taxikz.data.db.DatabaseHelper;
import kz.taxikz.data.prefs.PreferenceWrapper;
import kz.taxikz.google.RegistrationIntentService;
import kz.taxikz.job.BaseJob;
import kz.taxikz.job.address.BaseAutoCompleteJob;
import kz.taxikz.job.auth.BaseAuthJob;
import kz.taxikz.job.client.BaseClientJob;
import kz.taxikz.job.cost.BaseCostJob;
import kz.taxikz.job.google.BaseGoogleJob;
import kz.taxikz.job.news.BaseNewsJob;
import kz.taxikz.job.order.BaseOrderJob;
import kz.taxikz.job.params.BaseParamsJob;
import kz.taxikz.ui.address.AddressActivity;
import kz.taxikz.ui.address.fragment.BaseAddressFragment;
import kz.taxikz.ui.auth.fragment.BaseAuthFragment;
import kz.taxikz.ui.main.MainActivity;
import kz.taxikz.ui.news.BaseNewsFragment;
import kz.taxikz.ui.orders.OrdersActivity;
import kz.taxikz.ui.splash.SplashScreensActivity;

public interface TaxiKzGraph {
    void inject(TaxiKzApp taxiKzApp);

    void inject(BaseController baseController);

    void inject(DatabaseHelper databaseHelper);

    void inject(PreferenceWrapper preferenceWrapper);

    void inject(BaseDaggerActivity baseDaggerActivity);

    void inject(BaseDaggerBroadcastReceiver baseDaggerBroadcastReceiver);

    void inject(BaseDaggerDialogFragment baseDaggerDialogFragment);

    void inject(BaseDaggerFragment baseDaggerFragment);

    void inject(BaseDaggerService baseDaggerService);

    void inject(RegistrationIntentService registrationIntentService);

    void inject(BaseJob baseJob);

    void inject(BaseAutoCompleteJob baseAutoCompleteJob);

    void inject(BaseAuthJob baseAuthJob);

    void inject(BaseClientJob baseClientJob);

    void inject(BaseCostJob baseCostJob);

    void inject(BaseGoogleJob baseGoogleJob);

    void inject(BaseNewsJob baseNewsJob);

    void inject(BaseOrderJob baseOrderJob);

    void inject(BaseParamsJob baseParamsJob);

    void inject(AddressActivity addressActivity);

    void inject(BaseAddressFragment baseAddressFragment);

    void inject(BaseAuthFragment baseAuthFragment);

    void inject(MainActivity mainActivity);

    void inject(BaseNewsFragment baseNewsFragment);

    void inject(OrdersActivity ordersActivity);

    void inject(SplashScreensActivity splashScreensActivity);
}

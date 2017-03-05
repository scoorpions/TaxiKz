package kz.taxikz.di;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.analytics.AnalyticsTrackers;
import kz.taxikz.bus.MainThreadEventBus;
import kz.taxikz.controllers.auth.AccountController;
import kz.taxikz.controllers.auth.AuthEvents;
import kz.taxikz.ui.AppContainer;
import kz.taxikz.ui.auth.LoginActivity;
import timber.log.Timber;

public abstract class BaseDaggerActivity extends AppCompatActivity {

    @Inject
    protected Bus eventBus;

    @Inject
    AccountController mAccountController;

    @Inject
    protected AnalyticsTrackers mAnalyticsTrackers;

    @Inject
    protected AppContainer mAppContainer;

    private MainThreadEventBus parentEventListener;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        TaxiKzApp.get().component().inject(this);
        onInject();
        parentEventListener = new MainThreadEventBus() {
            @Subscribe
            public void onUnauthorizedUserDetectedEVentListener(final AuthEvents.UnauthorizedUserDetected unauthorizedUserDetected) {

                Timber.e("onUnauthorizedUserDetectedEVentListener()");
                mAccountController.logOut();
                finish();
                LoginActivity.startActivity(BaseDaggerActivity.this);
            }
        };
    }

    protected abstract void onInject();

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
        eventBus.unregister(parentEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
        eventBus.register(parentEventListener);
    }

    protected void setCustomContentView(final int layoutId) {
        ButterKnife.bind(this, getLayoutInflater().inflate(layoutId, mAppContainer.get(this)));
    }

}

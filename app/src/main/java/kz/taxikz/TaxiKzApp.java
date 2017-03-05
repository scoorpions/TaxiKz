package kz.taxikz;

import android.app.Application;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import kz.taxikz.TaxiKzComponent.Initializer;
import kz.taxikz.data.db.Storage;
import kz.taxikz.di.TaxiKzGraph;
import kz.taxikz.taxi4.BuildConfig;
import kz.taxikz.ui.ActivityHierarchyServer;
import kz.taxikz.utils.CrashlyticsLibrary;
import timber.log.Timber;

public class TaxiKzApp extends Application {
    public static Storage storage;
    private static TaxiKzApp taxiKzApp;
    @Inject
    ActivityHierarchyServer activityHierarchyServer;
    private TaxiKzGraph component;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TaxiKzApp get() {
        return taxiKzApp;
    }

    public void onCreate() {
        super.onCreate();
        storage = Storage.initiate(this);
        taxiKzApp = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
            Timber.plant(new CrashReportingTree());
        }

        buildComponentAndInject();
        registerActivityLifecycleCallbacks(this.activityHierarchyServer);
    }

    private void enabledStrictMode() {
        StrictMode.setThreadPolicy(new Builder().detectAll().penaltyLog().penaltyDeath().build());
    }

    public void buildComponentAndInject() {
        this.component = Initializer.init(this);
        this.component.inject(this);
    }

    public TaxiKzGraph component() {
        return this.component;
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            CrashlyticsLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    CrashlyticsLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    CrashlyticsLibrary.logWarning(t);
                }
            }
        }
    }
}

package kz.taxikz.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.birbit.android.jobqueue.JobManager;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kz.taxikz.analytics.AnalyticsTrackers;
import kz.taxikz.bus.MainThreadEventBus;
import kz.taxikz.data.db.DatabaseHelper;
import kz.taxikz.data.prefs.BooleanPreference;
import kz.taxikz.job.MyJobManager;
import kz.taxikz.ui.auth.IsPhoneValid;

@Module
public final class DataModule {

    @Singleton
    @Provides
    Bus provideEventBus() {
        return new MainThreadEventBus();
    }

    @Singleton
    @Provides
    JobManager provideJobManager(Application application) {
        return new MyJobManager(application);
    }

    @Singleton
    @Provides
    AnalyticsTrackers provideAnalyticsTracker(Application application) {
        return new AnalyticsTrackers(application);
    }

    @Singleton
    @Provides
    public DatabaseHelper provideDatabaseHelper(Application application) {
        return OpenHelperManager.getHelper(application, DatabaseHelper.class);
    }

    @Singleton
    @IsPhoneValid
    @Provides
    BooleanPreference provideIsPhoneValidBooleanPreference(SharedPreferences sharedPreferences) {
        return new BooleanPreference(sharedPreferences, "IsPhoneValid");
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("taxikz", 0);
    }

}

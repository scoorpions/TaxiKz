package kz.taxikz.controllers;

import com.birbit.android.jobqueue.JobManager;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kz.taxikz.analytics.AnalyticsTrackers;
import kz.taxikz.controllers.address.AutoCompleteController;
import kz.taxikz.controllers.auth.AccountController;
import kz.taxikz.controllers.client.ClientController;
import kz.taxikz.controllers.cost.CostController;
import kz.taxikz.controllers.google.GoogleController;
import kz.taxikz.controllers.news.NewsController;
import kz.taxikz.controllers.order.OrderController;
import kz.taxikz.controllers.version.ParamsController;

@Module
public class ControllerModule {
    @Singleton
    @Provides
    AccountController provideAccountController(Bus bus, JobManager jobManager, AnalyticsTrackers analyticsTrackers) {
        return new AccountController(bus, jobManager, analyticsTrackers);
    }

    @Singleton
    @Provides
    CostController provideNCostController(Bus bus, JobManager jobManager, AnalyticsTrackers analyticsTrackers) {
        return new CostController(bus, jobManager, analyticsTrackers);
    }

    @Singleton
    @Provides
    OrderController provideOrderController(Bus bus, JobManager jobManager) {
        return new OrderController(bus, jobManager);
    }

    @Singleton
    @Provides
    AutoCompleteController provideAutoCompleteController(Bus bus, JobManager jobManager) {
        return new AutoCompleteController(bus, jobManager);
    }

    @Singleton
    @Provides
    ClientController provideClientController(Bus bus, JobManager jobManager) {
        return new ClientController(bus, jobManager);
    }

    @Singleton
    @Provides
    NewsController provideNewsController(Bus bus, JobManager jobManager) {
        return new NewsController(bus, jobManager);
    }

    @Singleton
    @Provides
    ParamsController provideVersionController(Bus bus, JobManager jobManager) {
        return new ParamsController(bus, jobManager);
    }

    @Singleton
    @Provides
    GoogleController provideGoogleController(JobManager jobManager) {
        return new GoogleController(jobManager);
    }
}

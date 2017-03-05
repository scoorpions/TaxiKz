package kz.taxikz.ui;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UiModule {

    @Singleton
    @Provides
    AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }

    @Singleton
    @Provides
    ActivityHierarchyServer provideActivityHierarchyServer() {
        return ActivityHierarchyServer.NONE;
    }
}

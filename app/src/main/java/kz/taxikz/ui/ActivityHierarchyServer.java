package kz.taxikz.ui;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import timber.log.Timber;

/**
 * A "view server" adaptation which automatically hooks itself up to all activities.
 */
public interface ActivityHierarchyServer extends ActivityLifecycleCallbacks {
    /**
     * An {@link ActivityHierarchyServer} which does nothing.
     */
    ActivityHierarchyServer NONE = new ActivityHierarchyServer() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Timber.i("onActivityCreated() ");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Timber.i("onActivityStarted() ");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Timber.i("onActivityResumed() ");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Timber.i("onActivityPaused() ");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Timber.i("onActivityStopped() ");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            Timber.i("onActivitySaveInstanceState() ");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Timber.i("onActivityDestroyed() ");
        }
    };
}



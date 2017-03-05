package kz.taxikz.analytics;

import android.app.Application;

import timber.log.Timber;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;

public final class AnalyticsTrackers {
    private Application mApplication;
//    private final Tracker mTracker;

    public AnalyticsTrackers(final Application mApplication) {
        this.mApplication = mApplication;
//        this.mTracker = GoogleAnalytics.getInstance(mApplication).newTracker(R.xml.app_tracker);
    }

    public void numValid(final String s, final String label, final boolean b) {
        Timber.d("numValid\t\t\t%s%s", s,label);
//        Answers.getInstance().logLogin(new LoginEvent().putMethod(label).putSuccess(b));
//        if (!TextUtils.isEmpty(s)) {
//            this.mTracker.set("&uid", s);
//        }
//        this.mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory(Constants.GA_CATEGORY_USER)
//                .setAction(Constants.GA_CATEGORY_LOGIN)
//                .setLabel(label)
//                .build());
    }

    public void activate(final String s, final String label, final boolean b) {
        Timber.d("activate\t\t\t%s%s", s,label);
//        Answers.getInstance().logLogin(new LoginEvent().putMethod(label).putSuccess(b));
//        if (!TextUtils.isEmpty(s)) {
//            Timber.d("USERID %s", s);
//            this.mTracker.set("&uid", s);
//        }
//        this.mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory(Constants.GA_CATEGORY_USER)
//                .setAction(Constants.GA_CATEGORY_ACTIVATE)
//                .setLabel(label)
//                .build());
    }

    public void smsCode(final String s, final String label, final boolean b) {
        Timber.d("smsCode\t\t\t%s%s", s,label);
//        Answers.getInstance().logLogin(new LoginEvent().putMethod(label).putSuccess(b));
//        if (!TextUtils.isEmpty(s)) {
//            this.mTracker.set("&uid", s);
//        }
//        this.mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory(Constants.GA_CATEGORY_USER)
//                .setAction(Constants.GA_CATEGORY_SMS)
//                .setLabel(label)
//                .build());
    }

    public void screenVisited(final String screenName) {
        Timber.d("ScreenVisited\t\t\t%s", screenName);
//        this.mTracker.setScreenName(screenName);
//        this.mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}

package kz.taxikz.data.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.helper.BaseConstants;

@Singleton
public final class PreferenceWrapper {
    @Inject
    SharedPreferences prefs;

    @Inject
    public PreferenceWrapper() {
        TaxiKzApp.get().component().inject(this);
    }


    public long getCinemasLastUpdate() {
        return this.prefs.getLong(BaseConstants.Prefs.LAST_UPDATE_CINEMAS, 0);
    }

    public void setCinemasLastUpdate(long lastUpdate) {
        this.prefs.edit().putLong(BaseConstants.Prefs.LAST_UPDATE_CINEMAS, lastUpdate).apply();
    }

    public long getHospitalsLastUpdate() {
        return this.prefs.getLong(BaseConstants.Prefs.LAST_UPDATE_HOSPITAL, 0);
    }

    public void setHospitalsLastUpdate(long lastUpdate) {
        this.prefs.edit().putLong(BaseConstants.Prefs.LAST_UPDATE_HOSPITAL, lastUpdate).apply();
    }
}

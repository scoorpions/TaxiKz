package kz.taxikz.data.prefs;

import android.content.SharedPreferences;

public class BooleanPreference {
    private final boolean defaultValue;
    private final String key;
    private final SharedPreferences preferences;

    public BooleanPreference(final SharedPreferences sharedPreferences, final String s) {
        this(sharedPreferences, s, false);
    }

    public BooleanPreference(final SharedPreferences preferences, final String key, final boolean defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public void delete() {
        this.preferences.edit().remove(this.key).apply();
    }

    public boolean get() {
        return this.preferences.getBoolean(this.key, this.defaultValue);
    }

    public boolean isSet() {
        return this.preferences.contains(this.key);
    }

    public void set(final boolean b) {
        this.preferences.edit().putBoolean(this.key, b).apply();
    }
}

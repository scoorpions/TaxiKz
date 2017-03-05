package kz.taxikz.data.prefs;

import android.content.SharedPreferences;

public class StringPreference {
    private final String defaultValue;
    private final String key;
    private final SharedPreferences preferences;

    public StringPreference(SharedPreferences preferences, String key) {
        this(preferences, key, null);
    }

    public StringPreference(SharedPreferences preferences, String key, String defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String get() {
        return this.preferences.getString(this.key, this.defaultValue);
    }

    public boolean isSet() {
        return this.preferences.contains(this.key);
    }

    public void set(String value) {
        this.preferences.edit().putString(this.key, value).apply();
    }

    public void delete() {
        this.preferences.edit().remove(this.key).apply();
    }
}

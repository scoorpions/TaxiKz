package kz.taxikz.data.db;

import android.content.Context;
import android.content.SharedPreferences;

import kz.taxikz.ui.news.NewsDetailActivity;
import timber.log.BuildConfig;

@Deprecated
public class Storage {
    private static Storage instance;
    private SharedPreferences prefs;

    private Storage(Context context) {
        setContext(context);
    }

    private Storage setContext(Context context) {
        this.prefs = context.getSharedPreferences(context.getPackageName(), 0);
        return this;
    }

    public void setRegistrationKey(String key) {
        this.prefs.edit().putString("registration_key", key).apply();
    }

    public String getRegistrationKey() {
        return this.prefs.getString("registration_key", BuildConfig.VERSION_NAME);
    }

    public static Storage initiate(Context context) {
        Storage storage = new Storage(context);
        instance = storage;
        return storage;
    }

    public String getPhone() {
        return this.prefs.getString("phone", BuildConfig.VERSION_NAME);
    }

    public void setPhone(String phone) {
        this.prefs.edit().putString("phone", phone).apply();
    }

    public String getCliendId() {
        return this.prefs.getString("client_id", BuildConfig.VERSION_NAME);
    }

    public void setClientId(String clientId) {
        this.prefs.edit().putString("client_id", clientId).apply();
    }

    public String getCityId() {
        return this.prefs.getString("city_id", BuildConfig.VERSION_NAME);
    }

    public void setCityId(String phone) {
        this.prefs.edit().putString("city_id", phone).apply();
    }

    public void setUdsId(int sortMethod) {
        this.prefs.edit().putInt("uds_id", sortMethod).apply();
    }

    public int getCrewGroupId() {
        return this.prefs.getInt("crew_group_id", 0);
    }

    public void setCrewGroup_id(int sortMethod) {
        this.prefs.edit().putInt("crew_group_id", sortMethod).apply();
    }

    public void setCurrentCity(String currentCity) {
        this.prefs.edit().putString("current_town", currentCity).apply();
    }

    public String getCurrentCity() {
        return this.prefs.getString("current_town", BuildConfig.VERSION_NAME);
    }

    public void setPassword(String type) {
        this.prefs.edit().putString("user_password", type).apply();
    }

    public String getPassword() {
        return this.prefs.getString("user_password", BuildConfig.VERSION_NAME);
    }

    public void setFirstRun(Boolean isFirstRun) {
        this.prefs.edit().putBoolean("first_run", isFirstRun.booleanValue()).apply();
    }

    public boolean getFirstRun() {
        return this.prefs.getBoolean("first_run", true);
    }

    public void setName(String name) {
        this.prefs.edit().putString("name", name).apply();
    }

    public String getName() {
        return this.prefs.getString("name", "\u0438\u043c\u044f");
    }

    public void setOrderId(String order_id) {
        this.prefs.edit().putString("order_id", order_id).apply();
    }

    public String getOrderId() {
        return this.prefs.getString("order_id", BuildConfig.VERSION_NAME);
    }

    public void setParams(String params) {
        this.prefs.edit().putString("params", params).apply();
    }

    public String getParams() {
        return this.prefs.getString("params", BuildConfig.VERSION_NAME);
    }

    public boolean isNews() {
        return this.prefs.getBoolean(NewsDetailActivity.EXTRA_NEWS, false);
    }

    public void setNews(boolean isNews) {
        this.prefs.edit().putBoolean(NewsDetailActivity.EXTRA_NEWS, isNews).apply();
    }

    public void setOrderTime(String order_time) {
        this.prefs.edit().putString("order_time", order_time).apply();
    }

    public void setBonus(int bonus) {
        this.prefs.edit().putInt("bonus", bonus).apply();
    }

    public int getBonus() {
        return this.prefs.getInt("bonus", 0);
    }

    public void setOrderBonus(int bonus) {
        this.prefs.edit().putInt("order_bonus", bonus).apply();
    }

    public int getOrderBonus() {
        return this.prefs.getInt("order_bonus", 0);
    }

    public void setBonusActive(boolean useBonus) {
        this.prefs.edit().putBoolean("use_bonus", useBonus).apply();
    }

    public boolean isBonusActive() {
        return this.prefs.getBoolean("use_bonus", false);
    }

    public void setSpendBonusLimit(int limit) {
        this.prefs.edit().putInt("spend_bonus_limit", limit).apply();
    }

    public int getSpendBonusLimit() {
        return this.prefs.getInt("spend_bonus_limit", 0);
    }

    public void setLastAppVersion(int version) {
        this.prefs.edit().putInt("last_app_version", version).apply();
    }

    public int getLastAppVersion() {
        return this.prefs.getInt("last_app_version", 51);
    }

    public void setAboutAppContent(String content) {
        this.prefs.edit().putString("about_app_content", content).apply();
    }

    public String getAboutAppContent() {
        return this.prefs.getString("about_app_content", BuildConfig.VERSION_NAME);
    }
}

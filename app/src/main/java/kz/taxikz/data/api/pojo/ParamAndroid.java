package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class ParamAndroid {
    @SerializedName("agreement")
    protected String agreement;
    @SerializedName("bonus_limit")
    protected String bonus_limit;
    @SerializedName("current_version")
    protected int current_version;
    @SerializedName("min_version")
    protected int min_version;
    @SerializedName("taxikz_about")
    protected String taxiKzAboutApp;
    @SerializedName("use_bonus")
    protected String use_bonus;
    @SerializedName("youtube_url_instructions")
    protected String youtube_url_instructions;

    public int getMinVersion() {
        return this.min_version;
    }

    public int getCurrentVersion() {
        return this.current_version;
    }

    public boolean isBonusActive() {
        boolean z = true;
        if (this.use_bonus == null) {
            return false;
        }
        if (Integer.valueOf(this.use_bonus).intValue() != 1) {
            z = false;
        }
        return z;
    }

    public int getBonusLimit() {
        if (this.bonus_limit == null) {
            return 0;
        }
        return Integer.valueOf(this.bonus_limit).intValue();
    }

    public String getYoutubeUrlInstructions() {
        return this.youtube_url_instructions;
    }

    public String getAgreement() {
        return this.agreement;
    }

    public String getTaxiKzAboutApp() {
        return this.taxiKzAboutApp;
    }
}

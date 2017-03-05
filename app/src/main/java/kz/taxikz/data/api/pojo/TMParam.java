package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class TMParam {
    @SerializedName("city_id")
    protected int city_id;
    @SerializedName("city_name")
    protected String city_name;
    @SerializedName("crew_group_id")
    protected int crew_group_id;
    @SerializedName("tariff_id")
    protected int tariff_id;
    @SerializedName("uds_id")
    protected int uds_id;

    public int getTariffId() {
        return this.tariff_id;
    }

    public int getCityId() {
        return this.city_id;
    }

    public int getCrewGroupId() {
        return this.crew_group_id;
    }

    public int getUdsId() {
        return this.uds_id;
    }

    public String getCityName() {
        return this.city_name;
    }
}

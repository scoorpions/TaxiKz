package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {
    @SerializedName("house_id")
    private String houseId;
    @SerializedName("street_id")
    private int streetId;
    @SerializedName("street_name")
    private String streetName;

    public int getStreetId() {
        return this.streetId;
    }

    public String getStreetName() {
        return this.streetName;
    }

    public String getHouseId() {
        return this.houseId;
    }
}

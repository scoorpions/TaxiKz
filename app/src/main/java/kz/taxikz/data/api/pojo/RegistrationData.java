package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class RegistrationData {
    @SerializedName("key")
    protected String key;
    @SerializedName("name")
    protected String name;
    @SerializedName("phone")
    protected String phone;

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }
}

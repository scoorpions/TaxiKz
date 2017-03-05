package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class UpdateClientInfo {
    @SerializedName("code")
    protected int code;

    public int getCode() {
        return this.code;
    }
}

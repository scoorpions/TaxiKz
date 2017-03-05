package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class ClientInfoData {
    @SerializedName("data")
    private ClientInfo clientInfo;
    @SerializedName("code")
    private int code;

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public int getCode() {
        return this.code;
    }
}

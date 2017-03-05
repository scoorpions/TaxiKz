package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class CredentialsData {
    @SerializedName("client_id")
    protected String client_id;
    @SerializedName("password")
    protected String password;

    public String getPassword() {
        return this.password;
    }

    public String getClientId() {
        return this.client_id;
    }
}

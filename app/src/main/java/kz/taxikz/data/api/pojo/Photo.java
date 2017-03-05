package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photo implements Serializable {
    @SerializedName("url")
    protected String url;

    public String getUrl() {
        return this.url;
    }
}

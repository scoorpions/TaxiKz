package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class CreateOrderDataItem {
    @SerializedName("code")
    protected int code;
    @SerializedName("data")
    protected CreateOrderDataItemData data;
    @SerializedName("descr")
    protected String descr;

    public int getCode() {
        return this.code;
    }

    public String getDescr() {
        return this.descr;
    }

    public CreateOrderDataItemData getData() {
        return this.data;
    }
}

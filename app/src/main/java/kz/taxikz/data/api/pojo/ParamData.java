package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParamData {
    @SerializedName("android_var")
    protected ParamAndroid android_var;
    @SerializedName("tm_order_params")
    protected List<TMOrderParam> tm_order_params;
    @SerializedName("tm_params")
    protected List<TMParam> tm_params;

    public List<TMOrderParam> getTmOrderParams() {
        return this.tm_order_params;
    }

    public List<TMParam> getTmParams() {
        return this.tm_params;
    }

    public ParamAndroid getAndroidVar() {
        return this.android_var;
    }
}

package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class Cost {
    @SerializedName("comment")
    protected String comment;
    @SerializedName("sum")
    protected String sum;

    public String getComment() {
        return this.comment;
    }

    public String getSum() {
        return this.sum;
    }
}

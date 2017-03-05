package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class CreateOrderDataItemData {
    @SerializedName("order_id")
    protected String order_id;

    public String getOrderId() {
        return this.order_id;
    }
}

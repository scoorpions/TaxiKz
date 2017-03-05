package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CostData {
    @SerializedName("check")
    protected List<Cost> costList;
    @SerializedName("sum")
    protected int sum;

    public int getSum() {
        return this.sum;
    }

    public List<Cost> getCostList() {
        return this.costList;
    }
}

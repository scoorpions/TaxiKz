package kz.taxikz.data.api.pojo;

import java.util.List;

public class CreateOrderParam {
    private List<String> mAddresses;
    private String mAutodialPhone;
    private int mBonusSum;
    private String mCityId;
    private String mClientId;
    private String mComment;
    private String mCustomer;
    private List<Integer> mOrderParams;
    private String mPhone;
    private String mPorch;
    private int mTotalCost;

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public void setAutodialPhone(String phone) {
        this.mAutodialPhone = phone;
    }

    public void setClientId(String client_id) {
        this.mClientId = client_id;
    }

    public void setAddresses(List<String> addresses) {
        this.mAddresses = addresses;
    }

    public void setCityId(String city_id) {
        this.mCityId = city_id;
    }

    public void setPorch(String porch) {
        this.mPorch = porch;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public void setCustomer(String customer) {
        this.mCustomer = customer;
    }

    public void setOrderParams(List<Integer> order_params) {
        this.mOrderParams = order_params;
    }

    public void setTotalCost(int total_cost) {
        this.mTotalCost = total_cost;
    }

    public void setBonusSum(int bonus_sum) {
        this.mBonusSum = bonus_sum;
    }

    public String getPhone() {
        return this.mPhone;
    }

    public String getAutodialPhone() {
        return this.mAutodialPhone;
    }

    public String getClientId() {
        return this.mClientId;
    }

    public List<String> getAddresses() {
        return this.mAddresses;
    }

    public String getCityId() {
        return this.mCityId;
    }

    public String getPorch() {
        return this.mPorch;
    }

    public String getComment() {
        return this.mComment;
    }

    public String getCustomer() {
        return this.mCustomer;
    }

    public List<Integer> getOrderParams() {
        return this.mOrderParams;
    }

    public int getTotalCost() {
        return this.mTotalCost;
    }

    public int getBonusSum() {
        return this.mBonusSum;
    }
}

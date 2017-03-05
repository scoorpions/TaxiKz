package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class BaseApiData<T> {
    @SerializedName("code")
    protected int code;
    @SerializedName("data")
    protected T data;
    @SerializedName("text")
    protected String text;

    public int getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }

    public T getData() {
        return this.data;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseApiData)) {
            return false;
        }
        BaseApiData that = (BaseApiData) o;
        if (getCode() == that.getCode()) {
            if (getData() != null) {
                if (getData().equals(that.getData())) {
                    return true;
                }
            } else if (that.getData() == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (getCode() * 31) + (getData() != null ? getData().hashCode() : 0);
    }
}

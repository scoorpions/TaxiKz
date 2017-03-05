package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressData implements Serializable {
    public static final String TYPE_POINT = "point";
    public static final String TYPE_STREET = "street";
    @SerializedName("address")
    private Address address;
    @SerializedName("category")
    private int category;
    @SerializedName("city_id")
    private int cityId;
    @SerializedName("has_address")
    private int hasAddress;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getCategory() {
        return this.category;
    }

    public int getHasAddress() {
        return this.hasAddress;
    }

    public int getCityId() {
        return this.cityId;
    }

    public Address getAddress() {
        return this.address;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(this.id);
        sb.append(", ");
        sb.append("name=");
        sb.append(this.name);
        sb.append(", ");
        sb.append("type=");
        sb.append(this.type);
        sb.append(", ");
        sb.append("cityId=");
        sb.append(this.cityId);
        sb.append(", ");
        sb.append("category=");
        sb.append(this.category);
        sb.append(", ");
        sb.append("hasAddress=");
        sb.append(this.hasAddress);
        sb.append(", ");
        sb.append("address=");
        sb.append(this.address == null ? "[NO ADDRESS]" : "[ADDRESS]");
        return sb.toString();
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.name != null) {
            result = this.name.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.address != null) {
            i = this.address.hashCode();
        }
        return ((((i2 + i) * 31) + this.id) + this.cityId) + this.category;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressData that = (AddressData) o;
        if (this.name == null ? that.name != null : !this.name.equals(that.name)) {
            return false;
        }
        if (this.type == null ? that.type != null : !this.type.equals(that.type)) {
            return false;
        }
        if (this.id != that.id) {
            return false;
        }
        return true;
    }
}

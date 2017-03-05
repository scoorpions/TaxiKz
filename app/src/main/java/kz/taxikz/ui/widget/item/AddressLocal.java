package kz.taxikz.ui.widget.item;

import java.io.Serializable;

public class AddressLocal implements Serializable {

    private String name;
    private String address;
    private String homeNum;
    private String porch;
    private String type;
    private String key;
    private String category;

    public AddressLocal(String name, String address, String porch, String homeNum, String type, String key, String category) {
        this.name = name;
        this.address = address;
        this.porch = porch;
        this.homeNum = homeNum;
        this.type = type;
        this.key = key;
        this.category = category;
    }

    public AddressLocal() {}

    @Override
    public String toString() {
        return "AddressLocal{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", homeNum='" + homeNum + '\'' +
                ", porch='" + porch + '\'' +
                ", type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public String getName() {
        if (name == null) return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        if (address == null) return "";
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomeNum() {
        if (homeNum == null) return "";
        return homeNum;
    }

    public void setHomeNum(String homeNum) {
        this.homeNum = homeNum;
    }

    public String getType() {
        if (type == null) return "";
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        if (key == null) return "";
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPorch() {
        if (porch == null) return "";
        return porch;
    }

    public void setPorch(String porch) {
        this.porch = porch;
    }

    public String getCategory() {
        if (category == null) return "";
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressLocal that = (AddressLocal) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (homeNum != null ? !homeNum.equals(that.homeNum) : that.homeNum != null) return false;
        if (porch != null ? !porch.equals(that.porch) : that.porch != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return category != null ? category.equals(that.category) : that.category == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (homeNum != null ? homeNum.hashCode() : 0);
        result = 31 * result + (porch != null ? porch.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
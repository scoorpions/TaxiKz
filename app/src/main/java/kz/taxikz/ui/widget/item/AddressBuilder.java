package kz.taxikz.ui.widget.item;

public class AddressBuilder {
    private String name;
    private String address;
    private String homeNum;
    private String porch;
    private String type;
    private String category;
    private String key;

    public AddressBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AddressBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public AddressBuilder setHomeNum(String homeNum) {
        this.homeNum = homeNum;
        return this;
    }

    public AddressBuilder setPorch(String porch) {
        this.porch = porch;
        return this;
    }

    public AddressBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public AddressBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public AddressBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public AddressLocal createAddress() {
        return new AddressLocal(name, address, homeNum, porch, type, category, key);
    }
}
package kz.taxikz.controllers.address;

import java.util.List;

import kz.taxikz.data.api.pojo.AddressData;

public class AddressEvents {

    public static class AddressFailed {
    }

    public static class AddressSocketTimeout {
    }

    public static class AddressSuccess {
        List<AddressData> addressList;
        String searchString;

        public String getSearchString() {
            return this.searchString;
        }

        public List<AddressData> getAddressList() {
            return this.addressList;
        }

        public AddressSuccess(List<AddressData> addressList, String searchString) {
            this.addressList = addressList;
            this.searchString = searchString;
        }
    }
}
